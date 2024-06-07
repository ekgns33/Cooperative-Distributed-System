package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.BooleanWrapper;
import com.example.demo.gui.Figure;
import org.springframework.messaging.simp.stomp.StompSession;

import javax.swing.*;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public class StompClient {

    private static StompSession socketStompClient;

    public static void connect(String ip, int port) throws ExecutionException, InterruptedException {
        System.out.println("Connecting to ws://" + ip + ":" + port + "/whiteboard");
        socketStompClient = ClientWebSocketStompConfig.getWebSocketStompClient("ws://" + ip + ":" + port + "/whiteboard");
    }

    public static void subscribe(ConcurrentMap<Integer, Figure> figureMap, PriorityQueue<Figure> figures, JLabel noticeLabel, Object lock, BooleanWrapper lockResult) {
        ClientWebSocketStompSessionHandler stompHandler = new ClientWebSocketStompSessionHandler(figureMap, figures, noticeLabel, lock, lockResult);
        socketStompClient.subscribe("/room", stompHandler);
        socketStompClient.subscribe("/user/room", stompHandler);
    }

    public static void send(Message message) {
        socketStompClient.send("/room/pub", message);
    }
}
