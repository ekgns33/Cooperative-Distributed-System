package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.Figure;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Queue;

public class ClientWebSocketStompSessionHandler extends StompSessionHandlerAdapter {

    public final HashMap<Integer, Figure> figureMap;
    public final Queue<Figure> figures;

    public ClientWebSocketStompSessionHandler(HashMap<Integer, Figure> figureMap, Queue<Figure> figures) {
        this.figureMap = figureMap;
        this.figures = figures;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message message = (Message) payload;

        // 1. message -> figure 컨버팅

        // 2. figureMap, figures에 추가하기

        System.out.println("payload = " + message);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("success connect");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("SpringStompSessionHandler.handleException");
        System.out.println("exception = " + exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("SpringStompSessionHandler.handleTransportError");
    }
}
