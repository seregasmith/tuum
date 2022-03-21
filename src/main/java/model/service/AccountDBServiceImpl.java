package model.service;

import model.entity.Account;
import model.entity.Balance;
import model.repo.AccountRepository;
import model.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.AccountWithBalances;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDBServiceImpl implements AccountDbService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public AccountDBServiceImpl(AccountRepository accountRepository,
                                BalanceRepository balanceRepository) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Optional<AccountWithBalances> findAccountWithBalances(Long accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            //todo log it
            return Optional.empty();
        }
        List<Balance> balances = balanceRepository.findByAccountId(accountId);
        return Optional.of(new AccountWithBalances(accountOpt.get(), balances));
    }
}
