package com.nidhin.marketzen.controller;

import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Wallet;
import com.nidhin.marketzen.models.WalletTransaction;
import com.nidhin.marketzen.models.Withdrawal;
import com.nidhin.marketzen.services.UserService;
import com.nidhin.marketzen.services.WalletService;
import com.nidhin.marketzen.services.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Wallet userWallet=walletService.getUserWallet(user);

        Withdrawal withdrawal=withdrawalService.requetWithdrawal(amount,user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());

       // WalletTransaction walletTransaction=walletTransationService.createTransaction();

        return  new ResponseEntity<>(withdrawal, HttpStatus.OK);


    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal=withdrawalService.procedWithdrawal(id,accept);

        Wallet userWallet=walletService.getUserWallet(user);
        if (!accept){
            walletService.addBalance(userWallet,withdrawal.getAmount());
        }

        return new ResponseEntity<>(userWallet,HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
            @RequestHeader("Authorization")String jwt
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawal=withdrawalService.getUserWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization")String jwt
    ) throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        List<Withdrawal>withdrawal=withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
}
