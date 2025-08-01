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
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MentorClass", description = "A data model representing a classroom associated with a mentor and the total number of sessions conducted in that classroom.")
public class MentorClassDTO {

    @NotBlank(message = "Classroom name must not be blank")
    @JsonProperty("classroom_name")
    @Schema(description = "The official name or title of the classroom", example = "CISSP Exam Prep", requiredMode = Schema.RequiredMode.REQUIRED)
    private String classroomName;

    @NotNull(message = "The number of sessions must not be null")
    @Min(value = 0, message = "The number of sessions must be non-negative")
    @JsonProperty("session_count")
    @Schema(description = "Total number of sessions conducted by the mentor in this classroom", example = "32", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sessionCount;
}
