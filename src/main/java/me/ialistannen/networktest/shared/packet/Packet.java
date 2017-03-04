package me.ialistannen.networktest.shared.packet;

/**
 * A Packet
 */
public abstract class Packet {

    /**
     * We'd need to enforce this default constructor. Sadly Java has no way for that.
     */
    protected Packet() {

    }

    /**
     * Loads the packet from a buffer
     *
     * @param buffer The buffer to load from
     *
     * @throws PacketDecodingException if an error occurred decoding the packet
     */
    public abstract void load(PacketBuffer buffer);

    /**
     * Saves the packet to a buffer
     *
     * @param buffer The buffer to save to
     */
    public abstract void save(PacketBuffer buffer);
}
