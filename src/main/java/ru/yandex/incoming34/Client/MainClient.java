package ru.yandex.incoming34.Client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.yandex.incoming34.Server.Server;

public class MainClient {
	public static void main(String[] args) {
		ApplicationContext contextClient = new ClassPathXmlApplicationContext("configClient.xml");
		Client client = (Client) contextClient.getBean("client");
	}
}
