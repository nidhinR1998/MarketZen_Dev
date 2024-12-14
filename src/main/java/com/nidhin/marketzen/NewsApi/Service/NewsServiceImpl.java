package com.nidhin.marketzen.NewsApi.Service;

import com.nidhin.marketzen.models.NewsDTO;
import com.nidhin.marketzen.response.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);
    private final RestTemplate restTemplate;

    // Constants for URL parameters
    private static final String BASE_URL = "https://gnews.io/api/v4/top-headlines";
    private static final String CATEGORY_PARAM = "category=business";
    private static final String CATEGORY_PARAM_SPORTS = "category=sports";
    private static final String CATEGORY_PARAM_POLITICAL = "category=politics";
    private static final String LANGUAGE_PARAM = "lang=en";
    private static final String COUNTRY_PARAM = "country=in";
    private static final String MAX_RESULTS_PARAM = "max=50";
    private static final String QUERY_PARAM = "q=Business";
    private static final String QUERY_PARAM_CRYPTO = "q=Crypto";
    private static final String QUERY_PARAM_SPORTS = "q=Sports";
    private static final String QUERY_PARAM_POLITICAL = "q=Politics";

    // API Keys
    private static final List<String> API_KEYS = Arrays.asList(
            "apikey=44cb614ffcdb7940b36dd9c22d99ccb1",
            "apikey=0e13184a88bd8deb2a0c3683d25ce0be"
            // Add more API keys here
    );

    // Thread-safe index for rotating API keys
    private static final AtomicInteger currentKeyIndex = new AtomicInteger(0);

    // Date format for the API
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'IST'");

    public NewsServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gets the next API key in a cyclic manner.
     *
     * @return The next API key.
     */
    private String getNextApiKey() {
        int index = currentKeyIndex.getAndUpdate(i -> (i + 1) % API_KEYS.size());
        return API_KEYS.get(index);
    }

    @Override
    public NewsResponse fetchLatestBusinessNews() {
        NewsResponse responseBusiness = new NewsResponse();

        // Calculate 'from' as start of yesterday and 'to' as 5 minutes ago
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = LocalDateTime.now().minusMinutes(5);

        // Format dates to UTC format
        String fromParam = "from=" + fromDate.format(DATE_FORMATTER);
        String toParam = "to=" + toDate.format(DATE_FORMATTER);

        // Get the next API key
        String apiKeyParam = getNextApiKey();

        // Build the full API URL
        String newsApiUrl = String.format("%s?%s&%s&%s&%s&%s&%s&%s&%s",
                BASE_URL, CATEGORY_PARAM, LANGUAGE_PARAM, COUNTRY_PARAM,
                MAX_RESULTS_PARAM, fromParam, toParam, QUERY_PARAM, apiKeyParam);

        try {
            logger.info("Requesting latest business news from URL: {}", newsApiUrl);

            NewsDTO responseAPI = restTemplate.getForObject(newsApiUrl, NewsDTO.class);

            if (responseAPI != null) {
                logger.info("Fetched {} articles successfully.", responseAPI.getTotalArticles());
                responseBusiness.setArticles(responseAPI.getArticles());
            } else {
                logger.warn("No articles found in response.");
            }
            return responseBusiness;
        } catch (RestClientException e) {
            logger.error("Failed to fetch news data from API", e);
            return null;
        }
    }

    @Override
    public NewsResponse fetchLatestCryptoNews() throws Exception {
        NewsResponse responseCrypto = new NewsResponse();

        // Calculate 'from' as start of yesterday and 'to' as 5 minutes ago
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = LocalDateTime.now().minusMinutes(5);

        // Format dates to UTC format
        String fromParam = "from=" + fromDate.format(DATE_FORMATTER);
        String toParam = "to=" + toDate.format(DATE_FORMATTER);

        // Get the next API key
        String apiKeyParam = getNextApiKey();

        // Build the full API URL
        String newsApiUrl = String.format("%s?%s&%s&%s&%s&%s&%s&%s&%s",
                BASE_URL, CATEGORY_PARAM, LANGUAGE_PARAM, COUNTRY_PARAM,
                MAX_RESULTS_PARAM, fromParam, toParam, QUERY_PARAM_CRYPTO, apiKeyParam);

        try {
            logger.info("Requesting latest crypto news from URL: {}", newsApiUrl);

            NewsDTO responseAPI = restTemplate.getForObject(newsApiUrl, NewsDTO.class);

            if (responseAPI != null) {
                logger.info("Fetched {} articles successfully.", responseAPI.getTotalArticles());
                responseCrypto.setArticles(responseAPI.getArticles());
            } else {
                logger.warn("No articles found in response.");
            }
            return responseCrypto;
        } catch (RestClientException e) {
            logger.error("Failed to fetch news data from API", e);
            return null;
        }
    }

    @Override
    public NewsResponse fetchLatestSportsNews() throws Exception {
        NewsResponse responseCrypto = new NewsResponse();

        // Calculate 'from' as start of yesterday and 'to' as 5 minutes ago
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = LocalDateTime.now().minusMinutes(5);

        // Format dates to UTC format
        String fromParam = "from=" + fromDate.format(DATE_FORMATTER);
        String toParam = "to=" + toDate.format(DATE_FORMATTER);

        // Get the next API key
        String apiKeyParam = getNextApiKey();

        // Build the full API URL
        String newsApiUrl = String.format("%s?%s&%s&%s&%s&%s&%s&%s&%s",
                BASE_URL, CATEGORY_PARAM_SPORTS, LANGUAGE_PARAM, COUNTRY_PARAM,
                MAX_RESULTS_PARAM, fromParam, toParam, QUERY_PARAM_SPORTS, apiKeyParam);

        try {
            logger.info("Requesting latest crypto news from URL: {}", newsApiUrl);

            NewsDTO responseAPI = restTemplate.getForObject(newsApiUrl, NewsDTO.class);

            if (responseAPI != null) {
                logger.info("Fetched {} articles successfully.", responseAPI.getTotalArticles());
                responseCrypto.setArticles(responseAPI.getArticles());
            } else {
                logger.warn("No articles found in response.");
            }
            return responseCrypto;
        } catch (RestClientException e) {
            logger.error("Failed to fetch news data from API", e);
            return null;
        }
    }

    @Override
    public NewsResponse fetchLatestPoliticalNews() throws Exception {
        NewsResponse responseCrypto = new NewsResponse();

        // Calculate 'from' as start of yesterday and 'to' as 5 minutes ago
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = LocalDateTime.now().minusMinutes(5);

        // Format dates to UTC format
        String fromParam = "from=" + fromDate.format(DATE_FORMATTER);
        String toParam = "to=" + toDate.format(DATE_FORMATTER);

        // Get the next API key
        String apiKeyParam = getNextApiKey();

        // Build the full API URL
        String newsApiUrl = String.format("%s?%s&%s&%s&%s&%s&%s&%s&%s",
                BASE_URL, CATEGORY_PARAM_POLITICAL, LANGUAGE_PARAM, COUNTRY_PARAM,
                MAX_RESULTS_PARAM, fromParam, toParam, QUERY_PARAM_POLITICAL, apiKeyParam);

        try {
            logger.info("Requesting latest crypto news from URL: {}", newsApiUrl);

            NewsDTO responseAPI = restTemplate.getForObject(newsApiUrl, NewsDTO.class);

            if (responseAPI != null) {
                logger.info("Fetched {} articles successfully.", responseAPI.getTotalArticles());
                responseCrypto.setArticles(responseAPI.getArticles());
            } else {
                logger.warn("No articles found in response.");
            }
            return responseCrypto;
        } catch (RestClientException e) {
            logger.error("Failed to fetch news data from API", e);
            return null;
        }
    }
}
