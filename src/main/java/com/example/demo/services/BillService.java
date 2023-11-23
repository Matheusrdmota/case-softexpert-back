package com.example.demo.services;

import com.example.demo.models.Bill;
import com.example.demo.models.Person;

import java.util.List;

public interface BillService {
    List<Person> calculatedBill(Bill bill);
}
