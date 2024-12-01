package com.nidhin.marketzen.ChatBotApi.chatController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nidhin.marketzen.ChatBotApi.chatService.ChatbotService;
import com.nidhin.marketzen.ChatBotApi.response.ApiResponse;
import com.nidhin.marketzen.ChatBotApi.response.FunctionResponse;
import com.nidhin.marketzen.models.CoinDTO;
import com.nidhin.marketzen.requests.PromptBody;
import com.nidhin.marketzen.services.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/getData")
    public ResponseEntity<CoinDTO> getData(@RequestBody PromptBody promptBody) throws Exception {
        CoinDTO response = chatbotService.makeApiRequest(promptBody.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/getData2")
    public ResponseEntity<ApiResponse>getCoinDetails(@RequestBody PromptBody promptBody) throws Exception {
        ApiResponse response = chatbotService.getCoinDetails(promptBody.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/simple")
    public ResponseEntity<String>simpleChatHandler(@RequestBody PromptBody promptBody) throws Exception {
        String response = chatbotService.simpleChat(promptBody.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/functionCall")
    public ResponseEntity<FunctionResponse>getFunctionCall(@RequestBody PromptBody promptBody) throws Exception {
        FunctionResponse response = chatbotService.getFunctionResponse(promptBody.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
