package com.mbpt.peertutoringplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Payment", description = "A data model representing a payment in the platform")
public class PaymentDTO {
    @JsonProperty("mentor_id")
    @Schema(description = "ID of the mentor", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mentorId;

    @NotBlank(message = "Mentor name must not be blank")
    @JsonProperty("mentor_name")
    @Schema(description = "Name of the mentor", example = "Daniel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mentorName;

    @NotNull(message = "Total fee must not be null")
    @Min(value = 0, message = "Total fee must be non-negative")
    @JsonProperty("total_fee")
    @Schema(description = "Total payment made to the mentor for conducting the session.", example = "12000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double totalFee;
}
