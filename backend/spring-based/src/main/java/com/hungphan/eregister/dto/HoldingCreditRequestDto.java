package com.hungphan.eregister.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoldingCreditRequestDto {

    private String requestId;
    private Long creditAmount;
    private String username;
    private String description;

}
