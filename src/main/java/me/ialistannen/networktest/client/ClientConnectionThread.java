package me.ialistannen.networktest.client;

import java.net.Socket;
import java.util.Objects;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.shared.threading.ConnectionThreadBase;

/**
 * The {@link Thread} holding a connection to the server
 *
 * @author I Al Istannen
 */
class ClientConnectionThread extends ConnectionThreadBase {

    private final Client client;

    /**
     * Creates a new {@link ClientConnectionThread}
     *
     * @param socket The {@link Socket}. It should already be opened. I will <em>not</em> open it for you...
     * @param client The {@link Client}
     */
    ClientConnectionThread(Socket socket, Client client) {
        super("Client Connection thread", socket, 50);

        // won't catch everything (opened, then closed again) but it is good enough for now
        Preconditions.checkArgument(socket.isConnected(), "socket needs to be connected");

        this.client = Objects.requireNonNull(client, "client can not be null!");
    }

    @Override
    protected void handleRead(PacketBuffer buffer) {
        client.handleRead(buffer.rewind());
    }

    @Override
    protected int getPacketID(Packet packet) {
        return client.getPacketMapper()
                .getPacketID(packet.getClass())
                .orElseThrow(() -> new RuntimeException("Unknown packet class: " + packet.getClass().getName()));
    }
}
