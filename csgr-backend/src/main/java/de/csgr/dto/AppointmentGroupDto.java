package de.csgr.dto;

import de.csgr.entity.AppointmentGroup;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentGroupDto {
    private String uuid;
    private List<String> employees;

    public AppointmentGroupDto(AppointmentGroup appointmentGroup) {
        this.uuid = appointmentGroup.getUuid();
        this.employees = appointmentGroup.getEmployees();
    }
}

