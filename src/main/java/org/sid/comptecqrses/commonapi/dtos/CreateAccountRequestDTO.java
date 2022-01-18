package org.sid.comptecqrses.commonapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

 @NoArgsConstructor @AllArgsConstructor
public class CreateAccountRequestDTO {
    @Getter private double initialBalance;
    @Getter private String currency;
}
