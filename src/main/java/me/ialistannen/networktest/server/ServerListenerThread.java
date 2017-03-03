package me.ialistannen.networktest.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import me.ialistannen.networktest.util.RunnableUtil;

/**
 * The Thread listening on the {@link ServerSocket} and creating {@link ServerThread}s
 *
 * @author I Al Istannen
 */
class ServerListenerThread <T extends ConnectedClient> extends Thread {

    private final Server<T> server;
    private final ServerSocket serverSocket;
    private final BiFunction<Server<T>, Socket, T> clientFactory;

    private final int SOCKET_TIMEOUT_MILLIS;
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Creates a new {@link ServerListenerThread}
     *
     * @param server The {@link Server} instance
     * @param clientFactory The Factory creating {@link ConnectedClient}s of type {@code <T>}
     * @param port The port of the server
     * @param socketTimeoutMillis The timeout of the {@link ServerSocket} in Milliseconds
     *
     * @throws IOException if {@link ServerSocket#ServerSocket(int)} throws one
     */
    ServerListenerThread(Server<T> server, BiFunction<Server<T>, Socket, T> clientFactory,
                         int port, int socketTimeoutMillis)
            throws IOException {

        this.server = Objects.requireNonNull(server, "server can not be null!");
        this.clientFactory = Objects.requireNonNull(clientFactory, "clientFactory can not be null!");
        this.SOCKET_TIMEOUT_MILLIS = socketTimeoutMillis;

        serverSocket = new ServerSocket(port);
    }

    /**
     * Stops waiting and closes the serverSocket
     */
    void shutdown() {
        running.set(false);
    }

    @Override
    public void run() {
        try {
            serverSocket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);

            while (running.get() && !isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();

                    T connectedClient = clientFactory.apply(server, socket);
                    server.acceptClient(connectedClient, socket);

                } catch (SocketTimeoutException ignored) {
                    // Okay. We will try again!
                }
            }

        } catch (IOException e) {
            System.err.println("An I/O error occurred."
                    + " This probably means the client closed the connection or the network has problems");

            e.printStackTrace();
            shutdown();
        } finally {
            RunnableUtil.doUnchecked(serverSocket::close);
        }
    }
}
