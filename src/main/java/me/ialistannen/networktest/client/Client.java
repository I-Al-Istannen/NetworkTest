package me.ialistannen.networktest.client;

import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.server.Server;
import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.EventManager;
import me.ialistannen.networktest.shared.event.EventManager.State;
import me.ialistannen.networktest.shared.event.IPacketEvent;
import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.shared.packet.protocol.PacketMapper;
import me.ialistannen.networktest.util.RunnableUtil;

/**
 * The exposed Client
 *
 * @author I Al Istannen
 */
public class Client {

    private final EventManager eventManager = new EventManager();
    private PacketMapper packetMapper;

    private ClientConnectionThread clientConnectionThread;

    private EventFactory eventFactory;

    /**
     * Creates a new {@link Client} and starts listening
     *
     * @param socket The {@link Socket}. It should already be opened. I will <em>not</em> open it for you...
     * @param packetMapper The {@link PacketMapper} to use
     * @param eventFactory The {@link EventFactory} to use
     */
    public Client(Socket socket, PacketMapper packetMapper, EventFactory eventFactory) {
        this.packetMapper = Objects.requireNonNull(packetMapper, "packetMapper can not be null!");
        this.eventFactory = Objects.requireNonNull(eventFactory, "eventFactory can not be null!");

        clientConnectionThread = new ClientConnectionThread(socket, this);

        clientConnectionThread.start();
    }

    /**
     * Returns the {@link Client}'s {@link EventManager}
     *
     * @return The {@link EventManager}
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Checks if the {@link Client} is connected to a {@link Server}
     *
     * @return True if the {@link Client} is most likely connected to a {@link Server}
     */
    public boolean isConnected() {
        return clientConnectionThread.isAlive();
    }

    /**
     * Sends a Packet
     *
     * @param packet The {@link Packet} to send
     *
     * @throws IllegalStateException if the connection thread to the server is dead ({@link #isConnected()})
     */
    public void sendPacket(Packet packet) {
        Preconditions.checkState(isConnected(), "The connection thread is dead.");

        IPacketEvent packetEvent = eventFactory.create(
                packet, Client.this, Direction.TO_SERVER, State.FILTER
        );
        getEventManager().postEvent(packetEvent, State.FILTER);

        if (packetEvent instanceof PacketEvent && ((PacketEvent) packetEvent).isCancelled()) {
            return;
        }

        getEventManager().postEvent(
                eventFactory.create(
                        packet, Client.this, Direction.TO_SERVER, State.LISTEN
                ),
                State.LISTEN
        );

        clientConnectionThread.sendPacket(packet);
    }

    /**
     * @return The {@link PacketMapper} of the client
     */
    PacketMapper getPacketMapper() {
        return packetMapper;
    }

    /**
     * Handles the reading of some data
     *
     * @param buffer The read buffer. NOT rewinded.
     */
    void handleRead(PacketBuffer buffer) {
        try {
            int packetId = buffer.getInt();
            Optional<Class<? extends Packet>> classOptional = packetMapper.getPacketClass(packetId);
            if (!classOptional.isPresent()) {
                System.err.println("Unknown packet id: " + packetId);
                return;
            }

            Class<? extends Packet> packetClass = classOptional.get();

            RunnableUtil.doUnchecked(() -> {
                Packet packet = packetClass.newInstance();
                packet.load(buffer);

                {
                    IPacketEvent packetEvent = eventFactory.create(
                            packet, Client.this, Direction.TO_CLIENT, State.FILTER
                    );
                    getEventManager().postEvent(packetEvent, State.FILTER);

                    if (packetEvent instanceof PacketEvent && ((PacketEvent) packetEvent).isCancelled()) {
                        return;
                    }

                    getEventManager().postEvent(
                            eventFactory.create(
                                    packet, Client.this, Direction.TO_CLIENT, State.LISTEN
                            ),
                            State.LISTEN
                    );
                }
            });
        } catch (Exception e) {
            System.err.println("An error occurred reading a packet");
            e.printStackTrace();
        }
    }
}
