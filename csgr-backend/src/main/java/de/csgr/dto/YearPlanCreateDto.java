package de.csgr.dto;

import de.csgr.entity.Rotation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class YearPlanCreateDto {

    @Schema(description = "List of employee names to be included in the year plan", example = "[\"Max Mustermann\", \"Erika Mustermann\"]")
    @NotNull(message = "Employees list cannot be null")
    @Size(min = 4, message = "Employees list must contain at least 4 employees")
    private List<String> employees;

    @NotNull(message = "Group count cannot be null")
    @Min(value = 2, message = "Group count must be at least 2")
    private Integer groupCount;

    @NotNull(message = "Rotation cannot be null")
    private Rotation rotation;

    @NotNull(message = "Year cannot be null")
    private Integer year;

}
