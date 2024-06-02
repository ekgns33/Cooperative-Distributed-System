package com.example.demo.stomp_client;

import com.example.demo.Message;
import com.example.demo.gui.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.*;

public class ClientWebSocketStompSessionHandler extends StompSessionHandlerAdapter {

    private final JLabel noticeLabel;
    private final PriorityQueue<Figure> figures;
    private final HashMap<Integer, Figure> figureMap;
    private boolean loadLock;

    public ClientWebSocketStompSessionHandler(HashMap<Integer, Figure> figureMap, PriorityQueue<Figure> figures, JLabel noticeLabel) {
        this.figureMap = figureMap;
        this.figures = figures;
        this.noticeLabel = noticeLabel;
        this.loadLock = false;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message message = (Message) payload;
        if (message.getStatus() == 1) {
//            System.out.println("[Log]message received: " + message.getNickname() + " is enter");
            noticeLabel.setText(message.getNickname() + "이(가) 접속했습니다.");
        } else if (message.getStatus() == 2) {
//            System.out.println("[Log]message received: " + message.getNickname() + " is out");
            noticeLabel.setText(message.getNickname() + "이(가) 접속을 종료했습니다.");
        } else if (message.getStatus() == 3) {
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
        } else if (message.getStatus() == 4) {
        } else if (message.getStatus() == 5) {
        } else if (message.getStatus() == 6) {
            this.loadLock = message.isLockResult();
        } else if (message.getStatus() == 7) {
            //TODO: unblock
            
        } else if (message.getStatus() == 8) {
            //TODO: block
            figureMap.clear();
            figures.clear();
            if (loadLock){
                //TODO: load data
            }
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
