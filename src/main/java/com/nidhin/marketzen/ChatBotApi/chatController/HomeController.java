package com.nidhin.marketzen.ChatBotApi.chatController;

import com.nidhin.marketzen.ChatBotApi.chatService.ChatbotService;
import com.nidhin.marketzen.ChatBotApi.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatBot")
public class HomeController {
    
    @Autowired
    private ChatbotService chatbotService;
    @GetMapping()
    public ResponseEntity<ApiResponse>Home() {
        ApiResponse response =new ApiResponse();
        response.setMessage("Ai ChatBot");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
