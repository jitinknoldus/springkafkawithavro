package org.knoldus.util;

import org.knoldus.domain.generated.*;

import java.util.List;
import java.util.Random;

public class CoffeeOrderUtil {
    public static CoffeeOrder buildNewCoffeeOrder() {
//        var orderId = OrderId.buildAddress()
//                .setId(randomId())
//                .build();

        return  CoffeeOrder.newBuilder()
                .setId(randomId())
                .setName("Jitin Saxena")
                .setStore(generateStore())
                .setOrderLineItems(generateOrderLineItems())
                .setStatus("New")
                .build();
    }

    private static List<OrderLineItem> generateOrderLineItems() {

        var orderLineItem = OrderLineItem.newBuilder()
                .setName("Caffe Latte")
                .setQuantity(1)
                .setSize(Size.MEDIUM)
                .build();

        return List.of(orderLineItem);
    }

    private static Store generateStore(){

        return  Store.newBuilder()
                .setId(randomId())
                .setAddress(buildAddress())
                .build();
    }

    private static Address buildAddress() {

        return Address.newBuilder()
                .setAddressLine1("Gaur city Mall")
                .setCity("Greater Noida")
                .setProvince("Uttar Pradesh")
                .setZip("201318")
                .setCountry("India")
                .build();

    }

    public static int randomId(){
        Random random = new Random();
        return random.nextInt(1000);
    }

}