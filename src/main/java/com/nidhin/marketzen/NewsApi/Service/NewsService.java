package com.nidhin.marketzen.NewsApi.Service;

import com.nidhin.marketzen.response.NewsResponse;

public interface NewsService {

   public NewsResponse fetchLatestBusinessNews() throws Exception;

   public NewsResponse fetchLatestCryptoNews() throws Exception;

   public NewsResponse fetchLatestSportsNews() throws Exception;

   public NewsResponse fetchLatestPoliticalNews() throws Exception;
}
