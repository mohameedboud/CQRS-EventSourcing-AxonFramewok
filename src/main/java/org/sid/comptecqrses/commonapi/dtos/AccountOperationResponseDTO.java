package org.sid.comptecqrses.commonapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.comptecqrses.commonapi.enums.OperationType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationResponseDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
}
