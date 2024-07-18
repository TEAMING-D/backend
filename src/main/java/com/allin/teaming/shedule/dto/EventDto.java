package com.allin.teaming.shedule.dto;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.user.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
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
        @NotBlank
        private Long scheduleId;

        private String title;
        private String info;

        @NotBlank
        private Week week;

        @NotBlank
        private LocalTime start_time;

        @NotBlank
        private LocalTime end_time;

        public Event toEvent(Schedule schedule) {
            return Event.builder()
                    .schedule(schedule)
                    .title(title)
                    .info(info)
                    .week(week)
                    .start_time(start_time)
                    .end_time(end_time)
                    .build();
        }
    }

    @Getter
    public static class EventModifyDto {
        private String title;
        private String info;
        private Week week;
        private LocalTime start_time;
        private LocalTime end_time;
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
