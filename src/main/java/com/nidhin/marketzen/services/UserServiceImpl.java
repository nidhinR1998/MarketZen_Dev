package com.nidhin.marketzen.services;

import com.nidhin.marketzen.config.Jwtprovider;
import com.nidhin.marketzen.domain.VerificationType;
import com.nidhin.marketzen.models.TwoFactorAuth;
import com.nidhin.marketzen.models.TwoFactorOTP;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;
    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = Jwtprovider.getEamilFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("User not Found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("User not Found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new Exception("User not found");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);

        return userRepository.save(user);
    }

    @Override
    public boolean isOtpDeleted(User user, String id, String jwt) {
        if (id != null && jwt != null) {
            String email = Jwtprovider.getChangePassowrdEamilFromToken(jwt); // Corrected typo here
            User existingUser = userRepository.findByEmail(email);
            TwoFactorOTP userOTP = twoFactorOtpService.findById(id);

            if (existingUser != null && userOTP != null) {
                // Delete the OTP if found
                twoFactorOtpService.deleteTwoFactorOtp(userOTP);
               // logger.info("OTP Deleted for user: {}", existingUser.getEmail());
                return true;
            }
        }
       // logger.error("Failed to delete OTP for user.");
        return false;
    }



}
