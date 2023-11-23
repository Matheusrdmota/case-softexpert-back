package com.example.demo.paymentIntegrationTest;

import com.example.demo.services.PaymentIntegration;
import com.example.demo.servicesImpl.PicPayPaymentIntegration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class PicPayIntegrationTest {
    private static PaymentIntegration paymentIntegration;

    @BeforeAll
    static void setPaymentIntegrationConfig(){
        paymentIntegration = new PicPayPaymentIntegration();
    }

    @Test
    void assertIfTheUrlContainsTheCorrectValue(){
        String urlTest = "https://picpay.me/matheusrodmota/7.040";

        Assertions.assertEquals(paymentIntegration.paymentLink(BigDecimal.valueOf(7.04).setScale(3)), urlTest);
    }

    @Test
    void assertExceptionWhenNullValueIsInformed(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> paymentIntegration.paymentLink(null));
    }

    @Test
    void assertExceptionWhenNegativeValueIsInformed(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> paymentIntegration.paymentLink(BigDecimal.valueOf(-1)));
    }
}
