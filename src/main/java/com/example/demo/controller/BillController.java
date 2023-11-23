package com.example.demo.controller;

import com.example.demo.dto.BillDTO;
import com.example.demo.models.Bill;
import com.example.demo.models.Person;
import com.example.demo.services.BillService;
import com.example.demo.servicesImpl.BillServiceImpl;
import com.example.demo.servicesImpl.PicPayPaymentIntegration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/bill")
public class BillController {
    private BillService service;

    public BillController(){
        this.service = new BillServiceImpl(new PicPayPaymentIntegration());
    }

    @PostMapping
    public ResponseEntity calculateBill(@RequestBody BillDTO billDTO){
        Bill bill = new Bill();
        bill.setFeesList(billDTO.getFeesList());
        bill.setFoodsList(billDTO.getFoodsList());

        try{
            return ResponseEntity.ok(this.service.calculatedBill(bill));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
