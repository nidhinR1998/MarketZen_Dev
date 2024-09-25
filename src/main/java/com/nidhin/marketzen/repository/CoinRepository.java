package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Integer> {

}
