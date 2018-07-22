package com.github.ckpoint.spring5.model.product;

import com.github.ckpoint.spring5.model.BaseModel;
import lombok.Data;

@Data
public class ProductModel extends BaseModel {

    private String name;
    private ProductType type;
    private String description;
    private String photoUrl;
    private Long basePrice;
    private Double discountPercent;
}
