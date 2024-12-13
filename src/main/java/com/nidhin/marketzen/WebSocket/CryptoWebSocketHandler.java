package com.nidhin.marketzen.WebSocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class CryptoWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // You can send live updates here
        session.sendMessage(new TextMessage("Live crypto update: " + message.getPayload()));
    }
}

