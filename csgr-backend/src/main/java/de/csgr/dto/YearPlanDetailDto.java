package de.csgr.dto;

import de.csgr.entity.Rotation;
import de.csgr.entity.YearPlan;
import de.csgr.util.EncounterMatrixUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class YearPlanDetailDto {
    private String uuid;
    private int year;
    private Rotation rotation;
    private List<String> employees;
    private List<AppointmentDto> appointments;
    private final Map<String, Map<String, Integer>> encounterMatrix;

    public YearPlanDetailDto(YearPlan yearPlan) {
        this.uuid = yearPlan.getUuid();
        this.year = yearPlan.getYear();
        this.rotation = yearPlan.getRotation();
        this.employees = yearPlan.getEmployees();
        this.appointments = yearPlan.getAppointments().stream().map(AppointmentDto::new).collect(Collectors.toList());
        this.encounterMatrix = EncounterMatrixUtil.createEncounterMatrix(yearPlan);
    }
}
