package com.mbpt.skillmentor.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Integer mentorId;
    private String mentorName;
    private Double totalFee;
}
