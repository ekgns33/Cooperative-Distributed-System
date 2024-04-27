package com.example.demo;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class WebSocketClient {

	private static WebSocketClient webSocketClient;
	private WebSocketStompClient webSocketStompClient;
	private ClientWebSocketStompSessionHandler handler;

	public static WebSocketClient getInstace() {
		if(webSocketClient == null) {
			webSocketClient = new WebSocketClient();
		}
		return webSocketClient;
	}

	private WebSocketClient() {
		this.webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
		this.handler = new ClientWebSocketStompSessionHandler();
		init();
	}
	// init
	// stompHeaders.add("host", "karim");

	public void init() {
		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
	}
	public void connect() {
		String url = "ws://localhost:8080/whiteboard";
		Object[] urlVariables = {};
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add("host", "karim");


		webSocketStompClient.connect(url, null, stompHeaders, handler, urlVariables);
	}

}
