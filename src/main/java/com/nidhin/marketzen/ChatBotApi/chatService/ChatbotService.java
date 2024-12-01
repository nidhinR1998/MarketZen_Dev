package com.nidhin.marketzen.ChatBotApi.chatService;

import com.nidhin.marketzen.ChatBotApi.response.ApiResponse;
import com.nidhin.marketzen.ChatBotApi.response.FunctionResponse;
import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.CoinDTO;

public interface ChatbotService {

    /**
     * Makes an API request to fetch coin details based on the currency name.
     *
     * @param currencyName the name of the currency.
     * @return JSON string containing coin details.
     * @throws Exception if an error occurs during the API request.
     */
    public CoinDTO makeApiRequest(String currencyName) throws Exception;

    /**
     * Fetches and processes the coin details based on the provided prompt.
     *
     * @param prompt the prompt or currency name for fetching details.
     * @return ApiResponse containing the result of the operation.
     * @throws Exception if an error occurs while fetching or processing details.
     */
    ApiResponse getCoinDetails(String prompt) throws Exception;

    /**
     * Handles simple chat functionality.
     *
     * @param prompt the input for the chat functionality.
     * @return a response for the chat prompt.
     */
    String simpleChat(String prompt);

    public FunctionResponse getFunctionResponse(String prompt);
}
