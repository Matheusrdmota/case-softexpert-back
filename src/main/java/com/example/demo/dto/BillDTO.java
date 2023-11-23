package com.example.demo.dto;

import com.example.demo.models.Fee;
import com.example.demo.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private List<Item> foodsList;
    private List<Fee> feesList;
}
