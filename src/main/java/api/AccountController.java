package api;

import api.dto.GetAccountRequestData;
import api.dto.GetAccountResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.AccountService;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountDbService) {
        this.accountService = accountDbService;
    }

    @GetMapping("/account")
    public GetAccountResponseData getAccount(@RequestBody GetAccountRequestData rqData) {
        return accountService.findAccount(rqData);
    }
}
