package org.sid.comptecqrses.query.mappers;

import org.mapstruct.Mapper;
import org.sid.comptecqrses.commonapi.dtos.AccountOperationResponseDTO;
import org.sid.comptecqrses.commonapi.queries.BankAccountResponseDTO;
import org.sid.comptecqrses.query.entities.Account;
import org.sid.comptecqrses.query.entities.Operation;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountResponseDTO bankAccountToBankAccountDTO(Account bankAccount);
    AccountOperationResponseDTO accountOperationToAccountOperationDTO(Operation accountOperation);
}
