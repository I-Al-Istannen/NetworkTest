package me.ialistannen.networktest.server;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.identification.ID;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A Client that is connected to the server
 *
 * @author I Al Istannen
 */
public abstract class ConnectedClient {

    private ServerThread serverThread;

    /**
     * Sends a {@link Packet} to the client and calls the needed events
     *
     * @param packet The {@link Packet} to send
     *
     * @throws IllegalStateException if the connection thread to the client is dead
     */
    public final void sendPacket(Packet packet) {
        Preconditions.checkState(serverThread.isAlive(), "The connection thread is dead.");

        if (getServer().callEvent(packet, this, Direction.TO_CLIENT)) {
            serverThread.sendPacket(packet);
        }
    }

    /**
     * Returns the server
     * <p>
     * <em>This method is only valid <strong>after</strong> the client was accepted by the server.</em>
     *
     * @return The {@link Server} this client is connected to
     */
    public Server<ConnectedClient> getServer() {
        return serverThread.getServer();
    }

    /**
     * Returns the client's unique {@link ID}
     *
     * @return The {@link ID} of this client
     */
    public abstract ID getId();

    /**
     * Sets the {@link ServerThread}
     *
     * @param serverThread The new {@link ServerThread}
     */
    void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }
}
