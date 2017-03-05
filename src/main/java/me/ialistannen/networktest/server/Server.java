package me.ialistannen.networktest.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.EventManager;
import me.ialistannen.networktest.shared.event.EventManager.State;
import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.identification.ID;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.shared.packet.PacketDecodingException;
import me.ialistannen.networktest.shared.packet.protocol.PacketMapper;
import me.ialistannen.networktest.util.ReflectionUtil;
import me.ialistannen.networktest.util.RunnableUtil;

/**
 * The exposed Server
 *
 * @param <T> The type of the {@link ConnectedClient}
 *
 * @author I Al Istannen
 */
public class Server <T extends ConnectedClient> {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private EventManager eventManager;
    private PacketMapper packetMapper;

    private ServerListenerThread<T> serverListenerThread;

    private BiMap<T, ServerThread> clientToServerMap = HashBiMap.create();
    private BiMap<ServerThread, T> serverToClientMap = clientToServerMap.inverse();

    private EventFactory eventFactory;

    /**
     * Creates a new server <em>and starts listening</em>
     *
     * @param packetMapper The {@link PacketMapper} to use
     * @param clientFactory The Factory creating {@link ConnectedClient}s of type {@code <T>}
     * @param port The port of the server
     * @param eventFactory The {@link EventFactory} to use
     *
     * @throws IOException if the {@link ServerSocket} throws any
     */
    public Server(PacketMapper packetMapper, BiFunction<Server<T>, Socket, T> clientFactory,
                  EventFactory eventFactory, int port) throws IOException {

        this.packetMapper = Objects.requireNonNull(packetMapper, "packetMapper can not be null!");
        this.eventFactory = Objects.requireNonNull(eventFactory, "eventFactory can not be null!");
        this.eventManager = new EventManager();


        serverListenerThread = new ServerListenerThread<>(this, clientFactory, port, 1000);
        serverListenerThread.start();
    }

    /**
     * @return THe {@link Server}'s {@link EventManager}
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Returns all {@link ConnectedClient}s
     *
     * @return All the {@link ConnectedClient}s. Unmodifiable
     */
    public Collection<T> getClients() {
        return Collections.unmodifiableCollection(serverToClientMap.values());
    }

    /**
     * Searches a {@link ConnectedClient} by its {@link ID}.
     * <p>
     * No guarantees are made about the lookup speed at this moment.
     * <br>Currently it is O(n), which isn't great. The future will show if you need a better one.
     *
     * @param id The {@link ID} of the client
     *
     * @return The client, if any
     */
    public Optional<T> getClientById(ID id) {
        return getClients().stream().filter(t -> t.getID().equals(id)).findAny();
    }

    /**
     * Sends a {@link Packet} to all {@link ConnectedClient}s
     * <p>
     * Shorthand for {@code getClients().forEach(client -> client.sendPacket(packet));}
     *
     * @param packet The {@link Packet} to broadcast
     */
    public void broadcastPacket(Packet packet) {
        Objects.requireNonNull(packet, "packet can not be null!");

        getClients().forEach(client -> client.sendPacket(packet));
    }

    /**
     * Stops listening for new clients
     */
    public void stopListeningForClients() {
        serverListenerThread.shutdown();
    }

    /**
     * Removes a {@link ConnectedClient} and closes the connection if it is open
     *
     * @param client The {@link ConnectedClient} to remove
     */
    public void removeClient(ConnectedClient client) {
        // We have a "rawer" type in client, but the actual one at runtime should be the same
        @SuppressWarnings("SuspiciousMethodCalls")
        ServerThread removed = clientToServerMap.remove(client);

        if (removed != null) {
            removed.shutdown();
        }
    }

    /**
     * Checks if the client is still connected
     *
     * @param client The {@link ConnectedClient} to check
     *
     * @return True if the client is probably still connected
     */
    public boolean isConnected(T client) {
        return clientToServerMap.get(client).isAlive();
    }

    /**
     * @param serverThread The {@link ServerThread}
     *
     * @return The {@link ConnectedClient} for it
     */
    T getClient(ServerThread serverThread) {
        return serverToClientMap.get(serverThread);
    }

    /**
     * @return The {@link Server}'s {@link EventFactory}
     */
    EventFactory getEventFactory() {
        return eventFactory;
    }

    /**
     * @return The {@link PacketMapper} of the {@link Server}
     */
    PacketMapper getPacketMapper() {
        return packetMapper;
    }

    /**
     * Accepts a new client
     *
     * @param client The client
     * @param socket The Socket the client connected with
     */
    void acceptClient(T client, Socket socket) {
        ServerThread serverThread = new ServerThread(socket, 50, this);
        client.setServerThread(serverThread);

        clientToServerMap.put(client, serverThread);

        serverThread.start();
    }

    /**
     * Handles a read in a server thread
     *
     * @param packetBuffer The read {@link PacketBuffer}
     */
    void handleRead(PacketBuffer packetBuffer, ServerThread serverThread) {
        int packetId = packetBuffer.getInt();
        Optional<Class<? extends Packet>> classOptional = packetMapper.getPacketClass(packetId);

        if (!classOptional.isPresent()) {
            LOGGER.log(Level.WARNING, "Unknown packet ID: {}", packetId);
            return;
        }

        Class<? extends Packet> packetClass = classOptional.get();

        try {
            RunnableUtil.doUnchecked(() -> {
                Packet packet = ReflectionUtil.newInstance(packetClass);
                packet.load(packetBuffer);

                T client = serverToClientMap.get(serverThread);

                {
                    PacketEvent<?> packetEvent = eventFactory.create(packet, client, Direction.TO_SERVER, State.FILTER);
                    getEventManager().postEvent(packetEvent, State.FILTER);

                    if (packetEvent.isCancelled()) {
                        return;
                    }

                    getEventManager().postEvent(
                            eventFactory.create(packet, client, Direction.TO_SERVER, State.LISTEN),
                            State.LISTEN
                    );
                }
            });
        } catch (RunnableUtil.ErrorInRunnableException e) {
            if (e.getCause() instanceof PacketDecodingException) {
                LOGGER.log(Level.WARNING, "Error decoding packet", e);
            }
            else {
                throw e;
            }
        }
    }
}
