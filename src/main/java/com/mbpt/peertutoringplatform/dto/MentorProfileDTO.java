package com.mbpt.peertutoringplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mbpt.peertutoringplatform.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "MentorProfile",
        description = "A data model representing the complete profile of a mentor, including their personal details and details of the classrooms they conduct.")
public class MentorProfileDTO {

    @NotNull(message = "mentor must not be null")
    @JsonProperty("mentor")
    @Schema(description = "Detailed information about the mentor who conducts the classrooms.", implementation = MentorDTO.class, requiredMode = Schema.RequiredMode.REQUIRED)
    private MentorDTO mentorDTO;


    @NotNull(message = "The list of classrooms conducted by a mentor must not be null")
    @NotEmpty(message = "The mentor must conduct at least one classroom.")
    @JsonProperty("mentor_classes")
    @Schema(description = "The classrooms and their total number of sessions conducted by a mentor. ", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MentorClassDTO> mentorClassDTOList;
}
