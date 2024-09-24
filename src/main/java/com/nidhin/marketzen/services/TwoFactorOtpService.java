package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.TwoFactorOTP;
import com.nidhin.marketzen.models.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUser(long userId);

    TwoFactorOTP findById(String id);

    boolean varifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
