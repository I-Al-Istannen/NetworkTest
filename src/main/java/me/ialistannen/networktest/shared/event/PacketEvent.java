package me.ialistannen.networktest.shared.event;

import java.util.Objects;

import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A general {@link PacketEvent}
 *
 * @author I Al Istannen
 */
public class PacketEvent <T extends Packet> {

    private Object source;
    private Direction direction;

    private T packet;

    private boolean cancelled;

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public PacketEvent(Object source, T packet, Direction direction) {
        this.source = Objects.requireNonNull(source, "source can not be null!");
        this.direction = Objects.requireNonNull(direction, "direction can not be null!");
        this.packet = packet;
    }

    /**
     * @return The source
     */
    public Object getSource() {
        return source;
    }

    /**
     * @return The {@link Direction}
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return The Packet
     */
    public T getPacket() {
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
