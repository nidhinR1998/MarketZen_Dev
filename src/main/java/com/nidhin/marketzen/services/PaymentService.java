package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.PaymentMethod;
import com.nidhin.marketzen.models.PaymentOrder;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLing(User user, Long amount, Long orderId) throws RazorpayException;

    PaymentResponse createStripePaymentLing(User user, Long amount,Long orderId) throws StripeException;
}
