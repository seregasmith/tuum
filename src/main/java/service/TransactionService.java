package service;

import api.dto.GetTransactionsRequestData;
import api.dto.GetTransactionsResponseData;
import model.service.AccountDbService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final AccountDbService accountDbService;

    @Autowired
    public TransactionService(AccountDbService accountDbService) {
        this.accountDbService = accountDbService;
    }


    public GetTransactionsResponseData findTransactions(@NotNull GetTransactionsRequestData rqData) {
        List<model.entity.Transaction> transactionsOfAccount = accountDbService.findTransactionsOfAccount(rqData.getAccountId());

        return new GetTransactionsResponseData(
                transactionsOfAccount
                        .stream()
                        .map(tr -> api.dto.Transaction.builder()
                                .withTransactionId(tr.getId())
                                .withAccountId(tr.getAccountId())
                                .withAmount(tr.getAmount())
                                .withCurrency(tr.getCurrency())
                                .withDescription(tr.getDescription())
                                .withDirection(tr.getDirection())
                                .build()
                        ).collect(Collectors.toList())
        );
    }
}
