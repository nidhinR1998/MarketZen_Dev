package com.nidhin.marketzen.services;

import com.nidhin.marketzen.domain.OrderType;
import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.Order;
import com.nidhin.marketzen.models.OrderItem;
import com.nidhin.marketzen.models.User;

import java.util.List;

public interface OrderService {
    Order creatOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrderOfUser(Long userId, OrderType OrderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
