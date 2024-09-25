package com.nidhin.marketzen.requests;

import com.nidhin.marketzen.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {

    private String sendTo;
    private VerificationType verificationType;

}
