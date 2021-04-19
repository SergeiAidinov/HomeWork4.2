package ru.yandex.incoming34.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.yandex.incoming34.Server.Server;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientHandler implements Runnable {
	@Autowired
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
	
	public void initialize(Socket socket, Server server) {
	    try {
	      clients_count++;
	      this.server = server;
	      this.clientSocket = socket;
	      outMessage = new PrintWriter(socket.getOutputStream());
	      inMessage = new Scanner(socket.getInputStream());
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	    System.out.println("ClientHandler initialized: " + this + " for Server: " + server);
	    System.out.println("Канал входящих сообщений: " + inMessage + " Канал исходящих сообщений: " + outMessage);
	    new Thread(this).start();
	    //run();
	  }
	
	
	public ClientHandler() {
		
	}
	

	public void run() {
		
			// сервер отправляет сообщение
			server.sendMessageToAllClients("Новый участник вошёл в чат!");
			server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
		
		while (true) {
			if (Objects.isNull(inMessage)) {
				continue;
			}
			// Если от клиента пришло сообщение
			if (inMessage.hasNext()) {
				String clientMessage = inMessage.nextLine();
				server.sendMessageToAllClients(clientMessage);
				System.out.println(clientMessage);
				// если клиент отправляет данное сообщение, то цикл прерывается и
				// клиент выходит из чата
				if (clientMessage.equalsIgnoreCase("##session##end##")) {
					break;
				}
				// выводим в консоль сообщение (для теста)
				
				// отправляем данное сообщение всем клиентам
				//server.sendMessageToAllClients(clientMessage);
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

	public void sendMessageToClient(String msg) {
		try {
			System.out.println("Sending: " + msg + " to " + outMessage);
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

}
