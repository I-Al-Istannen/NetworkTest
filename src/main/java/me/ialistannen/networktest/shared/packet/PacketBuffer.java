package me.ialistannen.networktest.shared.packet;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.common.base.Preconditions;

/**
 * A special byte outputStream
 * <p>
 * <em>
 * All methods will produce undefined results if different data is stored. This means you need to know the protocol.
 * </em>
 *
 * @author I Al Istannen
 */
public class PacketBuffer {
    private static final int GROW_BY_AMOUNT = 64;
    private ByteBuffer buffer = ByteBuffer.allocate(GROW_BY_AMOUNT);

    /**
     * @param anInt The int to add
     *
     * @return This buffer
     */
    public PacketBuffer putInt(int anInt) {
        ensureCapacity(4);
        buffer.putInt(anInt);
        return this;
    }


    /**
     * @param aByte The byte to add
     *
     * @return This buffer
     */
    public PacketBuffer putByte(byte aByte) {
        ensureCapacity(1);
        buffer.put(aByte);

        return this;
    }

    /**
     * @param bytes The bytes to add
     *
     * @return This buffer
     */
    public PacketBuffer putBytes(byte[] bytes) {
        ensureCapacity(bytes.length);
        buffer.put(bytes);
        return this;
    }

    /**
     * @param string The String to add
     *
     * @return This buffer
     */
    public PacketBuffer putString(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        putInt(bytes.length);
        putBytes(bytes);

        return this;
    }

    /**
     * @return A read int
     */
    public int getInt() {
        return buffer.getInt();
    }

    /**
     * @param data The data to read
     *
     * @return The same array. Populated
     *
     * @throws BufferUnderflowException if not enough bytes are in this buffer
     */
    public byte[] getBytes(byte[] data) {
        buffer.get(data);
        return data;
    }

    /**
     * Reads a String from the buffer.
     *
     * @return The read String
     *
     * @throws BufferUnderflowException if not enough bytes are in this buffer
     */
    public String getString() {
        byte[] bytes = new byte[getInt()];
        return new String(getBytes(bytes), StandardCharsets.UTF_8);
    }

    /**
     * @return The buffer as a byte array. The array is from [0;position]
     */
    public byte[] asArray() {
        // TODO: 24.02.2017 Return the whole packet? 
        int position = position();
        byte[] data = new byte[position];

        rewind();

        getBytes(data);
        buffer.position(position);

        return data;
    }

    /**
     * Returns a part of the buffer as an array.
     * <p>
     * Similar to {@link Arrays#copyOfRange(byte[], int, int)}
     *
     * @param start The start position
     * @param end The end position
     *
     * @return The byte array between start and end
     */
    public byte[] rangeAsArray(int start, int end) {
        Preconditions.checkElementIndex(start, limit(), "start must be inside [0;limit()]");
        Preconditions.checkElementIndex(end, limit(), "(end) must be inside [0;limit()]");
        int oldPosition = position();

        setPosition(start);
        byte[] tmp = new byte[end - start];
        getBytes(tmp);

        setPosition(oldPosition);

        return tmp;
    }


    /**
     * Rewinds this buffer.  The position is set to zero and the mark is
     * discarded.
     * <p>
     * <p> Invoke this method before a sequence of channel-write or <i>get</i>
     * operations, assuming that the limit has already been set
     * appropriately.  For example:
     * <p>
     * <pre>
     * out.write(buf);    // Write remaining data
     * buf.rewind();      // Rewind buffer
     * buf.get(array);    // Copy data into array</pre>
     *
     * @return This buffer
     */
    public PacketBuffer rewind() {
        buffer.rewind();
        return this;
    }

    /**
     * Returns this buffer's position.
     *
     * @return The position of this buffer
     */
    public int position() {
        return buffer.position();
    }

    /**
     * Sets this buffer's position.  If the mark is defined and larger than the
     * new position then it is discarded.
     *
     * @param newPosition The new position value; must be non-negative
     * and no larger than the current limit
     *
     * @return This buffer
     *
     * @throws IllegalArgumentException If the preconditions on <tt>newPosition</tt> do not hold
     */
    public PacketBuffer setPosition(int newPosition) {
        buffer.position(newPosition);
        return this;
    }

    /**
     * Returns this buffer's limit.
     *
     * @return The limit of this buffer
     */
    public int limit() {
        return buffer.limit();
    }

    /**
     * Clears this buffer.  The position is set to zero, the limit is set to
     * the capacity, and the mark is discarded.
     * <p>
     * <p> Invoke this method before using a sequence of channel-read or
     * <i>put</i> operations to fill this buffer.  For example:
     * <p>
     * <pre>
     * buf.clear();     // Prepare buffer for reading
     * in.read(buf);    // Read data</pre>
     * <p>
     * <p> This method does not actually erase the data in the buffer, but it
     * is named as if it did because it will most often be used in situations
     * in which that might as well be the case. </p>
     *
     * @return This buffer
     */
    public PacketBuffer clear() {
        buffer.clear();
        return this;
    }

    /**
     * Returns the number of elements between the current position and the
     * limit.
     *
     * @return The number of elements remaining in this buffer
     */
    public int remaining() {
        return buffer.remaining();
    }

    /**
     * Returns this buffer's capacity.
     *
     * @return The capacity of this buffer
     */
    public int capacity() {
        return buffer.capacity();
    }

    private void ensureCapacity(int min) {
        if (buffer.remaining() < min) {
            ByteBuffer tmp = ByteBuffer.allocate(buffer.capacity() + min + GROW_BY_AMOUNT);

            byte[] tmpArray = new byte[buffer.position()];
            buffer.rewind();
            buffer.get(tmpArray);

            tmp.put(tmpArray);
            buffer = tmp;
        }
    }
}
