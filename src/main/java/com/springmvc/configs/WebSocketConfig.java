package com.springmvc.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

// Enable STOMP messaging

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) 
	{
		// 表示在topic和user这两个域上可以向客户端发消息
        config.enableSimpleBroker("/topic", "/user");
        
        // 表示给指定用户发送（一对一）的主题前缀是 /user/  
        config.setUserDestinationPrefix("/user/");
        
        // 表示客户端向服务端发送时的主题上面需要加"/app"作为前缀
        config.setApplicationDestinationPrefixes("/app");
    }
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) 
	{
		// This method registers "/my-websocket" as a STOMP endpoint. This path 
		// is distinct from any destination path that you might send or receive 
		// messages from. It is the endpoint that a client would *connect to* 
		// before subscribing to or publishing to a destination path. 
		registry.addEndpoint("/my-websocket").withSockJS();
	}

}