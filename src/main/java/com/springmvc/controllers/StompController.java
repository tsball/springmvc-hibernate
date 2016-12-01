package com.springmvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvc.services.SecurityService;

@RestController
//@RequestMapping("/stomps")
public class StompController
{
	private final static Logger LOG = LoggerFactory.getLogger(StompController.class.getName());
	
	@Autowired SimpMessagingTemplate template;
	@Autowired SecurityService securityService;
	
	// 广播
	@MessageMapping("/greeting") // 发送通道。发送信息时，push消息到该路径（路径前需要加上ApplicationDestinationPrefixes）
	@SendTo("/topic/greeting") // 订阅通道。订阅该地址，当有人push消息到改通道时，会收到信息。
	public String greeting(String message) throws Exception {
		LOG.info("StompController.greeting() method invoked ...");
        return message;
    }
	
	// 点对点发送
	@MessageMapping("/message") // 发送通道。 /UserDestinationPrefix/UserId/MessageMapping, for example: /user/1/message
    @SendToUser("/message") // 订阅通道。 /UserDestinationPrefix/UserId/SendToUser, for example: /user/1/message
    public String userMessage(String message) throws Exception {  
        return message;  
    }
	
	@RequestMapping(value = "boardcast", method = RequestMethod.POST)
	public ResponseEntity<?> boardcast(@RequestParam("message") String message) {
		template.convertAndSend("/topic/hello", message);
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@RequestMapping(value = "notify", method = RequestMethod.POST)
	public ResponseEntity<?> notify(@RequestParam("message") String message) {
		Long userId = securityService.findLoggedInUser().getId();
		template.convertAndSendToUser(userId.toString(), "/message", message);
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
