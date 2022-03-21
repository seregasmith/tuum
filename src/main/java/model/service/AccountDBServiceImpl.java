package model.service;

import model.entity.Account;
import model.entity.Balance;
import model.entity.Transaction;
import model.repo.AccountRepository;
import model.repo.BalanceRepository;
import model.repo.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.AccountWithBalances;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDBServiceImpl implements AccountDbService {
    private final Logger LOG = LoggerFactory.getLogger(AccountDBServiceImpl.class);
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountDBServiceImpl(AccountRepository accountRepository,
                                BalanceRepository balanceRepository,
                                TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<AccountWithBalances> findAccountWithBalances(Long accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            LOG.error("No account found with id {}", accountId);
            return Optional.empty();
        }
        List<Balance> balances = balanceRepository.findByAccountId(accountId);
        return Optional.of(new AccountWithBalances(accountOpt.get(), balances));
    }

    @Override
    public List<Transaction> findTransactionsOfAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

}
