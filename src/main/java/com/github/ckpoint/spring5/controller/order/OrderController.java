package com.github.ckpoint.spring5.controller.order;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.order.OrderModel;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    @PutMapping("/create")
    public OrderModel createOrder(@ValidationBody OrderModel orderModel) {
        return orderModel;
    }

    @GetMapping("/find")
    public OrderModel getOrder(@ValidationParam BaseModel baseModel) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(baseModel.getId());
        return orderModel;
    }

    @PutMapping("/update")
    public OrderModel updateOrder(@ValidationBody OrderModel orderModel) {
        return orderModel;
    }
}
