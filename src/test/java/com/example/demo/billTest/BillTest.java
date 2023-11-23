package com.example.demo.billTest;

import com.example.demo.models.Bill;
import com.example.demo.models.Fee;
import com.example.demo.models.Item;
import com.example.demo.models.Person;
import com.example.demo.enums.FeeTypeEnum;
import com.example.demo.enums.ValueTypeEnum;
import com.example.demo.servicesImpl.BillServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BillTest {
    @Autowired
    private BillServiceImpl billService;
    private Bill bill;
    private List<Person> personList;
    private List<Item> itemList;
    private List<Fee> feeList;

    @BeforeEach
    void setBillInformations(){
        bill = new Bill();

        personList = new ArrayList<>();
        Person person1 = new Person(null, BigDecimal.ZERO, "", false);
        Person person2 = new Person("amigo", BigDecimal.ZERO, "", true);

        personList.add(person1);
        personList.add(person2);

        itemList = new ArrayList<>();
        itemList.add(new Item("hamburguer", BigDecimal.valueOf(40), person1));
        itemList.add(new Item("sobremesa", BigDecimal.valueOf(2), person1));
        itemList.add(new Item("sanduiche", BigDecimal.valueOf(8), person2));

        feeList = new ArrayList<>();
        feeList.add(new Fee("entrega", FeeTypeEnum.ADDITION, ValueTypeEnum.NUMERIC, BigDecimal.valueOf(8)));
        feeList.add(new Fee("desconto", FeeTypeEnum.DISCOUNT, ValueTypeEnum.NUMERIC, BigDecimal.valueOf(20)));

        bill.setFeesList(feeList);
        bill.setFoodsList(itemList);
    }

    @Test
    void assertCalculateTotalForEachPersonWithTwoPersons(){
        List<Person> personListTest = new ArrayList<>();

        Person person1test = new Person(null, BigDecimal.valueOf(42), "", false);
        Person person2test = new Person("amigo", BigDecimal.valueOf(8), "", true);

        personListTest.add(person1test);
        personListTest.add(person2test);

        Assertions.assertEquals(this.billService.calculateTotalForEachPerson(bill), personListTest);
    }

    @Test
    void assertCalculateTotalForEachPersonWithThreePersons(){
        Person person3 = new Person("amigo2", BigDecimal.ZERO, "", true);
        this.personList.add(person3);

        List<Person> personListTest = new ArrayList<>();
        Person person1test = new Person(null, BigDecimal.valueOf(42), "", false);
        Person person2test = new Person("amigo", BigDecimal.valueOf(8), "", true);
        Person person3test = new Person("amigo2", BigDecimal.valueOf(10), "", true);

        personListTest.add(person1test);
        personListTest.add(person2test);
        personListTest.add(person3test);

        this.itemList.add(new Item("sanduiche", BigDecimal.valueOf(10), person3));

        Assertions.assertEquals(this.billService.calculateTotalForEachPerson(bill), personListTest);
    }

    @Test
    void assertCalculateTotalFoodValue(){
        Assertions.assertEquals(this.billService.calculateBillTotalFoodValue(itemList), BigDecimal.valueOf(50));
    }

    @Test
    void assertCalculateTotalFeeValue(){
        BigDecimal totalFoodValue = this.billService.calculateBillTotalFoodValue(itemList);
        Assertions.assertEquals(this.billService.calculateBillTotalFeeValue(feeList, totalFoodValue), BigDecimal.valueOf(-12));
    }

    @Test
    void assertCalculatedBillWithTwoPersons(){
        List<Person> personListTest = new ArrayList<>();
        Person person1test = new Person(null, BigDecimal.valueOf(31.92).setScale(3), "", false);
        Person person2test = new Person("amigo", BigDecimal.valueOf(6.08).setScale(3), "https://picpay.me/matheusrodmota/6.080", true);

        personListTest.add(person1test);
        personListTest.add(person2test);

        Assertions.assertEquals(billService.calculatedBill(bill), personListTest);
    }

    @Test
    void assertCalculatedBillWithThreePersons(){
        Person person3 = new Person("amigo2", BigDecimal.ZERO, "", true);
        this.personList.add(person3);

        List<Person> personListTest = new ArrayList<>();
        Person person1test = new Person(null, BigDecimal.valueOf(36.96).setScale(3), "", false);
        Person person2test = new Person("amigo", BigDecimal.valueOf(7.040).setScale(3), "https://picpay.me/matheusrodmota/7.040", true);
        Person person3test = new Person("amigo2", BigDecimal.valueOf(44).setScale(3), "https://picpay.me/matheusrodmota/44.000", true);

        personListTest.add(person1test);
        personListTest.add(person2test);
        personListTest.add(person3test);

        this.itemList.add(new Item("sanduiche", BigDecimal.valueOf(50), person3));

        Assertions.assertEquals(billService.calculatedBill(bill), personListTest);
    }

    @Test
    void assertExceptionCalculatedBillWithNullValueItem(){
        Person person3 = new Person("amigo2", BigDecimal.ZERO, "", true);
        this.personList.add(person3);

        this.itemList.add(new Item("sanduiche", null, person3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> billService.calculatedBill(bill));
    }

    @Test
    void assertExceptionCalculatedBillWithNullValueFee(){
        this.feeList.add(new Fee("desconto", FeeTypeEnum.ADDITION, ValueTypeEnum.NUMERIC, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> billService.calculatedBill(bill));
    }

    @Test
    void assertExceptionCalculatedBillWithNegativeValueItem(){
        Person person3 = new Person("amigo2", BigDecimal.ZERO, "", true);
        this.personList.add(person3);

        this.itemList.add(new Item("sanduiche", BigDecimal.valueOf(-20), person3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> billService.calculatedBill(bill));
    }

    @Test
    void assertExceptionCalculatedBillWithNegativeValueFee(){
        this.feeList.add(new Fee("desconto", FeeTypeEnum.ADDITION, ValueTypeEnum.NUMERIC, BigDecimal.valueOf(-10)));

        Assertions.assertThrows(IllegalArgumentException.class, () -> billService.calculatedBill(bill));
    }

    @Test
    void assertExceptionCalculatedBillWithFriendWithNoName(){
        Person person3 = new Person("", BigDecimal.ZERO, "", true);
        this.personList.add(person3);

        this.itemList.add(new Item("sanduiche", BigDecimal.valueOf(20), person3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> billService.calculatedBill(bill));
    }
}
