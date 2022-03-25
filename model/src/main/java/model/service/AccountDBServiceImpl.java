package model.service;

import common.Currency;
import common.ErrorCode;
import model.entity.Account;
import model.entity.Balance;
import model.entity.Transaction;
import model.repo.AccountRepository;
import model.repo.BalanceRepository;
import model.repo.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import service.dto.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static common.Direction.IN;
import static common.Direction.OUT;
import static java.util.Objects.isNull;

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

    @Transactional
    @Modifying
    public CreateAccountPayload createAccount(CreateAccountServiceRequest rqData) {
        Account accountBean = new Account();
        accountBean.setCountry(rqData.getCountry());
        accountBean.setCustomerId(rqData.getCustomerId());

        Account account = accountRepository.save(accountBean);
        List<Balance> balances = rqData.getCurrencies()
                .stream()
                .map(c -> createBalanceForAccount(account, c))
                .collect(Collectors.toList());
        return new CreateAccountPayload(
                new CreateAccountPayloadData(
                        new AccountWithBalances(account, balances)
                )
        );
    }

    public CreateTransactionPayload processTransaction(CreateTransactionServiceRequest rqData) {
        Long accountId = rqData.getAccountId();
        if (isNull(accountId) || isNull(rqData.getCurrency())) {
            CreateTransactionPayload.asError(ErrorCode.INCORRECT_PARAMS, "Invalid params");
        }
        Optional<AccountWithBalances> accountOpt = findAccountWithBalances(accountId);
        if (accountOpt.isEmpty()) {
            CreateTransactionPayload.asError(ErrorCode.NOT_FOUND, "Account not found by id");
        }
        Account account = accountOpt
                .map(AccountWithBalances::getAccount)
                .orElse(null);
        Optional<Balance> balance = accountOpt
                .map(AccountWithBalances::getBalances)
                .orElse(Collections.emptyList())
                .stream()
                .filter(b -> b.getCurrency().equals(rqData.getCurrency()))
                .findFirst();

        return balance
                .filter(b -> isAmountEnoughToProcessTransaction(b, rqData))
                .map(b -> processTransaction(b, rqData, account))
                .map(CreateTransactionPayload::new)
                .orElseGet(() -> CreateTransactionPayload.asError(ErrorCode.DB_ERROR, "Not created in DB"));

    }

    private Boolean isAmountEnoughToProcessTransaction(Balance balance, CreateTransactionServiceRequest rqData) {
        if (OUT.equals(rqData.getDirection())) {
            return balance.getAmount().compareTo(rqData.getAmount()) >= 0;
        }
        return true;
    }

    @Transactional
    @Modifying
    private CreateTransactionPayloadData processTransaction(Balance balance, CreateTransactionServiceRequest rqData, Account account) {
        if (OUT.equals(rqData.getDirection())) {
            balance.setAmount(balance.getAmount().subtract(rqData.getAmount()));
        } else if (IN.equals(rqData.getDirection())) {
            balance.setAmount(balance.getAmount().add(rqData.getAmount()));
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(rqData.getAmount());
        transaction.setCurrency(rqData.getCurrency());
        transaction.setDirection(rqData.getDirection());
        transaction.setDescription(rqData.getDescription());

        Balance balanceAfterTr = balanceRepository.save(balance);
        Transaction tr = transactionRepository.save(transaction);

        return new CreateTransactionPayloadData(tr, balanceAfterTr);
    }


    private Balance createBalanceForAccount(Account account, Currency currency) {
        Balance balanceBean = new Balance();
        balanceBean.setAccount(account);
        balanceBean.setCurrency(currency);
        balanceBean.setAmount(new BigDecimal(0));

        return balanceRepository.save(balanceBean);
    }
}
