package me.ialistannen.networktest.shared.event;

import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.PacketBuffer;

/**
 * A {@link IPacketEvent}, but containing the {@link PacketBuffer}
 *
 * @author I Al Istannen
 */
public class PacketBufferEvent extends SourcedPacketEvent {
    private PacketBuffer packetBuffer;

    /**
     * @param source The source object
     * @param packetBuffer The new {@link PacketBuffer}
     * @param direction The {@link Direction}
     */
    public PacketBufferEvent(Object source, PacketBuffer packetBuffer, Direction direction) {
        super(source, direction);
        this.packetBuffer = packetBuffer;
    }

    /**
     * Sets the new {@link PacketBuffer}
     *
     * @param packetBuffer The new {@link PacketBuffer}
     */
    public void setPacketBuffer(PacketBuffer packetBuffer) {
        this.packetBuffer = packetBuffer;
    }

    /**
     * Returns the {@link PacketBuffer}
     *
     * @return The {@link PacketBuffer}
     */
    public PacketBuffer getPacketBuffer() {
        return packetBuffer;
    }
}
