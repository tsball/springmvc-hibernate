package com.springmvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/stomp")
public class StompController
{
	private final static Logger LOG = LoggerFactory.getLogger(StompController.class.getName());
	
	// Manage a web-socket request over the topic /app/hello ('app' is the prefix
	// set with the .setApplicationDestinationPrefixes() method in the web-socket
	// configuration class). Returning a value, automatically pushes a message over
	// the /topic/greetings topic. The Greeting object is parsed to JSON before to
	// push the value back ...
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public String greeting(String message) throws Exception 
	{
		LOG.info("StompController.greeting() method invoked ...");
        return message;
    }
}
