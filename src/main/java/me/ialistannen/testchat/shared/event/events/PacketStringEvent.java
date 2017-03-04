package me.ialistannen.testchat.shared.event.events;

import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.packet.packets.PacketString;

/**
 * @author I Al Istannen
 */
public class PacketStringEvent extends PacketEvent<PacketString> {

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public PacketStringEvent(Object source, PacketString packet, Direction direction) {
        super(source, packet, direction);
    }

    @Override
    public String toString() {
        return "PacketStringEvent{" +
                "packet=" + getPacket() +
                '}';
    }
}
