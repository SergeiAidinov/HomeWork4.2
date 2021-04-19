package ru.yandex.incoming34.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Client {
	// графический интерфейс
	ClientWindow clientWindow;
	// адрес сервера
		private static final String SERVER_HOST = "localhost";
		// порт
		private static final int SERVER_PORT = 3443;
		// клиентский сокет
		private Socket clientSocket;
		// входящее сообщение
		private Scanner inMessage;
		// исходящее сообщение
		private PrintWriter outMessage;
		private JLabel jlNumberOfClients;
		public Client() {
		
		try {
			// подключаемся к серверу
			clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
			inMessage = new Scanner(clientSocket.getInputStream());
			outMessage = new PrintWriter(clientSocket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
		clientWindow = new ClientWindow(this);
		performWork();
	}
	
	public void performWork() {
	while (true) {
			try {
				// бесконечный цикл
				while (true) {
					// если есть входящее сообщение
					if (inMessage.hasNext()) {
						// считываем его
						String inMes = inMessage.nextLine();
						String clientsInChat = "Клиентов в чате = ";
						if (inMes.indexOf(clientsInChat) == 0) {
							jlNumberOfClients.setText(inMes);
							clientWindow.displayMessage("Empty message");
						} else {
							clientWindow.displayMessage(inMes + "\n");
						}
					}
				}
			} catch (Exception e) {
			}
		}
}

	public void performSendingMessage(String messageStr) {
		outMessage.println(messageStr);
		System.out.println(messageStr);
		outMessage.flush();
		//jtfMessage.setText("");
		
	}	

}
