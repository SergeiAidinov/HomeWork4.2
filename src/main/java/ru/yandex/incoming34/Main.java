package ru.yandex.incoming34;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
		Server server = (Server) context.getBean("server");
		System.out.println("Server: " + server + " Clienthandler: " + server.getClientHandler());

	}

}
