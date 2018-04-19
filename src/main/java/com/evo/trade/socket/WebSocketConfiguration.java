package com.evo.trade.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket")
			.setAllowedOrigins("*")
			.withSockJS();
	}
	
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/evo")
			.enableSimpleBroker("/bollingerband", "/coinmarketcap");
	}
}
