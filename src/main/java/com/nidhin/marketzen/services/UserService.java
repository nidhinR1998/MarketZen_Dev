package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.VerificationType;
import com.nidhin.marketzen.models.TwoFactorOTP;
import com.nidhin.marketzen.models.User;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long id) throws Exception;

    public  User enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user);

    User updatePassword(User user, String newPassword);
    public boolean isOtpDeleted(User user, String id, String jwt);

}
