package org.sid.comptecqrses.commonapi.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.comptecqrses.commonapi.enums.AccountStatus;

@Data @AllArgsConstructor @NoArgsConstructor
public class BankAccountResponseDTO {
    private String id;
    private double balance;
    private AccountStatus status;
}
