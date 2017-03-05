package me.ialistannen.networktest.shared.packet;

/**
 * A Packet
 * <p>
 * <u><em><strong>Every subclass needs to have a nullary constructor (without parameters)</strong></em></u>
 * <br>You can make it private though, but it needs to be there.
 */
public interface Packet {

    /**
     * Loads the packet from a buffer
     *
     * @param buffer The buffer to load from
     *
     * @throws PacketDecodingException if an error occurred decoding the packet
     */
    void load(PacketBuffer buffer);

    /**
     * Saves the packet to a buffer
     *
     * @param buffer The buffer to save to
     */
    void save(PacketBuffer buffer);
}
