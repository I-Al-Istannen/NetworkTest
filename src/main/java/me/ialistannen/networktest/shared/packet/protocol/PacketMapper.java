package me.ialistannen.networktest.shared.packet.protocol;

import java.util.Optional;
import java.util.OptionalInt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A class mapping a Packet ID to a {@link Packet}
 *
 * @author I Al Istannen
 */
public class PacketMapper {

    private BiMap<Integer, Class<? extends Packet>> idToPacketMap = HashBiMap.create();
    private BiMap<Class<? extends Packet>, Integer> packetToIdMap = idToPacketMap.inverse();

    /**
     * Registers a packet
     *
     * @param id The ID of the {@link Packet}
     * @param packetClass The class of the packet to add
     */
    public void registerPacket(int id, Class<? extends Packet> packetClass) {
        idToPacketMap.put(id, packetClass);
    }

    /**
     * Returns the Class of a {@link Packet} given the ID
     *
     * @param id The ID of the packet
     *
     * @return The Packet class if found
     */
    public Optional<Class<? extends Packet>> getPacketClass(int id) {
        return Optional.ofNullable(idToPacketMap.get(id));
    }

    /**
     * Returns the ID of a {@link Packet} given the class
     *
     * @param packetClass The {@link Class} of the {@link Packet}
     *
     * @return The packet ID if any
     */
    public OptionalInt getPacketID(Class<? extends Packet> packetClass) {
        return packetToIdMap.containsKey(packetClass)
               ? OptionalInt.of(packetToIdMap.get(packetClass))
               : OptionalInt.empty();
    }

}
