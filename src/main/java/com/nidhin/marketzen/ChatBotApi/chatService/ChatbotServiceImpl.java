package com.nidhin.marketzen.ChatBotApi.chatService;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nidhin.marketzen.ChatBotApi.response.ApiResponse;
import com.nidhin.marketzen.ChatBotApi.response.FunctionResponse;
import com.nidhin.marketzen.models.Coin;
import com.nidhin.marketzen.models.CoinDTO;
import com.nidhin.marketzen.services.CoinService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class ChatbotServiceImpl implements ChatbotService {
    @Value("${coingecko.api.key}")
    private String API_KEY;

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoinService coinService;

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        ApiResponse response = new ApiResponse();
        try {
            // Step 1: Fetch function response
            FunctionResponse res = getFunctionResponse(prompt);

            // Step 2: Fetch API response for currency
            CoinDTO apiResponse = makeApiRequest(res.getCurrencyName());

            // Step 3: Prepare Gemini API URL and headers
            String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Step 4: Create JSON body
            String body = createRequestBody(prompt, res, apiResponse);

            // Step 5: Make API call to Gemini
            String responseBody = callGeminiAPI(GEMINI_API_URL, headers, body);

            // Step 6: Parse response
            String text = parseResponse(responseBody);

            // Step 7: Set success message
            response.setMessage(text);
        } catch (Exception e) {
            // Handle any exception that occurs and set error message
            response.setMessage("Failed to fetch coin details: " + e.getMessage());
        }
        return response;
    }

    private String createRequestBody(String prompt, FunctionResponse res, CoinDTO apiResponse) throws JSONException {
        return new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "model")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionCall", new JSONObject()
                                                        .put("name", res.getFunctionName())
                                                        .put("args", new JSONObject()
                                                                .put("currencyName", res.getCurrencyName())
                                                                .put("currencyData", res.getCurrencyData())
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "function")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionResponse", new JSONObject()
                                                        .put("name", res.getFunctionName())
                                                        .put("response", new JSONObject()
                                                                .put("name", res.getFunctionName())
                                                                .put("content", apiResponse)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", res.getFunctionName())
                                                .put("description", "Get crypto data from given currency object.")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description", "The currency Name, id, symbol.")
                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description", "The currency data including id, symbol, current price, image, market cap, etc.")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")
                                                                .put("currencyData")
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .toString();
    }

    private String callGeminiAPI(String url, HttpHeaders headers, String body) throws RestClientException {
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("AI_Request -----------> " + request);
        System.out.println("Response -----------> " + response.getBody());
        return response.getBody();
    }

    private String parseResponse(String responseBody) throws Exception {
        ReadContext ctx = JsonPath.parse(responseBody);
        return ctx.read("$.candidates[0].content.parts[0].text");
    }


    // Method to make an API request and fetch coin details as a JSON string
    public CoinDTO makeApiRequest(String currencyName) throws Exception {
        String coinName = currencyName.toLowerCase();
        String url1 = "https://api.coingecko.com/api/v3/coins/" + coinName + "?x_cg_demo_api_key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        CoinDTO coinDTO = new CoinDTO();
        try {
            HttpHeaders headers = new HttpHeaders();
            // headers.set("x_cg_demo_api_key", API_KEY);
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            Coin coin = new Coin();
            coin.setId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").get("large").asText());
            JsonNode marketData = jsonNode.get("market_data");
            coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
            coin.setTotalSupply(marketData.get("total_supply").asLong());
            coinDTO = mapToCoinDTO(coin);
            return coinDTO;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("ERROR-------->" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private CoinDTO mapToCoinDTO(Coin coin) {
        CoinDTO coinDTO = new CoinDTO();
        coinDTO.setId(coin.getId());
        coinDTO.setName(coin.getName());
        coinDTO.setSymbol(coin.getSymbol());
        coinDTO.setImage(coin.getImage());
        coinDTO.setCurrentPrice(coin.getCurrentPrice());
        coinDTO.setMarketCap(coin.getMarketCap());
        coinDTO.setMarketCapRank(coin.getMarketCapRank());
        coinDTO.setTotalVolume(coin.getTotalVolume());
        coinDTO.setHigh24h(coin.getHigh24h());
        coinDTO.setLow24h(coin.getLow24h());
        coinDTO.setPriceChange24h(coin.getPriceChange24h());
        coinDTO.setPriceChangePercentage24h(coin.getPriceChangePercentage24h());
        coinDTO.setMarketCapChange24h(coin.getMarketCapChange24h());
        coinDTO.setMarketCapChangePercentage24h(coin.getMarketCapChangePercentage24h());
        coinDTO.setTotalSupply(coin.getTotalSupply());
        return coinDTO;
    }

  //  @Override
    public FunctionResponse getFunctionResponse(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY;

        //Create JSON request body using method chaining
        JSONObject requestBodyJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                //.put("role", "user")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Get the coin details from given currency object")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put(
                                                                                "description",
                                                                                "The currency name, " +
                                                                                        "id, symbol.")
                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "Currency Data id, " +
                                                                                        "symbol, " +
                                                                                        "name, " +
                                                                                        "image, " +
                                                                                        "current_price, " +
                                                                                        "marker_cap, " +
                                                                                        "marker_cap_rank, " +
                                                                                        "fully_diluted_valuation, " +
                                                                                        "total_volume, high_24h, " +
                                                                                        "low_24, price_change_24h, " +
                                                                                        "price_change_percentage_24h, " +
                                                                                        "market_cap_change_24h, " +
                                                                                        "market_cap_change_percentage_24h, " +
                                                                                        "circulating_supply, " +
                                                                                        "total_supply, " +
                                                                                        "max_supply, " +
                                                                                        "ath, " +
                                                                                        "auth_change_percentage, " +
                                                                                        "ath_date, " +
                                                                                        "atl, " +
                                                                                        "atl_change_percentage, " +
                                                                                        "atl_date, last_updated.")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")
                                                                .put("currencyData")
                                                        )
                                                )
                                        )
                                )
                        )
                );
        //Create HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create the HTTP entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);

        //Make the POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        String responseBody = response.getBody();
        System.out.println("=------------------ " + responseBody);

