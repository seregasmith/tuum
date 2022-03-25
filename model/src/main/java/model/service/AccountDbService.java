package model.service;

import model.entity.Transaction;
import service.dto.*;

import java.util.List;
import java.util.Optional;

/**
 * Provides methods to manage data connected with {@link model.entity.Account}
 * and it's {@link Transaction}
 */
public interface AccountDbService {
    Optional<AccountWithBalances> findAccountWithBalances(Long accountId);

    List<Transaction> findTransactionsOfAccount(Long accountId);

    CreateAccountPayload createAccount(CreateAccountServiceRequest rqData);

    CreateTransactionPayload processTransaction(CreateTransactionServiceRequest rqData);
}
