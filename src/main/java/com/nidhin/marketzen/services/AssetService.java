package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.Asset;
import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUSerAsset(Long userId);

    Asset updateAsset(long assetId, double quantity) throws Exception;

    Asset findAssetByUserAndCoinId(Long userId,String coinId);

    void delete(Long assetId);
}