//        ReadContext ctx = JsonPath.parse(responseBody);
//
//        //Extract specific values
//        String currencyName = ctx.read("$.candidates[0].content.parts[0].functionCall.args.currencyName");
//        String currencyData = ctx.read("$.candidates[0].content.parts[0].functionCall.args.currencyData");
//        String name = ctx.read("$.candidates[0].content.parts[0].functionCall.name");
//
//        //Print the extracted values
//        FunctionResponse res = new FunctionResponse();
//        res.setCurrencyName(currencyName);
//        res.setCurrencyData(currencyData);
//        res.setFunctionName(name);
        JSONObject jsonObject = new JSONObject(responseBody);

        //Extract the first candidate
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);

        //Extract the function call details
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        JSONObject firstPart = parts.getJSONObject(0);
        JSONObject functionCall = firstPart.getJSONObject("functionCall");

        String functionName = functionCall.getString("name");
        JSONObject args = functionCall.getJSONObject("args");
        String currencyName = args.getString("currencyName");
        String currencyData = args.getString("currencyData");

        //Print or use the extracted values
        System.out.println("FunctionName " + functionName);
        System.out.println("Currency Name  " + currencyName);
        System.out.println("Currency Data " + currencyData);

        FunctionResponse res = new FunctionResponse();
        res.setCurrencyName(currencyName);
        res.setCurrencyData(currencyData);
        res.setFunctionName(functionName);

        return res;

    }


    // Utility method to parse JSON string to a Coin object
    private Coin parseJsonToCoin(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(); // Using Jackson library for JSON parsing
        return objectMapper.readValue(json, Coin.class); // Map JSON string to Coin object
    }

    @Override
    public String simpleChat(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt))))
                ).toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        return response.getBody();
    }
}
