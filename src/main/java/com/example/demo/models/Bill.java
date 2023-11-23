package com.example.demo.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Bill {
    private List<Item> foodsList;
    private List<Fee> feesList;
    private BigDecimal foodTotalValue;
    private BigDecimal feeTotalValue;
    private BigDecimal totalValue;

    public Bill(){
        this.foodTotalValue = BigDecimal.ZERO;
        this.feeTotalValue = BigDecimal.ZERO;
        this.totalValue = BigDecimal.ZERO;
    }
}
