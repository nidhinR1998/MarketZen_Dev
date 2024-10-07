package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.WithdrawalStatus;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Withdrawal;
import com.nidhin.marketzen.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService{
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requetWithdrawal(Long amount, User user) {
        Withdrawal withdrawal=new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accepted) throws Exception {
        Optional<Withdrawal> withdrawal=withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isEmpty()){
            throw new Exception("Withdrawal not found");
        }
        Withdrawal withdrawal1=withdrawal.get();

        withdrawal1.setDate(LocalDateTime.now());
        if (accepted) {
            withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
        } else {
            withdrawal1.setStatus(WithdrawalStatus.PENDING);
        }
        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
