package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.VerificationType;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
