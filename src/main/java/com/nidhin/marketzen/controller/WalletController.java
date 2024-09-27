package com.nidhin.marketzen.controller;

import com.nidhin.marketzen.models.Order;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Wallet;
import com.nidhin.marketzen.models.WalletTransaction;
import com.nidhin.marketzen.services.UserService;
import com.nidhin.marketzen.services.WalletService;
import com.nidhin.marketzen.services.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet= walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req) throws Exception{
        User senderUser=userService.findUserProfileByJwt(jwt);
        Wallet receverWallet=walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser,receverWallet,req.getAmount());

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet=walletService.payOrderPayment(order,user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
