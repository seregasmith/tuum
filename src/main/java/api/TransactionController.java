package api;

import api.dto.GetTransactionsRequestData;
import api.dto.GetTransactionsResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.TransactionService;

import static java.util.Objects.isNull;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction")
    public ResponseEntity getTransactions(@RequestBody GetTransactionsRequestData rqData) {
        if (isNull(rqData.getAccountId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).build();
        }
        GetTransactionsResponseData foundTransactions = transactionService.findTransactions(rqData);
        return ResponseEntity.ok().body(foundTransactions);
    }
}
