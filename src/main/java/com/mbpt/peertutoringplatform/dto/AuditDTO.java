package com.mbpt.peertutoringplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Audit", description = "A data model representing an audit in the platform")
public class AuditDTO {

    private Integer sessionId;

    @NotNull(message = "Student ID must not be null")
    @JsonProperty("student_id")
    @Schema(description = "ID of the student", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer studentId;

    @NotBlank(message = "Student first name must not be blank")
    @JsonProperty("student_first_name")
    @Schema(description = "First name of the student", example = "Daniel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String studentFirstName;

    @NotBlank(message = "Student last name must not be blank")
    @JsonProperty("student_last_name")
    @Schema(description = "Last name of the student", example = "Andrew", requiredMode = Schema.RequiredMode.REQUIRED)
    private String studentLastName;

    @NotBlank(message = "Student email must not be blank")
    @Email(message = "Email must be valid")
    @JsonProperty("student_email")
    @Schema(description = "Email of the student", example = "daniel@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String studentEmail;

    @Pattern(regexp = "^\\+[1-9]\\d{6,14}$", message = "Phone number must be valid E.164 format. Must start with '+' followed by 7-15 digits only.")
    @NotBlank(message = "Student phone number must not be blank")
    @JsonProperty("student_phone_number")
    @Schema(description = "Phone number of the student", example = "+15559876543", requiredMode = Schema.RequiredMode.REQUIRED)
    private String studentPhoneNumber;

    @NotBlank(message = "Class title must not be blank")
    @JsonProperty("class_title")
    @Schema(description = "The official name of the classroom, indicating the core subject matters or skills covered.", example = "CISSP Exam Prep", requiredMode = Schema.RequiredMode.REQUIRED)
    private String classTitle;

    @NotNull(message = "Mentor ID must not be null")
    @JsonProperty("mentor_id")
    @Schema(description = "ID of the mentor", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mentorId;

    @NotBlank(message = "Mentor first name must not be blank")
    @JsonProperty("mentor_first_name")
    @Schema(description = "First name of the mentor", example = "Lucas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mentorFirstName;

    @NotBlank(message = "Mentor last name must not be blank")
    @JsonProperty("mentor_last_name")
    @Schema(description = "Last name of the mentor", example = "Joseph", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mentorLastName;

    @NotBlank(message = "Mentor phone number must not be blank")
    @JsonProperty("mentor_phone_number")
    @Schema(description = "Phone number of the mentor", example = "+15559876543", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mentorPhoneNumber;

    @NotNull(message = "Fee must not be null")
    @Min(value = 0, message = "Session fee must be non-negative")
    @JsonProperty("fee")
    @Schema(description = "The fee charged by the mentor for a single session.", example = "2500", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double fee;

    @NotNull(message = "Start time must not be null")
    @JsonProperty("start_time")
    @Schema(description = "Start time of the session in ISO-8601 format", example = "2025-08-01T09:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant startTime;

    @NotNull(message = "End time must not be null")
    @JsonProperty("end_time")
    @Schema(description = "End time of the session in ISO-8601 format", example = "2025-08-01T11:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant endTime;

    @NotBlank(message = "Topic must not be blank")
    @JsonProperty("topic")
    @Schema(description = "The specific topic covered during the session", example = "Introduction to Cloud Computing", requiredMode = Schema.RequiredMode.REQUIRED)
    private String topic;
}
