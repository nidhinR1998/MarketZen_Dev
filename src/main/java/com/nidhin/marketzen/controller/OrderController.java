package com.nidhin.marketzen.controller;

import com.nidhin.marketzen.domain.OrderType;
import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.Order;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.requests.CreateOrderRequest;
import com.nidhin.marketzen.services.CoinService;
import com.nidhin.marketzen.services.OrderService;
import com.nidhin.marketzen.services.UserService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

 //   @Autowired
 //   private WalletTransationService walletTransationService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    ) throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(req.getCoinId());

        Order order=orderService.processOrder(coin,req.getQuantity(),req.getOrderType(),user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId
    ) throws Exception{
        if (jwtToken == null){
            throw new Exception("token missing");
        }
        User user=userService.findUserProfileByJwt(jwtToken);
        Order order=orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrderForUser(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    ) throws Exception{
        if (jwtToken == null){
            throw new Exception("token missing");
        }
         Long userId=userService.findUserProfileByJwt(jwtToken).getId();

        List<Order> userOrders = orderService.getAllOrderOfUser(userId,order_type,asset_symbol);
        return ResponseEntity.ok(userOrders);
    }
}
