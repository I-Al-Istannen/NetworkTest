package me.ialistannen.testchat.shared.event.events;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.packet.packets.PacketString;

/**
 * @author I Al Istannen
 */
public class PacketStringEvent extends PacketEvent {

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public PacketStringEvent(Object source, Packet packet, Direction direction) {
        super(source, packet, direction);
        Preconditions.checkArgument(packet instanceof PacketString, "packet needs to be a PacketString");
    }

    @Override
    public PacketString getPacket() {
        return (PacketString) super.getPacket();
    }

    @Override
    public String toString() {
        return "PacketStringEvent{" +
                "packet=" + getPacket() +
                '}';
    }
}
