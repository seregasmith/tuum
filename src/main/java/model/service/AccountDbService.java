package model.service;

import service.dto.AccountWithBalances;

import java.util.Optional;

/**
 * Provides methods to manage data connected with {@link model.entity.Account}
 * and it's {@link model.entity.Transaction}
 */
public interface AccountDbService {
    Optional<AccountWithBalances> findAccountWithBalances(Long accountId);
}
