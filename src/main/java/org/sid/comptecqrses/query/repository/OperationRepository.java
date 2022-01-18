package org.sid.comptecqrses.query.repository;

import org.sid.comptecqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OperationRepository extends JpaRepository<Operation, Long> {
     List<Operation> findByAccountId(String accountId);
}
