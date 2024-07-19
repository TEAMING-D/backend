package com.allin.teaming.shedule.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ReccuringEvent extends Event {
    // 요일
    @Enumerated(EnumType.STRING)
    private Week week;
}

