package me.ialistannen.testchat.shared.packet.packets;

import java.util.Objects;

import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.networktest.shared.packet.PacketBuffer;
import me.ialistannen.networktest.shared.packet.PacketDecodingException;

/**
 * A packet transmitting a single String
 *
 * @author I Al Istannen
 */
public class PacketString extends Packet {

    private String value;

    /**
     * Forced by {@link Packet}
     */
    protected PacketString() {

    }

    /**
     * @param value The value of the {@link PacketString}
     */
    public PacketString(String value) {
        this.value = Objects.requireNonNull(value, "value can not be null!");
    }

    /**
     * @return The String value
     */
    public String getValue() {
        return value;
    }

    /**
     * Loads the packet from a buffer
     *
     * @param buffer The buffer to load from
     *
     * @throws PacketDecodingException if an error occurred decoding the packet
     */
    @Override
    public void load(PacketBuffer buffer) throws PacketDecodingException {
        try {
            value = buffer.getString();
        } catch (Exception e) {
            throw new PacketDecodingException("Error reading the string", e);
        }
    }

    /**
     * Saves the packet to a buffer
     *
     * @param buffer The buffer to save to
     */
    @Override
    public void save(PacketBuffer buffer) {
        buffer.putString(value);
    }

    @Override
    public String toString() {
        return "PacketString{" +
                "value='" + value + '\'' +
                '}';
    }
}
