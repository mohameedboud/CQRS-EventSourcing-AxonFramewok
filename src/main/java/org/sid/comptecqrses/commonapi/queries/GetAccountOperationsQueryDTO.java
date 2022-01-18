package org.sid.comptecqrses.commonapi.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class GetAccountOperationsQueryDTO {
    private String accountId;
}
