package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.WorkdayCalendarEntryInputDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingDayOutputDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingDaysOutputDto;
import de.carstenkremser.workhourcapture.backend.dto.WorkingTimeOutputDto;
import de.carstenkremser.workhourcapture.backend.model.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppDateAndTimeConverter {

    public String convertDurationToString(Duration duration) {
        if (duration == null) {
            return "0:00";
        }
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = Math.abs((seconds % 3600) / 60);

        return String.format("%1d:%02d", hours, minutes);
    }

    public String convertLocalTimeToString(LocalTime localTime) {
        if (localTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(formatter);
    }

    public String convertLocalDateToString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(formatter);
    }

    public LocalDate convertStringToLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }

    public WorkingDaysOutputDto convertWorkingDaysToDto(WorkingDays workingDays) {
        if (workingDays == null) {
            return new WorkingDaysOutputDto("", "","", List.of());
        }
        return new WorkingDaysOutputDto(
                convertDurationToString(workingDays.allocated()),
                convertDurationToString(workingDays.worked()),
                convertDurationToString(workingDays.difference()),
                workingDays.workingDays()
                        .stream()
                        .map(this::convertWorkingDayToDto)
                        .toList()
        );
    }

    public WorkingDayOutputDto convertWorkingDayToDto(WorkingDay workingDay) {
        if (workingDay == null) {
            return new WorkingDayOutputDto("", "", "", "", List.of());
        }
        return new WorkingDayOutputDto(
                convertLocalDateToString(workingDay.getDate()),
                convertDurationToString(workingDay.getAllocated()),
                convertDurationToString(workingDay.getWorkingTime()),
                convertDurationToString(workingDay.getDifference()),
                workingDay.getWorkingTimes()
                        .stream()
                        .map(this::convertWorkingTimeToDto)
                        .toList()
        );
    }

    public WorkingTimeOutputDto convertWorkingTimeToDto(WorkingTime workingTime) {
        if (workingTime == null) {
            return null;
        }
        String workStartDate = "";
        String workStartTime = "";
        String workEndDate = "";
        String workEndTime = "";
        if (workingTime.workStart() != null) {
            workStartDate = convertLocalDateToString(workingTime.workStart().getDate());
            workStartTime = convertLocalTimeToString(workingTime.workStart().getTime());
        }
        if (workingTime.workEnd() != null) {
            workEndDate = convertLocalDateToString(workingTime.workEnd().getDate());
            workEndTime = convertLocalTimeToString(workingTime.workEnd().getTime());
        }
        return new WorkingTimeOutputDto(
                workStartDate,
                workStartTime,
                workEndDate,
                workEndTime,
                convertDurationToString(workingTime.duration())
        );
    }

    public WorkdayCalendarEntry convertToWorkdayCalendarEntry(WorkdayCalendarEntryInputDto dto) {
        if (dto == null) {
            return null;
        }
        Duration scheduledHours = Duration.ZERO;
        if (dto.scheduledHours() != null && dto.scheduledHours().length() > 3 && dto.scheduledHours().contains(":")) {
            int minutes = Integer.parseInt(dto.scheduledHours().split(":")[1]);
            int hours = Integer.parseInt(dto.scheduledHours().split(":")[0]);
            scheduledHours = Duration.ofHours(hours).plusMinutes(minutes);
        }
        return new WorkdayCalendarEntry(
                "",
                dto.evaluationPriority(),//int evaluationPriority,
                dto.userId(),//String userId,
                WorkdayCalendarEntryType.valueOf(dto.entryType()), //WorkdayCalendarEntryType entryType,
                scheduledHours, //Duration scheduledHours,
                convertStringToLocalDate(dto.dateFrom()), //LocalDate dateFrom,
                convertStringToLocalDate(dto.dateTo()), //LocalDate dateTo,
                DayOfWeek.valueOf(dto.dayOfWeek()), //DayOfWeek dayOfWeek,
                dto.annotation() //String annotation
        );
    }
}
