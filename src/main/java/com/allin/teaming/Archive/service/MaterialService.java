package com.allin.teaming.Archive.service;

import com.allin.teaming.Archive.domain.Material;
import com.allin.teaming.Archive.domain.WorkMaterial;
import com.allin.teaming.Archive.dto.MaterialCreateRequestDto;
import com.allin.teaming.Archive.dto.MaterialCreateResponseDto;
import com.allin.teaming.Archive.dto.MaterialResponseDto;
import com.allin.teaming.Archive.repository.MaterialRepository;
import com.allin.teaming.Archive.repository.WorkMaterialRepository;
import com.allin.teaming.user.Jwt.JwtUtil;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserSimpleDto;
import com.allin.teaming.user.repository.UserRepository;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.workspace.domain.Workspace;
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

    // --------------------------------------------------------------------

    /*
    자료 생성
     */
    @Transactional
    public MaterialCreateResponseDto createMaterial(String token, MaterialCreateRequestDto request) {
        if (request.getFilename() == null) throw new IllegalArgumentException("파일 이름을 입력하지 않았습니다. ");
        User owner = findUserByToken(token);
        Material material = request.toEntity(owner);
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
        return MaterialCreateResponseDto.toDto(material, workNames, url, UserSimpleDto.of(owner));
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
    public List<MaterialResponseDto> getAllMaterial() {
        List<Material> materials = materialRepository.findAll();
        return materials.stream().map(MaterialResponseDto::toDto).toList();

    }

    // 워크스페이스 내 자료 전체 조회
    public List<MaterialResponseDto> getAllMaterialByWorkspaceId(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스를 조회할 수 없습니다. "));

        Set<Material> materials = new HashSet<>(materialRepository.findAllByWorkspaceId(workspaceId));

        return materials.stream().map(MaterialResponseDto::toDto).toList();
    }

    // 나의 자료 전체 조회(삭제에 사용)

    // 업무의 자료 전체 조회

    // 선택한 자료의 CDNUrl 반환 -> TODO Redis 사용

    // 업무에 자료List 추가 (자료에 업무 추가)

    // 자료 이름으로 조회

    //TODO : 자료 수정 (복사 -> 삭제)
}
