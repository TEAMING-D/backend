package com.allin.teaming.user.controller;

import com.allin.teaming.user.domain.School;
import com.allin.teaming.user.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @GetMapping("/schools")
    public ResponseEntity<List<School>> getAllSchool() {
        return ResponseEntity.ok(schoolService.getAllSchool());
    }
}
