package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.Order;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Wallet;

public interface WalletService {
    Wallet getUserWallet (User user);

    Wallet addBalance(Wallet wallet,Long money);

    Wallet findWalletById(Long Id) throws Exception;

    Wallet walletToWalletTransfer(User user,Wallet receiverWallet,Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;
}
