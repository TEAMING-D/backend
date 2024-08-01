package com.allin.teaming.user.service;

import com.allin.teaming.user.domain.School;
import com.allin.teaming.user.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<School> getAllSchool() {
        return schoolRepository.findAll();
    }
}
