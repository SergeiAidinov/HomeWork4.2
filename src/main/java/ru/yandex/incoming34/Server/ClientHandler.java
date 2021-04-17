package ru.yandex.incoming34.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.yandex.incoming34.Server.Server;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientHandler implements Runnable {
	private Server server;
	// исходящее сообщение
	private PrintWriter outMessage;
	// входящее собщение
	private Scanner inMessage;
	private static final String HOST = "localhost";
	private static final int PORT = 3443;
	// клиентский сокет
	private Socket clientSocket = null;
	// количество клиента в чате, статичное поле
	private static int clients_count = 0;

	public ClientHandler(Socket socket, Server server) {
	    try {
	      clients_count++;
	      this.server = server;
	      this.clientSocket = socket;
	      this.outMessage = new PrintWriter(socket.getOutputStream());
	      this.inMessage = new Scanner(socket.getInputStream());
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	  }
	
	public ClientHandler() {
		
	}

	public void run() {
		while (true) {
			// сервер отправляет сообщение
			// server.sendMessageToAllClients("Новый участник вошёл в чат!");
			// server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
			break;
		}
		while (true) {
			// Если от клиента пришло сообщение
			if (inMessage.hasNext()) {
				String clientMessage = inMessage.nextLine();
				// если клиент отправляет данное сообщение, то цикл прерывается и
				// клиент выходит из чата
				if (clientMessage.equalsIgnoreCase("##session##end##")) {
					break;
				}
				// выводим в консоль сообщение (для теста)
				System.out.println(clientMessage);
				// отправляем данное сообщение всем клиентам
				server.sendMessageToAllClients(clientMessage);
			}
			// останавливаем выполнение потока на 100 мс
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				this.close();
			}
		}
	}

	public void sendMsg(String msg) {
		try {
			outMessage.println(msg);
			outMessage.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// клиент выходит из чата
	public void close() {
		// удаляем клиента из списка
		server.removeClient(this);
		clients_count--;
		server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
	}

	public void setSocket(Socket socket) {
		clientSocket = socket;
		
	}

	public void serServer(Server srv) {
		server =srv;
		
	}

}
