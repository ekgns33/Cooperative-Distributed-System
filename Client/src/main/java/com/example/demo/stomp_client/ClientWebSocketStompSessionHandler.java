package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Queue;

public class ClientWebSocketStompSessionHandler extends StompSessionHandlerAdapter {

    public final HashMap<Integer, Figure> figureMap;
    public final Queue<Figure> figures;
    public final JLabel noticeLabel;

    public ClientWebSocketStompSessionHandler(HashMap<Integer, Figure> figureMap, Queue<Figure> figures, JLabel noticeLabel) {
        this.figureMap = figureMap;
        this.figures = figures;
        this.noticeLabel = noticeLabel;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message message = (Message) payload;
        if (message.getStatus() == 1) {
//            System.out.println("[Log]message received: " + message.getNickname() + " is enter");
            noticeLabel.setText(message.getNickname() + "이(가) 접속했습니다.");
            return;
        }
        if (message.getStatus() == 2) {
//            System.out.println("[Log]message received: " + message.getNickname() + " is out");
            noticeLabel.setText(message.getNickname() + "이(가) 접속을 종료했습니다.");
            return;
        }
        Figure curFigure = figureMap.get(message.getId());
        if (curFigure != null) {
            if (message.getType() == 0) {
//                System.out.println("[Log]message received: Circle");
                ((Circle) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 1) {
//                System.out.println("[Log]message received: Rect");
                ((Rect) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 2) {
//                System.out.println("[Log]message received: Line");
                ((Line) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime());
            } else if (message.getType() == 3) {
//                System.out.println("[Log]message received: Text");
                ((Text) curFigure).set(message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime(), message.getText());
            }
        } else {
            if (message.getType() == 0) {
//                System.out.println("[Log]message received: Circle");
                curFigure = new Circle(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 1) {
//                System.out.println("[Log]message received: Rect");
                curFigure = new Rect(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
            } else if (message.getType() == 2) {
//                System.out.println("[Log]message received: Line");
                curFigure = new Line(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime());
            } else if (message.getType() == 3) {
//                System.out.println("[Log]message received: Text");
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
