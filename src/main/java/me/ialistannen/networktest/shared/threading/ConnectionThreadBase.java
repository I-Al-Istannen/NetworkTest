package me.ialistannen.networktest.shared.threading;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.util.RunnableUtil;

/**
 * The base for a {@link Thread} connecting Client and the Server
 *
 * @author I Al Istannen
 */
public abstract class ConnectionThreadBase extends Thread {

    protected final int SOCKET_TIMEOUT_MILLIS;

    protected final Socket socket;

    protected final AtomicBoolean running = new AtomicBoolean(true);
    protected final Queue<PacketBuffer> writeQueue = new ConcurrentLinkedDeque<>();

    /**
     * Creates a new {@link ConnectionThreadBase}
     *
     * @param name The name of the {@link Thread}
     * @param socket The {@link Socket}. It should already be opened. I will <em>not</em> open it for you...
     * @param socketTimeoutMinutes The timeout of the socket in milliseconds
     */
    protected ConnectionThreadBase(String name, Socket socket, int socketTimeoutMinutes) {
        super(name);

        // won't catch everything (opened, then closed again) but it is good enough for now
        Preconditions.checkArgument(socket.isConnected(), "socket needs to be connected");

        this.socket = socket;
        this.SOCKET_TIMEOUT_MILLIS = socketTimeoutMinutes;
    }

    /**
     * Sends a {@link Packet} to the client
     *
     * @param packet The {@link Packet} to send. <em>NOT rewinded!</em>
     */
    public void sendPacket(Packet packet) {
        PacketBuffer buffer = new PacketBuffer();
        buffer.putInt(getPacketID(packet));
        packet.save(buffer);

        // Write Packet length
        int length = buffer.position();
        PacketBuffer finalBuffer = new PacketBuffer();
        finalBuffer.putInt(length);
        finalBuffer.putBytes(buffer.asArray());

        writeQueue.add(finalBuffer);
    }

    /**
     * Closes the socket and ends this thread
     */
    public void shutdown() {
        running.set(false);
    }

    /**
     * Returns the ID of a packet
     *
     * @param packet The {@link Packet} to get the ID for
     *
     * @return The ID of the {@link Packet}
     */
    protected abstract int getPacketID(Packet packet);

    /**
     * Handles reading something from the socket
     *
     * @param buffer The read {@link PacketBuffer}. <em>Not rewinded!</em>
     */
    protected abstract void handleRead(PacketBuffer buffer);

    private List<PacketBuffer> slicePacketIfNeeded(PacketBuffer buffer) {
        List<PacketBuffer> buffers = new ArrayList<>();

        int currentBufferPosition = buffer.position();

        // go back to the start, to be able to read the first size
        buffer.rewind();

        for (int startPos = 0; startPos < currentBufferPosition - 4; ) {
            PacketBuffer tmp = new PacketBuffer();

            // read the size of the packet (excluding the int for the size)
            int size = buffer.getInt();
            // Go past the size int.
            // We do not want that in our delivered data. It is just for us to split them correctly
            startPos += 4;

            // Add all bytes of the packet to the new buffer.
            tmp.putBytes(buffer.rangeAsArray(startPos, startPos + size));

            startPos += size;
            // advance the position for buffer#getInt to return the next size.
            // The methods above didn't change the position of the buffer.
            buffer.setPosition(startPos);

            buffers.add(tmp);
        }

        return buffers;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            socket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);

            while (running.get() && !isInterrupted()) {
                PacketBuffer readBuffer = new PacketBuffer();

                RunnableUtil.doUncheckedAndSwallow(() -> {
                    int tmp;
                    while ((tmp = inputStream.read()) != -1) {
                        readBuffer.putByte((byte) tmp);
                    }
                }, SocketTimeoutException.class);

                if (readBuffer.position() > 0) {
                    for (PacketBuffer buffer : slicePacketIfNeeded(readBuffer)) {
                        handleRead(buffer);
                    }
                }

                if (!writeQueue.isEmpty()) {
                    synchronized (writeQueue) {
                        for (PacketBuffer buffer : writeQueue) {
                            outputStream.write(buffer.asArray());
                            outputStream.flush();
                        }
                        writeQueue.clear();
                    }
                }
            }

        } catch (RuntimeException e) {
            if (e.getCause() instanceof SocketException) {
                // TODO: 03.03.2017 Error handling 
                System.err.println("The socket had a problem");
                e.printStackTrace();
                shutdown();
            }
            else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        } finally {
            RunnableUtil.doUnchecked(socket::close);
            System.out.println("I died!");
        }
    }

}