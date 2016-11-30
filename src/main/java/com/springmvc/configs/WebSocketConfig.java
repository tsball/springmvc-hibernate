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
		// Enables the STOMP broker relay and sets its destination prefixes to "/topic". 
		// (It is possible to set more than one destination, e.g. "/topic", "/queue", ..)
		// This is a clue to Spring that any messages whose destination begins with /topic 
		// should go to the STOMP broker. Depending on which STOMP broker you choose, you 
		// may be limited in your choices for the destination prefix
        config.enableSimpleBroker("/topic");
        
        // Sets an application prefix to "/app". Any messages whose destination begins
        // with /app will be routed to an @MessageMapping method and not published to a 
        // broker queue or topic.
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