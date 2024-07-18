package com.allin.teaming.shedule.controller;

import com.allin.teaming.shedule.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    // TODO : schedule CRUD 개발
    // schedule 조회

}
