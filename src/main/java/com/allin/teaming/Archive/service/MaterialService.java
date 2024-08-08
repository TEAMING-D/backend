package com.allin.teaming.Archive.service;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.Archive.domain.WorkMaterial;
import com.allin.teaming.Archive.dto.*;
import com.allin.teaming.Archive.repository.MaterialRepository;
import com.allin.teaming.Archive.repository.WorkMaterialRepository;
import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserSimpleDto;
import com.allin.teaming.user.repository.MembershipRepository;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceSimpleResponseDto;
import com.allin.teaming.workspace.repository.WorkRepository;
import com.allin.teaming.workspace.repository.WorkspaceRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final MaterialRepository materialRepository;
    private final WorkMaterialRepository workMaterialRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MembershipRepository membershipRepository;
    private final JwtUtil jwtUtil;

    /*
     ----------------------- 자료 presigned-url 생성 ---------------------
     */
    @Value("${amazon.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    private URL getPresignedUrl(String filePath) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, filePath);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String filename) {
        return new GeneratePresignedUrlRequest(bucket, filename)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration());
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + (1000 * 60 * 2));
        return expiration;
    }

    // TODO : 유일한 파일이름으로 변경
    private String createPath(String prefix, String filename) {
        return String.format("%s/%s", prefix, filename);
    }

    // --------------------------------------------------------------------

    /*
    CDN url 생성
     */
    @Value("${amazon.aws.cloudfront.domain}")
    private String domain;

    private String getCDNUrl(String filePath) {
        if (filePath == null) return null;
        return domain + "/" + filePath;
    }

    // --------------------------------------------------------------------

    /*
    repository 에서 조회
     */
    private User findUserByToken(String token) {
        return userRepository.findByEmail(jwtUtil.getEmail(token.split(" ")[1]))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. "));
    }

    private Work findWorkById(Long workId) {
        return workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("해당 업무를 조회할 수 없습니다. "));
    }

    private Material findMaterialById(Long materialId) {
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자료를 조회할 수 없습니다. "));
    }

    private Workspace findWorkspaceById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 조회할 수 없습니다. "));
    }

    private Membership findMembershipByUserAndWorkspace(User user, Workspace workspace) {
        return membershipRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버십을 조회할 수 없습니다. "));
    }
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. "));
    }

    // material dto 추출
    private MaterialResponseDto extractMaterialDto(Material material) {
        return MaterialResponseDto.toDto(
                material,
                extractUserDto(material),
                extractWorkspaceDto(material));
    }

    // user(owner) dto 추출
    private UserSimpleDto extractUserDto(Material material) {
        return UserSimpleDto.of(material.getMembership().getUser());
    }
    // workspace dto 추출
    private WorkspaceSimpleResponseDto extractWorkspaceDto(Material material) {
        return WorkspaceSimpleResponseDto.toDto(material.getMembership().getWorkspace());
    }

    // --------------------------------------------------------------------

    /*
    자료 생성
     */
    @Transactional
    public MaterialCreateResponseDto createMaterial(String token, MaterialCreateRequestDto request) {
        if (request.getFilename() == null) throw new IllegalArgumentException("파일 이름을 입력하지 않았습니다. ");
        User owner = findUserByToken(token);
        Workspace workspace = findWorkspaceById(request.getWorkspaceId());
        Membership membership = findMembershipByUserAndWorkspace(owner, workspace);

        Material material = request.toEntity(membership);
        materialRepository.save(material);

        List<Work> works = request.getWorkIds().stream().map(this::findWorkById).toList();
        List<WorkMaterial> savedMaterial = new ArrayList<>();

        List<String> workNames = new ArrayList<>();
        works.forEach(work -> {
            workNames.add(work.getName());
            savedMaterial.add(WorkMaterial.builder()
                    .material(material)
                    .work(work)
                    .build());
        });
        workMaterialRepository.saveAll(savedMaterial);

        String filaPath = createPath("materialId:" + material.getId(), material.getFilename());
        URL url = getPresignedUrl(filaPath);
        return MaterialCreateResponseDto.toDto(material, workNames, url, UserSimpleDto.of(owner), WorkspaceSimpleResponseDto.toDto(workspace));
    }

    // 자료 삭제
    @Transactional
    public String deleteMaterial(Long materialId) {
        Material material = findMaterialById(materialId);
        String filename = material.getFilename();
        String filePath = createPath("materialId:" + materialId, filename);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, filePath));
            materialRepository.delete(material);
            return "Object deleted successfully: " + filename;
        } catch (Exception e) {
            return "Error deleting object: " + e.getMessage();
        }
    }

    /*
    조회
     */
    // 자료 전체 조회
    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getAllMaterial() {
        List<Material> materials = materialRepository.findAll();
        return materials.stream().map(this::extractMaterialDto).toList();
    }

    // 워크스페이스 내 자료 전체 조회
    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getAllMaterialByWorkspaceId(Long workspaceId) {
        Set<Material> materials = new HashSet<>(materialRepository.findAllByWorkspaceId(workspaceId));
        return materials.stream().map(this::extractMaterialDto).toList();
    }

    // 나의 자료 전체 조회(삭제에 사용)
    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getAllMyMaterial(String token) {
        User user = findUserByToken(token);
        Set<Material> materials = new HashSet<>(materialRepository.findAllByUserId(user.getId()));
        return materials.stream().map(this::extractMaterialDto).toList();
    }


    // 업무의 자료 전체 조회
    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getAllMaterialByWorkId(Long workId) {
        List<Material> materials = materialRepository.findAllByWorkId(workId);
        return materials.stream().map(this::extractMaterialDto).toList();
    }

    // 업무에 자료List 추가 (자료에 업무 추가)
    @Transactional
    public MaterialResponseDto updateWork(MaterialUpdateWorkRequestDto request) {
        Material material = findMaterialById(request.getMaterialId());

        // 수정 후 일 정보
        Set<Long> workIdSet = new HashSet<>(request.getWorkIds());
        List<Work> afterWorks = workIdSet.stream().map(this::findWorkById).toList();

        // 기존의 일 목록
        List<WorkMaterial> beforeWorkMaterial = material.getWorkMaterials();
        List<Work> beforeWork = beforeWorkMaterial.stream().map(
                WorkMaterial::getWork).toList();

        // 기존의 일 목록에서 삭제해야 할 일 목록
        List<WorkMaterial> deleteWorkMaterials = beforeWorkMaterial.stream()
                .filter(wm -> !afterWorks.contains(wm.getWork()))
                .toList();


        List<WorkMaterial> addWorkMaterials = afterWorks.stream()
                .filter(w -> !beforeWork.contains(w))
                .map(w -> WorkMaterial.builder()
                        .material(material)
                        .work(w).build())
                .toList();


        material.getWorkMaterials().removeAll(deleteWorkMaterials);
        material.getWorkMaterials().addAll(addWorkMaterials);


        workMaterialRepository.deleteAll(deleteWorkMaterials);
        workMaterialRepository.saveAll(addWorkMaterials);

        Material updatedMaterial = materialRepository.save(material);

        return extractMaterialDto(updatedMaterial);
    }

    //TODO : 자료 수정 (복사 -> 삭제)

    // 자료 url 반환 -> TODO Redis 사용
    @Transactional(readOnly = true)
    public MaterialUrlResponseDto getUrl(Long materialId) {
        Material material = findMaterialById(materialId);
        String filePath = createPath("materialId:" + materialId, material.getFilename());
        String url = getCDNUrl(filePath);
        return MaterialUrlResponseDto.toDto(material, url);
    }
}
