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
import java.util.concurrent.ConcurrentMap;

public class ClientWebSocketStompSessionHandler extends StompSessionHandlerAdapter {

    private final JLabel noticeLabel;
    private final PriorityQueue<Figure> figures;
    private final ConcurrentMap<Integer, Figure> figureMap;
    private final Object lock;
    private BooleanWrapper lockResult;
    private BooleanWrapper loading;
    private boolean loadLock = false;
    private boolean clearLock = false;

    public ClientWebSocketStompSessionHandler(ConcurrentMap<Integer, Figure> figureMap, PriorityQueue<Figure> figures, JLabel noticeLabel, Object lock, BooleanWrapper lockResult, BooleanWrapper loading) {
        this.figureMap = figureMap;
        this.figures = figures;
        this.noticeLabel = noticeLabel;
        this.lock = lock;
        this.lockResult = lockResult;
        this.loading = loading;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message message = (Message) payload;
        if (message.getStatus() == 1) {
            noticeLabel.setText(message.getNickname() + "님이 접속했습니다.");
        } else if (message.getStatus() == 2) {
            noticeLabel.setText(message.getNickname() + "님이 접속을 종료했습니다.");
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
            synchronized (lock) {
                lockResult.setValue(message.isLockResult());
                lock.notify();
            }
        } else if (message.getStatus() == 6) {
            synchronized (lock) {
                this.loadLock = message.isLockResult();
                lockResult.setValue(message.isLockResult());
                if(!this.loadLock) {
                    lock.notify();
                }
                if(this.clearLock){
                    lock.notify();
                }
            }
        } else if (message.getStatus() == 7) {
            loading.setValue(false);
            this.clearLock = false;
            this.loadLock = false;
        } else if (message.getStatus() == 8) {
            loading.setValue(true);
            synchronized (lock) {
                figureMap.clear();
                figures.clear();
                this.clearLock = true;
                if(!message.isLockResult()) {
                    System.out.println("no lock");
                    lock.notify();
                }
                if (this.loadLock) {
                    System.out.println("get lock");
                    lock.notify();
                }
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
