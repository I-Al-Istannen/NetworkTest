package me.ialistannen.networktest.shared.event;

import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A general {@link IPacketEvent}
 *
 * @author I Al Istannen
 */
public class PacketEvent extends SourcedPacketEvent {
    private Packet packet;
    private boolean cancelled;

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public PacketEvent(Object source, Packet packet, Direction direction) {
        super(source, direction);
        this.packet = packet;
    }

    /**
     * @return The Packet
     */
    public Packet getPacket() {
        return packet;
    }

    /**
     * @return True if the events is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled True if the events should be cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return "PacketEvent{" +
                "packet=" + packet +
                ", cancelled=" + cancelled +
                ", direction=" + getDirection() +
                '}';
    }
}
