package me.ialistannen.testchat.shared.event;

import java.util.HashMap;
import java.util.Map;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;

import static me.ialistannen.networktest.shared.event.EventManager.State;

/**
 * The {@link EventFactory} for the server
 *
 * @author I Al Istannen
 */
public class PlaceholderEventFactory implements EventFactory {

    private Map<Class<? extends Packet>, EventGeneratingFunction<?>> packetFactories = new HashMap<>();

    /**
     * Adds a Factory for a {@link Packet}
     *
     * @param packetClass The {@link Class} of the {@link Packet} to add
     * @param factory The factory for it
     */
    public <T extends Packet> void addPacketFactory(Class<T> packetClass, EventGeneratingFunction<T> factory) {
        packetFactories.put(packetClass, factory);
    }

    /**
     * @param packet The Packet
     * @param source The source of the packet
     * @param direction The {@link Direction} it is travelling
     * @param state The current {@link State}
     *
     * @return The created {@link PacketEvent}
     */
    @Override
    public <T extends Packet> PacketEvent<T> create(T packet, Object source,
                                                    Direction direction, State state) {
        if (!packetFactories.containsKey(packet.getClass())) {
            return new PacketEvent<>(source, packet, direction);
        }

        // This _is_ safe. The class of the packet should be the same as the packet
        @SuppressWarnings("unchecked")
        Class<T> packetClass = (Class<T>) packet.getClass();

        return getFactory(packetClass).create(packet, source, direction);
    }

    /**
     * @param packetClass The {@link Class} of the {@link Packet}
     * @param <T> The type of the packet
     *
     * @return The {@link EventGeneratingFunction} from the map
     */
    private <T extends Packet> EventGeneratingFunction<T> getFactory(Class<T> packetClass) {
        @SuppressWarnings("unchecked")
        EventGeneratingFunction<T> function = (EventGeneratingFunction<T>) packetFactories.get(packetClass);

        return function;
    }

    /**
     * A function generating an event
     */
    public interface EventGeneratingFunction <T extends Packet> {

        /**
         * Creates a new {@link PacketEvent}
         *
         * @param packet The {@link Packet}
         * @param source The source of the packet
         * @param direction The {@link Direction} it is travelling
         *
         * @return The created {@link PacketEvent}
         */
        PacketEvent<T> create(T packet, Object source, Direction direction);
    }
}
