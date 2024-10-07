package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Withdrawal;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requetWithdrawal(Long amount, User user);

    Withdrawal procedWithdrawal(Long withdrawalId, boolean accepted) throws Exception;

    List<Withdrawal> getUserWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();
}
