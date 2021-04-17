package ru.yandex.incoming34;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Server {
	@Autowired
	ClientHandler clientHandler;

	public ClientHandler getClientHandler() {
		// TODO Auto-generated method stub
		return clientHandler;
	}

}
