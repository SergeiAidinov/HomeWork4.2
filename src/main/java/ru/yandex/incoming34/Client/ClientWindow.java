package ru.yandex.incoming34.Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ClientWindow extends JFrame {
	// ссылка для привязки графического окна к клиенту
	final Client client;

	// следующие поля отвечают за элементы формы
	private JTextField messageField;
	private JTextField nameField;
	private JTextArea jtaTextAreaMessage;
	private JLabel jlNumberOfClients;
	private boolean isFirstMessage = true;
	// Scanner inMessage;
	// имя клиента
	//private String clientName = null;

	// получаем имя клиента
	/*
	public String getClientName() {
		return this.clientName;
	}
	*/

	// конструктор
	public ClientWindow(final Client client) {
		this.client = client;
		// Задаём настройки элементов на форме
		setBounds(600, 300, 600, 500);
		setTitle("Client");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jtaTextAreaMessage = new JTextArea();
		jtaTextAreaMessage.setEditable(false);
		jtaTextAreaMessage.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
		add(jsp, BorderLayout.CENTER);
		// label, который будет отражать количество клиентов в чате
		jlNumberOfClients = new JLabel("Количество клиентов в чате: ");
		add(jlNumberOfClients, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel(new BorderLayout());
		add(bottomPanel, BorderLayout.SOUTH);
		JButton buttonSend = new JButton("Отправить");
		bottomPanel.add(buttonSend, BorderLayout.EAST);
		messageField = new JTextField("Введите ваше сообщение: ");
		bottomPanel.add(messageField, BorderLayout.CENTER);
		nameField = new JTextField("Введите ваше имя: ");
		bottomPanel.add(nameField, BorderLayout.WEST);
		setVisible(true);
		// обработчик события нажатия кнопки отправки сообщения
		buttonSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// если имя клиента, и сообщение непустые, то отправляем сообщение
				if (!messageField.getText().trim().isEmpty()) {
					// clientName = nameField.getText();
					sendMessage();
					// фокус на текстовое поле с сообщением
					messageField.grabFocus();
				}
			}
		});
		// при фокусе поле сообщения очищается
		messageField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				messageField.setText("");
			}
		});
		// при фокусе поле имя очищается
		nameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				nameField.setText("");
			}
		});
		// в отдельном потоке начинаем работу с сервером

		// добавляем обработчик события закрытия окна клиентского приложения
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				// здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
				if (client.getClientName().length() != 0 && !client.getClientName().equals("Введите ваше имя: ")) {
					sendFinalMessage(client.getClientName() + " вышел из чата!");
				} else {
					sendFinalMessage("Участник вышел из чата, так и не представившись!");
				}
				// отправляем служебное сообщение, которое является признаком того, что клиент
				// вышел из чата

				client.endSession();

			}

		});

		// отображаем форму

	}

	private void sendFinalMessage(String string) {
		client.performSendingMessage("END" + string);

	}

	// отправка сообщения
	public void sendMessage() {
		if (isFirstMessage) {
			client.defineClientName(nameField.getText());
			isFirstMessage = false;
		}
		// формируем сообщение для отправки на сервер
		String messageStr = client.getClientName() + ": " + messageField.getText();
		// отправляем сообщение
		client.performSendingMessage("RPL" + messageStr);
		messageField.setText("");
	}

	public void displayMessage(String string) {
		// TODO Auto-generated method stub
		jtaTextAreaMessage.append(string);
	}

	public void dislpayQuantityOfClientsInChat(String inMes) {
		jlNumberOfClients.setText(inMes);

	}

	public void freezeNameOfClient(String name) {
		nameField.setText(name);
		nameField.setEditable(false);
		
	}

	
}
