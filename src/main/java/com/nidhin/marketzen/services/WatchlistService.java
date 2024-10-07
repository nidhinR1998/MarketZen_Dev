package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.models.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(User user);
    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
