package me.ialistannen.testchat.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.Socket;

import com.google.common.eventbus.Subscribe;

import me.ialistannen.networktest.client.Client;
import me.ialistannen.networktest.shared.event.EventManager.State;
import me.ialistannen.testchat.server.ChatServer;
import me.ialistannen.testchat.shared.event.events.ReceiveChatMessageEvent;
import me.ialistannen.testchat.shared.packet.ChatPacketMapperFactory;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;
import me.ialistannen.testchat.shared.packet.packets.PacketSetNickName;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * A client for the {@link ChatServer}
 *
 * @author I Al Istannen
 */
public class ChatClient {

    private JTextArea textArea;

    private Client client;

    /**
     * @param port The port of the server
     * @param hostname The hostname of the server
     */
    public ChatClient(int port, String hostname) {
        client = new Client(getSocket(port, hostname), ChatPacketMapperFactory.getMapper(), new ClientEventFactory());

        client.getEventManager().register(State.LISTEN, this);


        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setMargin(new Insets(5, 5, 5, 5));

        JTextField textField = new JTextField();
        textField.addActionListener(e -> {
            if(textField.getText().equalsIgnoreCase(".quit")) {
                System.exit(0);
            }
            client.sendPacket(new PacketChatMessage(textField.getText()));
            textField.setText("");
        });

        contentPane.add(textArea, BorderLayout.CENTER);
        contentPane.add(textField, BorderLayout.SOUTH);

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        {
            JDialog dialog = new JDialog(frame, "Choose a Nickname", true);
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            
            dialog.setLayout(new GridLayout(1,2));
            dialog.add(new JLabel("Nickname: "));
            JTextField field = new JTextField();
            field.addActionListener(e -> {
                client.sendPacket(new PacketSetNickName(field.getText()));
                dialog.setVisible(false);
            });
            dialog.add(field);
            field.setMargin(new Insets(0, 10, 0, 0));
            
            dialog.setSize(250, 90);
            dialog.setVisible(true);
        }
    }

    @Subscribe
    private void onReceiveChatMessage(ReceiveChatMessageEvent event) {
        textArea.append(event.getMessage() + "\n");
    }

    private Socket getSocket(int port, String hostname) {
        Socket socket;
        try {
            socket = new Socket(hostname, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return socket;
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient(12345, "localhost");
    }
}
