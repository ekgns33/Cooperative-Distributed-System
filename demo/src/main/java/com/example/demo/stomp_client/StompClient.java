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
    private static String id;

    public static void connect(String ip) throws ExecutionException, InterruptedException {
        System.out.println("ws://" + ip + ":8080/whiteboard");
        socketStompClient = ClientWebSocketStompConfig.getWebSocketStompClient("ws://" + ip + ":8080/whiteboard");
    }

    public static void subscribe(HashMap<Integer, Figure> figureMap, Queue<Figure> figures, JLabel noticeLabel) {
        socketStompClient.subscribe("/room", new ClientWebSocketStompSessionHandler(figureMap, figures, noticeLabel));
    }

    public static void send(Message message) {
        socketStompClient.send("/room/pub", message);
    }
}
