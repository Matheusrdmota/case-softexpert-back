package com.example.demo.services;

import java.math.BigDecimal;

public interface PaymentIntegration {
    String paymentLink(BigDecimal value);
}
