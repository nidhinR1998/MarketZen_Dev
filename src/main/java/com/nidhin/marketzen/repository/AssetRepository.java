package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUserId(long userId);

    Asset findByUserIdAndCoinId(Long userId, String coinID);

}
