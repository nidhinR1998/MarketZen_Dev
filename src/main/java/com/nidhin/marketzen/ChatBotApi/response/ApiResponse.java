package com.nidhin.marketzen.ChatBotApi.response;

import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.CoinDTO;
import lombok.Data;

@Data
public class ApiResponse {
  private String message;
  private CoinDTO coin;
}
