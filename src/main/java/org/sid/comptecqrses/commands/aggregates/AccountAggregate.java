package org.sid.comptecqrses.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.sid.comptecqrses.commonapi.commands.CreateAccountCommand;
import org.sid.comptecqrses.commonapi.commands.CreditAccountCommand;
import org.sid.comptecqrses.commonapi.commands.DebitAccountCommand;
import org.sid.comptecqrses.commonapi.enums.AccountStatus;
import org.sid.comptecqrses.commonapi.events.AccountActivatedEvent;
import org.sid.comptecqrses.commonapi.events.AccountCreatedEvent;
import org.sid.comptecqrses.commonapi.events.AccountCreditedEvent;
import org.sid.comptecqrses.commonapi.events.AccountDebitedEvent;
import org.sid.comptecqrses.commonapi.exceptions.AmountNegativeException;
import org.sid.comptecqrses.commonapi.exceptions.BalanceNotSufficientException;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private  String accountId;
    private double balance ;
    private String currency;
    private AccountStatus status;

    protected AccountAggregate(){
        // Required by AXON
    }

    //Fonction de decision
    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand){
        if(createAccountCommand.getInitialBalance()<0) throw new RuntimeException("Impossible ... !");
        // OK
        log.info("CreateAccountCommand Received");
        AggregateLifecycle.apply(new AccountCreatedEvent(
           createAccountCommand.getId(),
           createAccountCommand.getInitialBalance(),
           createAccountCommand.getCurrency(),
                AccountStatus.CREATED
        ));
    }


    //Fonction d'evolution (changer l'etat du mon compte)
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId=event.getId();
        this.balance=event.getInitialBalance();
        this.currency=event.getCurrency();
        this.status=AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }
    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        log.info("AccountActivatedEvent Occured");
        this.status=event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance+=event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        if(this.balance<command.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient Exception =>" +balance);
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance-=event.getAmount();
    }


}
