package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.awt.*;
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
        Figure curFigure = figureMap.get(message.getId());
        if (curFigure != null) {
            if (message.getType() == 0) {
                ((Circle) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 1) {
                ((Rect) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 2) {
                ((Line) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime());
            } else if (message.getType() == 3) {
                ((Text) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime(), message.getText());
            }
        } else {
            if (message.getType() == 0) {
                curFigure = new Circle(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 1) {
                curFigure = new Rect(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 2) {
                curFigure = new Line(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime());
            } else if (message.getType() == 3) {
                curFigure = new Text(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime(), message.getText());
            }
            figureMap.put(message.getId(), curFigure);
            figures.add(curFigure);
        }

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
