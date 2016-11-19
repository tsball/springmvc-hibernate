package com.springmvc.sockets;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * This is the socket for message sending and receiving
 */
@ServerEndpoint("/messageSocket")
@Component
public class MessageSocket {

	private static int onlineCount = 0;

	private static CopyOnWriteArraySet<MessageSocket> messageSocketSet = new CopyOnWriteArraySet<MessageSocket>();

	private Session session;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		messageSocketSet.add(this);
		addOnlineCount();
		System.out.println("New person listens to the message socket! Current people: " + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		messageSocketSet.remove(this);
		subOnlineCount();
		System.out.println("One person stops listening to the message socket! Current people: " + getOnlineCount());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("New message: " + message);
		// Send to all the people
		for (MessageSocket messageSocket : messageSocketSet) {
			messageSocket.sendMessage(message);
		}
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		onlineCount--;
	}

}
