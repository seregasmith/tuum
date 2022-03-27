package api;

import api.dto.common.Response;
import api.dto.create.account.CreateAccountRequestData;
import api.dto.get.account.GetAccountRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.AccountService;

import static java.util.Objects.isNull;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final ErrorHandler errorHandler;

    @Autowired
    public AccountController(AccountService accountDbService,
                             ErrorHandler errorHandler) {
        this.accountService = accountDbService;
        this.errorHandler = errorHandler;
    }

    @GetMapping("/account")
    public ResponseEntity getAccount(@RequestBody GetAccountRequestData rqData) {
        if (isNull(rqData.getAccountId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).build();
        }
        Response res = accountService.findAccount(rqData);
        if (res.hasError()) {
            return errorHandler.handleError(res);
        }
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/create/account")
    public ResponseEntity createAccount(@RequestBody CreateAccountRequestData rqData) {
        Response res = accountService.createAccount(rqData);
        if (res.hasError()) {
            return errorHandler.handleError(res);
        }
        return ResponseEntity.ok().body(res);
    }
}
