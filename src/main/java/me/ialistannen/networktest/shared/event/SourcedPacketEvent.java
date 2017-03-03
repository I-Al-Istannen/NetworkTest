package me.ialistannen.networktest.shared.event;

import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A {@link IPacketEvent} with a source
 *
 * @author I Al Istannen
 */
class SourcedPacketEvent implements IPacketEvent {
    private Object source;
    private Direction direction;

    /**
     * @param source The source object
     * @param direction The {@link Direction} of the {@link Packet}
     */
    SourcedPacketEvent(Object source, Direction direction) {
        this.source = source;
        this.direction = direction;
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
}
