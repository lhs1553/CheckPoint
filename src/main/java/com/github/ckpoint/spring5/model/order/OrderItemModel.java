package com.github.ckpoint.spring5.model.order;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.product.ProductModel;
import lombok.Data;

@Data
public class OrderItemModel extends BaseModel {

    private ProductModel product;
    private Integer orderUnit;

}
