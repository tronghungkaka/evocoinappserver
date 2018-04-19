package com.evo.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.evo.trade.service.EvoBollingerBandSendService;

@Component
public class ContextRefreshedHandler implements ApplicationListener<ContextRefreshedEvent> {

//	private static Logger logger = LoggerFactory.getLogger(ContextRefreshedHandler.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			// Initialize the template for web socket messages
			EvoBollingerBandSendService.setTemplate(template);
			System.out.println("initialize for EvoBollingerBandSendService");
		} catch (Exception ex) {
			ex.printStackTrace();
//			logger.error(getClass().getName(), ex);
		}
	}
}