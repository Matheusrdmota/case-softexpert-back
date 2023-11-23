package com.example.demo.servicesImpl;

import com.example.demo.services.PaymentIntegration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PicPayPaymentIntegration implements PaymentIntegration {

    @Override
    public String paymentLink(BigDecimal value) {
        if(value == null || value.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("A positive value must be informed!");
        }
        String baseUrl = "https://picpay.me/matheusrodmota";
        return String.format("%s/%s", baseUrl, value.toString());
    }
}
