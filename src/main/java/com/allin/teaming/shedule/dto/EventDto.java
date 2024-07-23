package com.allin.teaming.shedule.dto;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
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

    @Getter
    static public class EventRegistDto {
        @NotNull
        private Long scheduleId;

        private String title;
        private String info;

        @NotNull
        private Week week;

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        private LocalTime start_time;

        @NotNull
        @JsonFormat(pattern = "HH:mm")
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

        @JsonFormat(pattern = "HH:mm")
        private LocalTime start_time;

        @JsonFormat(pattern = "HH:mm")
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
