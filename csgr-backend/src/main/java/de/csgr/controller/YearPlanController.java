package de.csgr.controller;

import de.csgr.dto.YearPlanCreateDto;
import de.csgr.dto.YearPlanDetailDto;
import de.csgr.dto.YearPlanDto;
import de.csgr.entity.YearPlan;
import de.csgr.service.YearPlanService;
import de.csgr.util.YearPlanExcelUtil;
import de.csgr.util.YearPlanIcsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groupchat/year-plan")
public class YearPlanController {

    private final YearPlanService yearPlanService;

    @PostMapping
    @Operation(
            summary = "Create a year plan",
            description = "Creates a groupchat roulette year plan based on the given criteria",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully created year plan, returns UUID of created year plan.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Given parameters are not valid"
                    )
            }
    )
    public ResponseEntity<Map<String, String>> createGroupchatYearPlan(@Valid @RequestBody YearPlanCreateDto yearPlanCreateDto) {
        String generatedUuid = yearPlanService.createGroupChatYearPlan(yearPlanCreateDto);
        Map<String, String> response = new HashMap<>();
        response.put("uuid", generatedUuid);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<YearPlanDto>> listYearPlans() {
        return ResponseEntity.ok(yearPlanService.listYearPlans().stream().map(YearPlanDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Retrieve a year plan",
            description = "Retrieve a year plan with detailed information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved year plan, returns year plan with details.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Year plan with given UUID was not found"
                    )
            }
    )
    public ResponseEntity<YearPlanDetailDto> getYearPlan(@PathVariable String uuid) {
        Optional<YearPlan> optional = yearPlanService.getYearPlan(uuid);
        return optional.map(yearPlan -> ResponseEntity.ok(new YearPlanDetailDto(yearPlan))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{uuid}/xslx")
    @Operation(
            summary = "Download excel overview of year plan",
            description = "Download excel overview of year plan, contains encounter matrix",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved year plan and created excel file, returns excel file for download.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Year plan with given UUID was not found"
                    )
            }
    )
    public ResponseEntity<byte[]> exportYearPlan(@PathVariable String uuid) throws IOException {
        Optional<YearPlan> optional = yearPlanService.getYearPlan(uuid);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        YearPlan yearPlan = optional.get();
        byte[] excelFile = YearPlanExcelUtil.generateYearPlanExcel(yearPlan);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Jahresplan_" + yearPlan.getYear() + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }

    @GetMapping("/{uuid}/ics")
    @Operation(
            summary = "Download iCalendar file with year plan events",
            description = "Download iCalendar file with year plan events for import in different calendars",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved year plan and created iCal file, returns .ics file for download.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Year plan with given UUID was not found"
                    )
            }
    )
    public ResponseEntity<byte[]> downloadYearPlanICS(@PathVariable String uuid) throws Exception {
        Optional<YearPlan> optional = yearPlanService.getYearPlan(uuid);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        YearPlan yearPlan = optional.get();
        byte[] icsFile = YearPlanIcsUtil.generateYearPlanICS(yearPlan);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Jahresplan_" + yearPlan.getYear() + ".ics")
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(icsFile);
    }
}
