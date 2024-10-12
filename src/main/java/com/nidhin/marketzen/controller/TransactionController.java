package com.nidhin.marketzen.controller;

import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Wallet;
import com.nidhin.marketzen.models.WalletTransaction;
import com.nidhin.marketzen.services.TransactionService;
import com.nidhin.marketzen.services.UserService;
import com.nidhin.marketzen.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/transaction")
    public ResponseEntity<List<WalletTransaction>> getUserWaller(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet =walletService.getUserWallet(user);

        List<WalletTransaction> transactions =transactionService.getTransactionByWallet(wallet);

        return new ResponseEntity<>(transactions, HttpStatus.ACCEPTED);
    }

}
