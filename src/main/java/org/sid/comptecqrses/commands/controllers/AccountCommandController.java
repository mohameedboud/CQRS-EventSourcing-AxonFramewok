package org.sid.comptecqrses.commands.controllers;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.sid.comptecqrses.commonapi.commands.CreateAccountCommand;
import org.sid.comptecqrses.commonapi.commands.CreditAccountCommand;
import org.sid.comptecqrses.commonapi.commands.DebitAccountCommand;
import org.sid.comptecqrses.commonapi.dtos.CreateAccountRequestDTO;
import org.sid.comptecqrses.commonapi.dtos.CreditAccountRequestDTO;
import org.sid.comptecqrses.commonapi.dtos.DebitAccountRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/account")
@AllArgsConstructor
public class AccountCommandController {

    @Autowired
    private CommandGateway commandGateway ;
    private EventStore eventStore;

   /* public AccountCommandController(){
        this(null);
    }*/

    @PostMapping(path = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO requestDTO){
        CompletableFuture<String> commandResponse = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                requestDTO.getInitialBalance(),
                requestDTO.getCurrency()
        ));
        return commandResponse;
    }

    @PutMapping(path = "/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO requestDTO){
        CompletableFuture<String> commandResponse = commandGateway.send(new CreditAccountCommand(
               requestDTO.getAccountId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
        return commandResponse;
    }

    @PutMapping(path = "/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO requestDTO){
        CompletableFuture<String> commandResponse = commandGateway.send(new DebitAccountCommand(
                requestDTO.getAccountId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
        return commandResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        ResponseEntity<String> entity = new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return entity ;
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }


}
