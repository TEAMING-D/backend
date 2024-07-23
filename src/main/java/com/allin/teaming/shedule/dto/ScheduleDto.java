package com.allin.teaming.shedule.dto;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class ScheduleDetailDto {
        private Long id;
        private String title;
        private Long userId;
        private List<EventDto.EventDetailDto> events = new ArrayList<>();

        public static ScheduleDetailDto of(Schedule schedule) {
            return ScheduleDetailDto.builder()
                    .id(schedule.getId())
                    .title(schedule.getTitle())
                    .userId(schedule.getUser().getId())
                    .events(schedule.getEvents().stream()
                            .map(EventDto.EventDetailDto::of).toList())
                    .build();
        }
    }

    @Getter
    static public class ScheduleCreateDto {
        private String title;

        // Long 타입에는 notnull 적용해야 함
        // String 타입에는 notBlank
        @NotNull
        private Long userId;

        public Schedule toSchedule(User user) {
            return Schedule.builder()
                    .title(title)
                    .user(user)
                    .build();
        }
    }

    @Getter
    static public class ScheduleModifyDto {
        private String title;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(Schedule schedule) {
            return new IdResponse(schedule.getId());
        }
    }
}
