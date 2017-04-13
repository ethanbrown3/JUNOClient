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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		initializeChat();
		intializeGameArea();
		
	}

	private void intializeGameArea() {
		
		
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
				chatInputArea = new JTextArea(3, 25);
				chatInputArea.setEditable(true);
				chatInputArea.setLineWrap(true);
				chatInputArea.setWrapStyleWord(true);
				JScrollPane inputScroll = new JScrollPane(chatInputArea);
				inputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				inputPanel.add(inputScroll);
				chatInputArea.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == KeyEvent.CTRL_MASK) {
							sendChat();
						}
					}

					@Override
					public void keyTyped(KeyEvent e) {
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}
				});
				this.setVisible(true);
				// send button setup
				JButton send = new JButton("Send");
				inputPanel.add(send);
				send.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendChat();
					}
				});

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
		chatArea.setText(chatSend);
		chatInputArea.setText("");
	
	}

	@Override
	public void giveMessage(JSONObject message) {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JUNOClient client = new JUNOClient();
	}

}
