package com.example.demo.models;

import com.example.demo.enums.FeeTypeEnum;
import com.example.demo.enums.ValueTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fee {
    private String description;
    private FeeTypeEnum feeType;
    private ValueTypeEnum valueType;
    private BigDecimal value;
}
