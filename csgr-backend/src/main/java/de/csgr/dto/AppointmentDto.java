package de.csgr.dto;

import de.csgr.entity.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AppointmentDto {
    private String uuid;
    private LocalDate fromDate;
    private List<AppointmentGroupDto> groups;

    public AppointmentDto(Appointment appointment) {
        this.uuid = appointment.getUuid();
        this.fromDate = appointment.getFromDate();
        this.groups = appointment.getGroups().stream().map(AppointmentGroupDto::new).collect(Collectors.toList());
    }
}
