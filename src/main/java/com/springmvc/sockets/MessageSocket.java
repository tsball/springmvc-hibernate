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
		System.out.println("New employee listens to the message socket! Current employees: " + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		messageSocketSet.remove(this);
		subOnlineCount();
		System.out.println("One employee stops listening to the message socket! Current employees: " + getOnlineCount());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("New message: " + message);
		// Send to all the employees
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
