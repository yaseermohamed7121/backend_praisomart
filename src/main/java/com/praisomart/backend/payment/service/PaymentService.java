package com.praisomart.backend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createOrder(BigDecimal amount, String currency, String receipt) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        // Razorpay expects amount in paise (Rupees * 100)
        orderRequest.put("amount", amount.multiply(new BigDecimal(100)).intValue());
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order order = razorpay.orders.create(orderRequest);
        return order.toString();
    }
}
