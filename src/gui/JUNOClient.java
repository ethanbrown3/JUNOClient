/**
 * JUNOClient
 * @author Ethan Brown
 * CS 3230
 * Apr 12, 2017
 */
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import junoServer.Protocol;
import junoServer.Receivable;

/**
 * @author Ethan
 *
 */
public class JUNOClient extends JFrame implements Receivable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2227017723330822281L;

	private JPanel contentPane;
	private JTextArea chatArea;
	private JTextArea chatInputArea;
	private Protocol protocol;
	private String username;
	private boolean userSet = false;

	/**
	 * 
	 */
	public JUNOClient() {
		try {
			protocol = new Protocol(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		intializeGameArea();
		initializeChat();

	}

	private void intializeGameArea() {
		JPanel gamePane = new JPanel(new BorderLayout());
		JButton startButton = new JButton("Start Game");
		gamePane.add(startButton, "Center");
		contentPane.add(gamePane, "East");
		
	}

	private void initializeChat() {
		// center panel
		JPanel chatPane = new JPanel(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		JScrollPane chatScroll = new JScrollPane(chatArea);
		chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatPane.add(chatScroll, "Center");

		// south panel
		JPanel inputPanel = new JPanel(new FlowLayout());
		chatInputArea = new JTextArea(3, 15);
		chatInputArea.setEditable(true);
		chatInputArea.setLineWrap(true);
		chatInputArea.setWrapStyleWord(true);
		JScrollPane inputScroll = new JScrollPane(chatInputArea);
		inputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		inputPanel.add(inputScroll);
		chatInputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == KeyEvent.CTRL_MASK) {
					sendChat();
				}
			}
		});
		this.setVisible(true);
		
		// send button setup
		JButton send = new JButton("Send");
		inputPanel.add(send);
		send.addActionListener(e -> sendChat());
			
		chatPane.add(inputPanel, "South");
		contentPane.add(chatPane, "West");
		chatPane.setVisible(true);
		setVisible(true);
		chatInputArea.requestFocusInWindow();
	}

	private void sendChat() {
		System.out.println("executed sendText()");
		String chatSend;
		chatSend = chatInputArea.getText();
		JSONObject message = new JSONObject();
		message.put("type", "chat");
		message.put("message", chatSend);
		protocol.sendMessage(message);
		chatArea.append(username + ": " + chatSend + "\n");
		chatInputArea.setText("");
	}

	private void handleChat(JSONObject m) {
		JSONObject message = m;
		System.out.println(message.toString());
		chatArea.append(message.getString("fromUser") + ": " + message.getString("message") + "\n");
	}
	
	@Override
	public void setUsername(String user) {
		if (!userSet) {
			this.username = user;
		}
	}

	@Override
	public void giveMessage(JSONObject m) {
		JSONObject message = m;

		if (message.getString("type").equals("chat")) {
			handleChat(message);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JUNOClient client = new JUNOClient();
	}

}
