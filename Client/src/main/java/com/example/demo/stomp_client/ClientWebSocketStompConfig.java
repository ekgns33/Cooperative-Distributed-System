package com.example.demo.stomp_client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class ClientWebSocketStompConfig {

    public static StompSession getWebSocketStompClient(String url) throws ExecutionException, InterruptedException {
        WebSocketStompClient webSocketClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler stompSessionHandler = new ClientWebSocketStompSessionHandler(null, null, null);
        Object[] urlVariables = {};
        return webSocketClient.connect(url, null, null, stompSessionHandler, urlVariables).get();
    }
}
