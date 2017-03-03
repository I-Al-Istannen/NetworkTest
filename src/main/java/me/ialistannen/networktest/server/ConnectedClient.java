package me.ialistannen.networktest.server;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.EventManager;
import me.ialistannen.networktest.shared.event.IPacketEvent;
import me.ialistannen.networktest.shared.event.PacketEvent;
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
     * Sends a {@link Packet} and calls the needed events
     *
     * @param packet The {@link Packet} to send
     *
     * @throws IllegalStateException if the connection thread to the client is dead
     */
    public final void sendPacket(Packet packet) {
        Preconditions.checkState(serverThread.isAlive(), "The connection thread is dead.");

        EventFactory eventFactory = getServer().getEventFactory();
        EventManager eventManager = getServer().getEventManager();
        
        IPacketEvent event = eventFactory
                .create(packet, this, Direction.TO_CLIENT, EventManager.State.FILTER);

        eventManager.postEvent(event, EventManager.State.FILTER);

        if (event instanceof PacketEvent && ((PacketEvent) event).isCancelled()) {
            return;
        }

        eventManager.postEvent(
                eventFactory.create(packet, this, Direction.TO_CLIENT, EventManager.State.LISTEN),
                EventManager.State.LISTEN
        );

        serverThread.sendPacket(packet);
    }

    /**
     * Returns the server
     * <p>
     * <em>This method is only valid <strong>after</strong> the client was accepted by the server.</em>
     *
     * @return The {@link Server} this client is connected to
     */
    public Server<? extends ConnectedClient> getServer() {
        return serverThread.getServer();
    }

    /**
     * Returns the client's unique {@link ID}
     *
     * @return The {@link ID} of this client
     */
    abstract public ID getID();

    /**
     * Sets the {@link ServerThread}
     *
     * @param serverThread The new {@link ServerThread}
     */
    void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }
}
