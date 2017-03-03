package me.ialistannen.testchat.shared.event;

import java.util.HashMap;
import java.util.Map;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.IPacketEvent;
import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.util.TriFunction;

import static me.ialistannen.networktest.shared.event.EventManager.State;

/**
 * The {@link EventFactory} for the server
 *
 * @author I Al Istannen
 */
public class PlaceholderEventFactory implements EventFactory {

    private Map<Class<? extends Packet>,
            TriFunction<Packet, Object, Direction, IPacketEvent>> packetFactories = new HashMap<>();

    /**
     * Adds a Factory for a {@link Packet}
     *
     * @param packetClass The {@link Class} of the {@link Packet} to add
     * @param factory The factory for it
     */
    public void addPacketFactory(Class<? extends Packet> packetClass,
                                 TriFunction<Packet, Object, Direction, IPacketEvent> factory) {
        packetFactories.put(packetClass, factory);
    }

    /**
     * @param packet The Packet
     * @param source The source of the packet
     * @param direction The {@link Direction} it is travelling
     * @param state The current {@link State}
     *
     * @return The created {@link IPacketEvent}
     */
    @Override
    public IPacketEvent create(Packet packet, Object source, Direction direction, State state) {
        if (!packetFactories.containsKey(packet.getClass())) {
            return new PacketEvent(source, packet, direction);
        }
        return packetFactories.get(packet.getClass()).apply(packet, source, direction);
    }
}
