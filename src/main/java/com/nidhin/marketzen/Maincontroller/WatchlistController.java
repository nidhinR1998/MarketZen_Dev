package com.nidhin.marketzen.Maincontroller;

import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Watchlist;
import com.nidhin.marketzen.services.CoinService;
import com.nidhin.marketzen.services.UserService;
import com.nidhin.marketzen.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Watchlist watchlist=watchlistService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchlist);
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Watchlist createWatchlist=watchlistService.createWatchlist(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createWatchlist);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(
            @PathVariable Long watchlistId
    ) throws Exception {
        Watchlist watchlist=watchlistService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    )throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(coinId);
        Coin addCoin = watchlistService.addItemToWatchlist(coin,user);
        return ResponseEntity.ok(addCoin);
    }
}
