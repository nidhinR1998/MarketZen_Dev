package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.TwoFactorOTP;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.repository.TwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {
    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt, String message) {
        UUID uuid = UUID.randomUUID();
        String id=uuid.toString();

        TwoFactorOTP twoFactorOtp = new TwoFactorOTP();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(id);
        twoFactorOtp.setMessage(message);
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    @Override
    public TwoFactorOTP findByUser(long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> otp=twoFactorOtpRepository.findById(id);
        return otp.orElse(null);
    }

    @Override
    public boolean varifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {

        twoFactorOtpRepository.delete(twoFactorOTP);

    }
}
