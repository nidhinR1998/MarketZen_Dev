package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.Wallet;
import com.nidhin.marketzen.models.WalletTransaction;
import com.nidhin.marketzen.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletTransaction createTransation(Wallet wallet, WalletTransaction transactionType, Long transferId, String purpose, Long amount) {
        WalletTransaction saveTransaction = new WalletTransaction();
        saveTransaction.setWallet(wallet);
        saveTransaction.setType(transactionType.getType());
        saveTransaction.setDate(LocalDate.now());
        saveTransaction.setTransferId(transferId);
        saveTransaction.setPurpose(purpose);
        saveTransaction.setAmount(amount);
        walletTransactionRepository.save(saveTransaction);
        return saveTransaction;
    }

    @Override
    public List<WalletTransaction> getTransactionByWallet(Wallet wallet) {
        return walletTransactionRepository.findByWalletId(wallet.getId());
    }
}
