package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.Watchlist;
import com.nidhin.marketzen.services.WatchlistService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<Watchlist,Long> {
    Watchlist findByUserId(Long userId);
}
