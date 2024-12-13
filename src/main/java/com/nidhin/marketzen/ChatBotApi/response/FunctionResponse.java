package com.nidhin.marketzen.ChatBotApi.response;

import lombok.Data;

@Data
public class FunctionResponse {
    private String currencyName;
    private String functionName;
    private String currencyData;
    private String text;
}
