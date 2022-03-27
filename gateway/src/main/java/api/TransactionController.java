package api;

import api.dto.common.Response;
import api.dto.create.transaction.CreateTransactionRequestData;
import api.dto.get.transactions.GetTransactionsRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.TransactionService;

import static java.util.Objects.isNull;

@RestController
public class TransactionController {

    private final ErrorHandler errorHandler;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 ErrorHandler errorHandler) {
        this.transactionService = transactionService;
        this.errorHandler = errorHandler;
    }

    @GetMapping("/transaction")
    public ResponseEntity getTransactions(@RequestBody GetTransactionsRequestData rqData) {
        if (isNull(rqData.getAccountId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).build();
        }
        Response res = transactionService.findTransactions(rqData);
        if (res.hasError()) {
            return errorHandler.handleError(res);
        }
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/transaction/create")
    public ResponseEntity createTransaction(@RequestBody CreateTransactionRequestData rqData) {
        Response res = transactionService.createTransaction(rqData);
        if (res.hasError()) {
            return errorHandler.handleError(res);
        }
        return ResponseEntity.ok().body(res);
    }
}
