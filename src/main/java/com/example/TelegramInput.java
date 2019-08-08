package com.example;

import org.apache.camel.builder.RouteBuilder;

public class TelegramInput extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("telegram:bots/{{env:TELEGRAM_API_KEY}}").tracing().to("log:info")
				.convertBodyTo(String.class).process(exchange -> {

					String body = exchange.getIn().getBody(String.class);

					body += "=====CamelTelegramChatId===" + exchange.getIn().getHeader("CamelTelegramChatId");
					exchange.getIn().setBody(body);
				}).to("knative:channel/telegram-input");

	}

}
