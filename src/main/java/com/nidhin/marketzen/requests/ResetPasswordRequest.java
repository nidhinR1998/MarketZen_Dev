package com.nidhin.marketzen.requests;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String otp;
    private String password;
}
