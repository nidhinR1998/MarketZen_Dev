package com.nidhin.marketzen.controller;

import com.nidhin.marketzen.config.Jwtprovider;
import com.nidhin.marketzen.models.TwoFactorOTP;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.repository.UserRepository;
import com.nidhin.marketzen.response.AuthResponse;
import com.nidhin.marketzen.services.CustomeUserDetailsService;
import com.nidhin.marketzen.services.EmailService;
import com.nidhin.marketzen.services.TwoFactorOtpService;
import com.nidhin.marketzen.services.WatchlistService;
import com.nidhin.marketzen.utils.CryptoUtils;
import com.nidhin.marketzen.utils.OtpUtils;
import io.jsonwebtoken.JwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private CryptoUtils cryptoUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {
        logger.info("Registration attempt for user: {}", user.getEmail());

        User isEamilExist = userRepository.findByEmail(user.getEmail());
        if (isEamilExist != null) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new Exception("Email Already Exist");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        User saveUser = userRepository.save(newUser);
        watchlistService.createWatchlist(saveUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = Jwtprovider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register success");

        logger.info("User registered successfully: {}", user.getEmail());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        logger.info("Login attempt for user: {}", user.getEmail());
        logger.info("Login attempt for userPassword: {}", user.getPassword());

        String userName = CryptoUtils.decrypt(user.getEmail());
        String password = CryptoUtils.decrypt(user.getPassword());
        logger.info("EMAIL = {}" +"and" + "Password = {}", userName , password);

        Authentication auth = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = Jwtprovider.generateToken(auth);
        User authUser = userRepository.findByEmail(userName);

        if (user.getTwoFactorAuth().isEnabled()) {
            logger.info("Two-factor authentication enabled for user: {}", userName);
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
             String message ="TWO FACTOR AUTH";
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(
                    authUser,
                    otp,
                    jwt,
                    message
            );
            emailService.sendVarificationOtpEmail(userName, otp);

            res.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login success");

        logger.info("User logged in successfully: {}", userName);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) throws Exception {
        UserDetails userDetails = customeUserDetailsService.loadUserByUsername(userName);
        logger.info("DB Password: {}", CryptoUtils.decrypt(userDetails.getPassword()));

        if (userDetails == null) {
            logger.error("Invalid username attempt: {}", userName);
            throw new BadCredentialsException("Invalid username");
        }
        if (!password.equals(CryptoUtils.decrypt(userDetails.getPassword()))) {
            logger.error("Invalid password for user: {}", userName);
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @GetMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp, @RequestParam String id, @RequestParam String jwt) throws Exception {
        logger.info("OTP verification attempt with id: {}", id);
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

        if (twoFactorOtpService.varifyTwoFactorOtp(twoFactorOTP, otp)) {
            AuthResponse res = new AuthResponse();
            res.setMessage("Authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());

            logger.info("Two Factor authentication verified for id: {}", id);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        logger.error("Invalid OTP for id: {}", id);
        throw new Exception("Invalid OTP");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPasswordSendOtp(@RequestBody User user) throws Exception {
        logger.info("Forgot Password Hit with User: {}", user);
        String email = CryptoUtils.decrypt(user.getEmail());
        logger.info("FORGOT-PASSWORD_EMAIL: {}",email);
        User authUser = userRepository.findByEmail(email);
        if (authUser != null) {
            String otp = OtpUtils.generateOTP();
            String message = "FORGOT PASSWORD OTP SEND SUCCESS";
            String jwt = Jwtprovider.generateForgotPasswordToken(email);
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(
                    authUser,
                    otp,
                    jwt,
                    message
            );
            emailService.sendVarificationOtpEmail(email, otp);
            AuthResponse res = new AuthResponse();
            res.setJwt(jwt);
            res.setMessage("FORGOT PASSWORD OTP SEND SUCCESS");
            res.setTwoFactorAuthEnabled(true);
            res.setSession(newTwoFactorOTP.getId());
            logger.error("Forgot Password OTP send to email: {}", email);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

        } else {
            throw new Exception("User not found");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword(@RequestBody User user, @RequestParam String id, @RequestParam String jwt) throws Exception {
        logger.info("Password change attempt for user: {}", user.getEmail());

        // Check if the provided OTP ID exists
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if (twoFactorOTP == null) {
            logger.error("Invalid User {}", id);
            throw new Exception("Invalid OTP ID");
        }

        // Extract email from JWT
        String email = Jwtprovider.getChangePassowrdEamilFromToken(jwt);
        logger.info("User Email from Token: {}", email);

        // Retrieve the user by email
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            logger.error("User not found for email: {}", email);
            throw new Exception("User not found");
        }

        // Update password and save user
        existingUser.setPassword(user.getPassword());
        userRepository.save(existingUser);

        // Re-authenticate with new password
        Authentication auth = new UsernamePasswordAuthenticationToken(
                existingUser.getEmail(),
                existingUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Generate a new JWT token
        String newJwt = Jwtprovider.generateToken(auth);
        AuthResponse res = new AuthResponse();
        res.setJwt(newJwt);
        res.setStatus(true);
        res.setMessage("Password changed successfully");

        logger.info("Password change successful for user: {}", existingUser.getEmail());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
