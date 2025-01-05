package de.csgr.dto;

import de.csgr.entity.Rotation;
import de.csgr.entity.YearPlan;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class YearPlanDto {
    private String uuid;
    private int year;
    private Rotation rotation;
    private List<String> employees;
    private List<AppointmentDto> appointments;

    public YearPlanDto(YearPlan yearPlan) {
        this.uuid = yearPlan.getUuid();
        this.year = yearPlan.getYear();
        this.rotation = yearPlan.getRotation();
        this.employees = yearPlan.getEmployees();
        this.appointments = yearPlan.getAppointments().stream().map(AppointmentDto::new).collect(Collectors.toList());
    }
}
