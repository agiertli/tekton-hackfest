package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.OutgoingTextMessage;

public class TelegramOutput extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("knative:channel/telegram-input").convertBodyTo(String.class).to("log:debug").choice()
				.when(body().contains("hackfest")).process(exchange -> {

					String body = exchange.getIn().getBody(String.class);
					String header = body.substring(
							body.indexOf("=====CamelTelegramChatId===") + "=====CamelTelegramChatId===".length(),
							body.length());

					OutgoingTextMessage msg = new OutgoingTextMessage();
					msg.setText(
							"Don't anger me or something bad is going to happen to you tonight! - new version - v2");
					exchange.getOut().setBody(msg);
					exchange.getOut().setHeader("CamelTelegramChatId", header);
				}).to("telegram:bots/{{env:TELEGRAM_API_KEY}}").otherwise().to("direct:null");

		from("direct:null").to("log:debug");

	}
}
