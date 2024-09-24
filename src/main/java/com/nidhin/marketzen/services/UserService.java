package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.VerificationType;
import com.nidhin.marketzen.models.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long id) throws Exception;

    public  User enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user);

    User updatePassword(User user, String newPassword);

}
