package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.Wallet;
import com.nidhin.marketzen.models.WalletTransaction;

import java.util.List;

public interface TransactionService {

    WalletTransaction createTransation(Wallet wallet,
                                       WalletTransaction transactionType,
                                       Long transferId,
                                       String purpose,
                                       Long amount);

    List<WalletTransaction> getTransactionByWallet(Wallet wallet);
}
