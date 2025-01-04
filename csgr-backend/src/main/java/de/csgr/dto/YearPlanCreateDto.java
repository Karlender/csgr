package de.csgr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class YearPlanCreateDto {

    @Schema(description = "List of employee names to be included in the year plan", example = "[\"Max Mustermann\", \"Erika Mustermann\"]")
    private List<String> employees;
    private Long groupCount;
    private Rotation rotation;
    private Long year;

}
