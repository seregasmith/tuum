package api;

import api.dto.GetAccountRequestData;
import api.dto.GetAccountResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.AccountService;

import java.util.Optional;

import static java.util.Objects.isNull;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountDbService) {
        this.accountService = accountDbService;
    }

    @GetMapping("/account")
    public ResponseEntity getAccount(@RequestBody GetAccountRequestData rqData) {
        if (isNull(rqData.getAccountId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<GetAccountResponseData> foundAccountOpt = accountService.findAccount(rqData);
        if (foundAccountOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(foundAccountOpt.get());
    }
}
