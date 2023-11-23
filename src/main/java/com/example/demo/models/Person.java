package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private BigDecimal value;
    private String paymentLink;
    private boolean friend;
}
