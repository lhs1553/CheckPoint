package com.github.ckpoint.spring5.model.order;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.user.UserModel;
import lombok.Data;

import java.util.List;

/**
 * The type Common req hsim.checkpoint.model.
 */

@Data
public class OrderModel extends BaseModel {

    private UserModel user;
    private List<OrderItemModel> orderItems;
    private Long totalPrice;
    private List<String> reservations;
}

