package com.nidhin.marketzen.models;

import com.nidhin.marketzen.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
