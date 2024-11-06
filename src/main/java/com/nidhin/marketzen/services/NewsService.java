package com.nidhin.marketzen.services;

import com.nidhin.marketzen.response.NewsResponse;

public interface NewsService {

   public NewsResponse fetchLatestBusinessNews() throws Exception;

   public NewsResponse fetchLatestCryptoNews() throws Exception;


}
