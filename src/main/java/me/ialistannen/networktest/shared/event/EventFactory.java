package me.ialistannen.networktest.shared.event;

import me.ialistannen.networktest.shared.event.EventManager.State;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A factory for {@link PacketEvent}s
 *
 * @author I Al Istannen
 */
@FunctionalInterface
public interface EventFactory {

    /**
     * @param packet The Packet
     * @param source The source of the packet
     * @param direction The {@link Direction} it is travelling
     * @param state The current {@link State}
     *
     * @return The created {@link PacketEvent}
     */
    PacketEvent create(Packet packet, Object source, Direction direction, State state);
}
