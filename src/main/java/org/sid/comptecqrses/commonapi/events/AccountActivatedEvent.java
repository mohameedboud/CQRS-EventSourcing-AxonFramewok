package org.sid.comptecqrses.commonapi.events;

import lombok.Getter;
import org.sid.comptecqrses.commonapi.enums.AccountStatus;

public class AccountActivatedEvent extends BaseEvent<String>{
    @Getter private AccountStatus status;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status ;
    }
}
