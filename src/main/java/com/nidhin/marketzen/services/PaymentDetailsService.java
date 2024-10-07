package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.PaymentDetails;
import com.nidhin.marketzen.models.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user);
    public PaymentDetails getUSerPaymentDetails(User user);
}
