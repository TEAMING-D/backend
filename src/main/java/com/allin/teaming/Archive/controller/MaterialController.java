package com.allin.teaming.Archive.controller;

import com.allin.teaming.Archive.dto.MaterialCreateRequestDto;
import com.allin.teaming.Archive.dto.MaterialResponseDto;
import com.allin.teaming.Archive.dto.MaterialUpdateWorkRequestDto;
import com.allin.teaming.Archive.service.MaterialService;
import com.allin.teaming.Response.BasicResponse;
import com.allin.teaming.Response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    // 자료 생성
    @PostMapping
    public ResponseEntity<? extends BasicResponse> createMaterial(
            @RequestHeader("Authorization") String token,
            @RequestBody MaterialCreateRequestDto request) {
        return ResponseEntity.ok(new DataResponse<>(materialService.createMaterial(token, request)));
    }

    // 자료 삭제
    @DeleteMapping("/{material_id}")
    public ResponseEntity<String> deleteMaterial(
            @PathVariable("material_id") Long materialId) {
        return ResponseEntity.ok(materialService.deleteMaterial(materialId));
    }

    // 조회
    // 자료 전체 조회
    @GetMapping
    public ResponseEntity<? extends BasicResponse> getAllMaterial() {
        return ResponseEntity.ok(new DataResponse<>(materialService.getAllMaterial()));
    }

    // 워크스페이스 내 자료 전체 조회
    @GetMapping("/workspace/{workspace_id}")
    public ResponseEntity<? extends BasicResponse> getAllMaterialByWorkspaceId(
            @PathVariable("workspace_id") Long workspaceId) {
        return ResponseEntity.ok(new DataResponse<>(materialService.getAllMaterialByWorkspaceId(workspaceId)));
    };

    // 나의 자료 전체 조회(삭제에 사용)
    @GetMapping("/my")
    public ResponseEntity<? extends BasicResponse> getAllMyMaterial(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new DataResponse<>(materialService.getAllMyMaterial(token)));
    }

    // 업무의 자료 전체 조회
    @GetMapping("/work/{work_id}")
    public ResponseEntity<? extends BasicResponse> getAllMaterialByWorkId(
            @PathVariable("work_id") Long workId) {
        return ResponseEntity.ok(new DataResponse<>(materialService.getAllMaterialByWorkId(workId)));
    }

    // 업무에 자료List 추가 (자료에 업무 추가)
    @PutMapping("/work")
    public ResponseEntity<? extends BasicResponse> updateWork(
            @RequestBody MaterialUpdateWorkRequestDto request) {
        return ResponseEntity.ok(new DataResponse<>(materialService.updateWork(request)));
    }


    // 자료 url 조회
    @GetMapping("/url/{material_id}")
    public ResponseEntity<? extends BasicResponse> getUrl(
            @PathVariable("material_id") Long materialId) {
        return ResponseEntity.ok(new DataResponse<>(materialService.getUrl(materialId)));
    }
}
