package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.Figure;
import org.springframework.messaging.simp.stomp.StompSession;

import javax.swing.*;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class StompClient {

    private static StompSession socketStompClient;

    public static void connect(String ip, int port) throws ExecutionException, InterruptedException {
        System.out.println("Connecting to ws://" + ip + ":" + port + "/whiteboard");
        socketStompClient = ClientWebSocketStompConfig.getWebSocketStompClient("ws://" + ip + ":" + port + "/whiteboard");
    }

    public static void subscribe(HashMap<Integer, Figure> figureMap, Queue<Figure> figures, JLabel noticeLabel) {
        String sessionId = socketStompClient.getSessionId();
        ClientWebSocketStompSessionHandler stompHandler = new ClientWebSocketStompSessionHandler(figureMap, figures, noticeLabel);
        socketStompClient.subscribe("/room-user" + sessionId, stompHandler);
        socketStompClient.subscribe("/room", stompHandler);
    }

    public static void send(Message message) {
        socketStompClient.send("/room/pub", message);
    }
}
