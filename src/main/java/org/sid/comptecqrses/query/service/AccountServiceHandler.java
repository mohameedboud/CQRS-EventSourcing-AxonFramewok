package org.sid.comptecqrses.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.sid.comptecqrses.commonapi.dtos.AccountOperationResponseDTO;
import org.sid.comptecqrses.commonapi.enums.OperationType;
import org.sid.comptecqrses.commonapi.events.AccountActivatedEvent;
import org.sid.comptecqrses.commonapi.events.AccountCreatedEvent;
import org.sid.comptecqrses.commonapi.events.AccountCreditedEvent;
import org.sid.comptecqrses.commonapi.events.AccountDebitedEvent;
import org.sid.comptecqrses.commonapi.queries.*;
import org.sid.comptecqrses.query.entities.Account;
import org.sid.comptecqrses.query.entities.Operation;
import org.sid.comptecqrses.query.mappers.BankAccountMapper;
import org.sid.comptecqrses.query.repository.AccountRepository;
import org.sid.comptecqrses.query.repository.OperationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final BankAccountMapper bankAccountMapper;
    private final QueryUpdateEmitter queryUpdateEmitter;



    @EventHandler
    @Transactional
    public void on(AccountCreatedEvent event){
        log.info("******************");
        log.info("AccountCreatedEvent recieved");
        Account account=new Account();
        account.setId(event.getId());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());
        account.setBalance(event.getInitialBalance());
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountActivatedEvent event){
        log.info("******************");
        log.info("AccountActivatedEvent recieved");
        Account account=accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountDebitedEvent event){
        log.info("******************");
        log.info("AccountDebitedEvent recieved");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // a ne pas faire ilfaut choisir la date de l'operation
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit(m->(
                        (GetAccountQueryDTO)m.getPayload()).getId().equals(event.getId()),
                bankAccountMapper.bankAccountToBankAccountDTO(account)
        );
    }

    @EventHandler
    @Transactional
    public void on(AccountCreditedEvent event){
        log.info("******************");
        log.info("AccountCreditedEvent recieved");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // a ne pas faire ilfaut choisir la date de l'operation
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit(m->(
                        (GetAccountQueryDTO)m.getPayload()).getId().equals(event.getId()),
                bankAccountMapper.bankAccountToBankAccountDTO(account)
        );
    }

    @QueryHandler
    public List<BankAccountResponseDTO> on(GetAllAccountQuery getAllAccountQuery){
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream()
                .map((acc->bankAccountMapper.bankAccountToBankAccountDTO(acc))).collect(Collectors.toList());
    }

    @QueryHandler
    public BankAccountResponseDTO on(GetAccountByIdQuery getAllAccountQuery){
        Account account = accountRepository.findById(getAllAccountQuery.getId()).get();
        return bankAccountMapper.bankAccountToBankAccountDTO(account);
    }

   @QueryHandler
    public List<AccountOperationResponseDTO> on(GetAccountOperationsQueryDTO getAccountOperationsQueryDTO) {
        List<Operation> accountOperations = operationRepository.
                findByAccountId(getAccountOperationsQueryDTO.getAccountId());
        return accountOperations.stream().map(op->bankAccountMapper.accountOperationToAccountOperationDTO(op)).collect(Collectors.toList());
    }

}
