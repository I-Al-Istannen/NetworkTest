package me.ialistannen.networktest.server;

import java.net.Socket;
import java.util.Objects;

import me.ialistannen.networktest.client.Client;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.shared.threading.ConnectionThreadBase;

/**
 * The {@link Thread} serving a {@link Client}
 *
 * @author I Al Istannen
 */
class ServerThread extends ConnectionThreadBase {

    private Server<? extends ConnectedClient> server;

    /**
     * Creates a new {@link ConnectionThreadBase}
     *
     * @param socket The {@link Socket}. It should already be opened. I will <em>not</em> open it for you...
     * @param socketTimeoutMillis The timeout of the socket in milliseconds
     */
    ServerThread(Socket socket, int socketTimeoutMillis, Server<? extends ConnectedClient> server) {
        super("Server connection thread: " + socket.getInetAddress(), socket, socketTimeoutMillis);

        this.server = Objects.requireNonNull(server, "server can not be null!");
    }

    /**
     * @return The {@link Server}
     */
    Server<? extends ConnectedClient> getServer() {
        return server;
    }

    /**
     * Handles reading something from the socket
     *
     * @param buffer The read {@link PacketBuffer}
     */
    @Override
    protected void handleRead(PacketBuffer buffer) {
        server.handleRead(buffer.rewind(), this);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        // Remove the lost client
        server.removeClient(server.getClient(this));
    }

    @Override
    protected int getPacketID(Packet packet) {
        return server.getPacketMapper()
                .getPacketID(packet.getClass())
                .orElseThrow(() -> new RuntimeException("Unknown packet class: " + packet.getClass().getName()));
    }
}
