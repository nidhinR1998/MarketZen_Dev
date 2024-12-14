package com.nidhin.marketzen.NewsApi.Controller;

import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.response.NewsResponse;
import com.nidhin.marketzen.NewsApi.Service.NewsService;
import com.nidhin.marketzen.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @GetMapping("/businessNews")
    public ResponseEntity<NewsResponse> getUserBusinessNews(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        logger.info("New Controller HIT 1");
        if (user != null) {
            NewsResponse response = newsService.fetchLatestBusinessNews();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        logger.error("Invalid JWT: {}", jwt);
        throw new Exception("Invalid JWT");
    }

    @GetMapping("/cryptoNews")
    public ResponseEntity<NewsResponse> getUserCryptoNews(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        logger.info("New Controller HIT 2");
        if (user != null) {
            NewsResponse response = newsService.fetchLatestCryptoNews();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        logger.error("Invalid JWT: {}", jwt);
        throw new Exception("Invalid JWT");
    }

    @GetMapping("/sportsNews")
    public ResponseEntity<NewsResponse> getUserSportsNews(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        logger.info("New Controller HIT 2");
        if (user != null) {
            NewsResponse response = newsService.fetchLatestSportsNews();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        logger.error("Invalid JWT: {}", jwt);
        throw new Exception("Invalid JWT");
    }

    @GetMapping("/politicalNews")
    public ResponseEntity<NewsResponse> getUserPoliticsNews(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        logger.info("New Controller HIT 2");
        if (user != null) {
            NewsResponse response = newsService.fetchLatestPoliticalNews();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        logger.error("Invalid JWT: {}", jwt);
        throw new Exception("Invalid JWT");
    }
}
