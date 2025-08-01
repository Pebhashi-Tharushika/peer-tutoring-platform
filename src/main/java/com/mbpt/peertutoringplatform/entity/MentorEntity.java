package com.mbpt.peertutoringplatform.entity;

import com.mbpt.peertutoringplatform.common.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Integer mentorId;

    @NotBlank(message = "Clerk Mentor ID must not be blank")
    @Column(name = "clerk_mentor_id", nullable = false, unique = true)
    private String clerkMentorId;

    @NotBlank(message = "First name must not be blank")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Address must not be blank")
    @Column(name = "address", nullable = false)
    private String address;

    @Pattern(regexp = "^\\+[1-9]\\d{6,14}$", message = "Phone number must be valid E.164 format.")
    @NotBlank(message = "Phone number must not be blank")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Title must not be blank")
    @Column(name = "title", nullable = false)
    @Enumerated(EnumType.STRING)
    private Constants.Title title;

    @NotNull(message = "Session fee must not be null")
    @Min(value = 0, message = "Session fee must be non-negative")
    @Column(name = "session_fee", nullable = false)
    private Double sessionFee;

    @NotBlank(message = "Profession must not be blank")
    @Column(name = "profession", nullable = false)
    private String profession;

    @NotBlank(message = "Subject must not be blank")
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotBlank(message = "Qualification must not be blank")
    @Column(name = "qualification", nullable = false)
    private String qualification;

    @NotBlank(message = "Mentor image URL must not be blank")
    @Column(name = "mentor_image", nullable = false)
    private String mentorImage;

    @NotNull(message="Certification status must not be null")
    @Column(name="is_certified", nullable = false)
    private Boolean isCertified;

    @OneToMany(mappedBy = "mentorEntity", fetch = FetchType.EAGER)
    private List<SessionEntity> sessionEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "mentorEntity", fetch = FetchType.EAGER)
    private List<ClassRoomEntity> classRoomEntityList = new ArrayList<>();
}
