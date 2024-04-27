package com.example.demo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class ClientWebSocketStompSessionHandler extends StompSessionHandlerAdapter {

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {

		// 구독한 채널의 메세지 받기
		System.out.println("SpringStompSessionHandler.handleFrame");
		System.out.println("headers = " + headers);
		System.out.println("payload = " + new String((byte[]) payload));
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Object.class;
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

		// 구독
		session.subscribe("/room/pub", this);

		Map<Object, String> params = new HashMap<>();
		params.put("channelId", "ss");
		// 메세지 보냄
		session.send("/room/pub", params);

		//System.out.println("params = " + params);
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
