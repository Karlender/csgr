package de.csgr.controller;

import de.csgr.dto.YearPlanCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupchat")
public class GroupchatController {

    @PostMapping("/year-plan")
    @Operation(
            summary = "Retrieve a year plan",
            description = "Fetches the year plan details based on the given criteria",
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
    public String createGroupchatYearPlan(@RequestBody YearPlanCreateDto yearPlan) {
        return "0";
    }
}
