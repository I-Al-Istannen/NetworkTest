package me.ialistannen.testchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;

import me.ialistannen.networktest.server.Server;
import me.ialistannen.networktest.shared.event.EventManager.State;
import me.ialistannen.testchat.server.events.ReceiveNickNameEvent;
import me.ialistannen.testchat.shared.event.events.ReceiveChatMessageEvent;
import me.ialistannen.testchat.shared.packet.ChatPacketMapperFactory;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;

/**
 * A simple Chat server
 *
 * @author I Al Istannen
 */
public class ChatServer extends Server<ConnectedChatClient> {

    /**
     * Creates a new ChatServer
     *
     * @param port The port
     *
     * @throws IOException if any error occurs while creating the {@link ServerSocket}
     */
    public ChatServer(int port) throws IOException {
        super(
                ChatPacketMapperFactory.getMapper(),
                (chatClientServer, socket) -> new ConnectedChatClient(),
                new ServerEventFactory(),
                port
        );

        getEventManager().register(State.LISTEN, this);
    }

    @Subscribe
    private void onReceiveNick(ReceiveNickNameEvent event) {
        System.out.println("Got nick: " + event.getNickName());

        ConnectedChatClient connectedChatClient = (ConnectedChatClient) event.getSource();
        connectedChatClient.setNick(event.getNickName());

        connectedChatClient.sendPacket(new PacketChatMessage("Nicked you as: \"" + event.getNickName() + "\""));
        broadcastPacket(new PacketChatMessage("Got a nick: " + event.getNickName()));
    }

    @Subscribe
    private void onReceiveChatMessage(ReceiveChatMessageEvent event) {
        System.out.println("Got: " + event.getMessage());

        ConnectedChatClient client = (ConnectedChatClient) event.getSource();
        String message = "<" + client.getNick() + "> " + event.getMessage();

        broadcastPacket(new PacketChatMessage(message));
    }

    public static void main(String[] args) throws Exception {
        ChatServer server = new ChatServer(12345);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                server.broadcastPacket(
                        new PacketChatMessage(
                                "Hey, server is here: "
                                        + LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                );
            }
        }, 0, 5000);
    }
}
