package com.evo.trade.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.evo.trade.objects.BollingerBand;
import com.evo.trade.objects.StreamingBollingerBand;

public class EvoBollingerBandSendService {
	private static SimpMessagingTemplate template;

	public static void setTemplate(SimpMessagingTemplate tmplt) {
		template = tmplt;
	}

	public static void send(StreamingBollingerBand sbb, String timeFrame) {
//		System.out.println("send: " + sbb.getSymbol() + " " + timeFrame);
		template.convertAndSend("/bollingerband/" + timeFrame, sbb);
	}
}
