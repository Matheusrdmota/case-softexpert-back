package com.example.demo.servicesImpl;

import com.example.demo.models.Bill;
import com.example.demo.models.Fee;
import com.example.demo.models.Item;
import com.example.demo.models.Person;
import com.example.demo.services.BillService;
import com.example.demo.services.PaymentIntegration;
import com.example.demo.enums.FeeTypeEnum;
import com.example.demo.enums.ValueTypeEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    private PaymentIntegration paymentIntegration;

    public BillServiceImpl(PaymentIntegration paymentIntegration){
        this.paymentIntegration = paymentIntegration;
    }

    @Override
    public List<Person> calculatedBill(Bill bill) {
        if(bill.getFeesList().stream().anyMatch(fee -> fee.getValue() == null || fee.getValue().compareTo(BigDecimal.ZERO) < 0) ||
            bill.getFoodsList().stream().anyMatch(item -> item.getValue() == null || item.getValue().compareTo(BigDecimal.ZERO) < 0)){
            throw new IllegalArgumentException("Items and fees must have positive values!");
        }

        if(bill.getFoodsList().stream().anyMatch(item -> item.getOwner().isFriend() && item.getOwner().getName() == "")){
            throw new IllegalArgumentException("Friend's name must be informed!");
        }

        List<Person> personList = this.calculateTotalForEachPerson(bill);

        bill.setFoodTotalValue(this.calculateBillTotalFoodValue(bill.getFoodsList()));
        bill.setFeeTotalValue(this.calculateBillTotalFeeValue(bill.getFeesList(), bill.getFoodTotalValue()));
        bill.setTotalValue(bill.getFeeTotalValue().add(bill.getFoodTotalValue()));

        personList
                .parallelStream()
                .forEach(person -> {
                    BigDecimal value = person.getValue()
                            .divide(bill.getFoodTotalValue(), 3, RoundingMode.HALF_UP)
                            .multiply(bill.getTotalValue());
                    person.setValue(value);
                    person.setPaymentLink(person.isFriend() ? this.paymentIntegration.paymentLink(value) : "");
                });

        return personList;
    }

    public List<Person> calculateTotalForEachPerson(Bill bill){
        List<Person> personsList = bill.getFoodsList().stream().map(Item::getOwner).distinct().toList();

        for(Person person : personsList){
            person.setValue(bill.getFoodsList().stream()
                    .filter(x -> x.getOwner().equals(person))
                    .map(Item::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return personsList;
    }

    public BigDecimal calculateBillTotalFoodValue(List<Item> itemsList){
        return itemsList
                .stream()
                .map(Item::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBillTotalFeeValue(List<Fee> fees, BigDecimal foodTotalValue){
        return fees.stream()
                .peek(fee -> {
                    if(fee.getValueType().equals(ValueTypeEnum.PERCENTAGE)){
                        fee.setValue(fee.getValue()
                                .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP)
                                .multiply(foodTotalValue));
                    }
                })
                .map(fee -> fee.getFeeType().equals(FeeTypeEnum.ADDITION) ? fee.getValue() : fee.getValue().multiply(BigDecimal.valueOf(-1)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
