package model.service;

import common.Currency;
import common.Direction;
import model.entity.Account;
import model.entity.Balance;
import model.repo.AccountRepository;
import model.repo.BalanceRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import service.dto.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {TestConfig.class},
        loader = SpringBootContextLoader.class)
@Transactional
public class AccountDBServiceTest {
    public static final String SOME_DESC = "Some desc";
    @Autowired
    private AccountDbService sut;
    @Resource
    private AccountRepository accountRepository;
    @Resource
    private BalanceRepository balanceRepository;

    @Test
    public void findAccountWithBalancesTest() {
        Account account = createAccount().getData().getAccountWithBalances().getAccount();

        Optional<AccountWithBalances> accountWithBalances = sut.findAccountWithBalances(account.getId());
        Account accountFromDb = accountWithBalances.get().getAccount();
        assertEquals(account.getId(), accountFromDb.getId());
        assertEquals(account.getCustomerId(), accountFromDb.getCustomerId());
    }

    @Test
    public void createTransactionTest() {
        AccountWithBalances accountWithBalances = createAccount().getData().getAccountWithBalances();

        BigDecimal amount = new BigDecimal(1000);
        Direction direction = Direction.IN;
        CreateTransactionServiceRequest rqData = getCreateTransactionRqData(
                accountWithBalances, amount, direction
        );
        BigDecimal amountBefore = accountWithBalances
                .getBalances()
                .stream()
                .filter(b -> b.getCurrency().equals(rqData.getCurrency()))
                .findFirst()
                .map(Balance::getAmount)
                .get();

        CreateTransactionPayload payload = sut.processTransaction(rqData);

        CreateTransactionPayloadData payloadData = payload.getData();
        assertEquals(amountBefore.add(amount), payloadData.getBalanceAfterTransaction().getAmount());
        assertEquals(amount, payloadData.getTransaction().getAmount());
        assertEquals(rqData.getCurrency(), payloadData.getTransaction().getCurrency());
        assertEquals(rqData.getAccountId(), payloadData.getTransaction().getAccount().getId());
        assertEquals(direction, payloadData.getTransaction().getDirection());
        assertEquals(SOME_DESC, payloadData.getTransaction().getDescription());
        assertNotNull(payloadData.getTransaction().getId());
    }

    @NotNull
    private CreateAccountPayload createAccount() {
        List<Currency> currencies = asList(Currency.EUR, Currency.GBP);

        Long customerId = ThreadLocalRandom.current().nextLong();
        String country = "UK";
        CreateAccountPayload createAccountPayload = sut.createAccount(
                new CreateAccountServiceRequest(customerId, country, currencies)
        );
        //region ASSERTIONS
        AccountWithBalances accountWithBalances = createAccountPayload.getData().getAccountWithBalances();
        assertNotNull(accountWithBalances.getAccount().getId());
        assertEquals(customerId, accountWithBalances.getAccount().getCustomerId());
        assertEquals(country, accountWithBalances.getAccount().getCountry());
        for (Currency currency : currencies) {
            assertTrue(accountWithBalances
                    .getBalances()
                    .stream()
                    .anyMatch(b -> b.getCurrency().equals(currency))
            );
        }
        assertEquals(country, accountWithBalances.getAccount().getCountry());
        //endregion
        return createAccountPayload;
    }

    private CreateTransactionServiceRequest getCreateTransactionRqData(
            AccountWithBalances accountWithBalances, BigDecimal amount, Direction direction) {
        CreateTransactionServiceRequest rqData = new CreateTransactionServiceRequest();
        rqData.setAccountId(accountWithBalances.getAccount().getId());
        rqData.setAmount(amount);
        rqData.setCurrency(accountWithBalances.getBalances().get(0).getCurrency());
        rqData.setDirection(direction);
        rqData.setDescription(SOME_DESC);

        return rqData;
    }
}