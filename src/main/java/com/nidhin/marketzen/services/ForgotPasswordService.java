package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.VerificationType;
import com.nidhin.marketzen.models.ForgotPasswordToken;
import com.nidhin.marketzen.models.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);



}
