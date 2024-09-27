package com.nidhin.marketzen.requests;

import com.nidhin.marketzen.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
