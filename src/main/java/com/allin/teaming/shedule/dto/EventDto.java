package com.allin.teaming.shedule.dto;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

public class EventDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class EventDetailDto {
        private Long id;
        private Long scheduleId;
        private String title;
        private String info;
        private Week week;
        private LocalTime start_time;
        private LocalTime end_time;

        static public EventDetailDto of(Event event) {
            return EventDetailDto.builder()
                    .id(event.getId())
                    .scheduleId(event.getSchedule().getId())
                    .title(event.getTitle())
                    .info(event.getInfo())
                    .week(event.getWeek())
                    .start_time(event.getStart_time())
                    .end_time(event.getEnd_time())
                    .build();
        }
    }

    // TODO : registDto 개발
    @Getter
    static public class EventRegistDto {
        private String title;
        public Event toEvent() {
            return  Event.builder()
                    .title(title)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(Event event) {
            return new IdResponse(event.getId());
        }
    }
}
