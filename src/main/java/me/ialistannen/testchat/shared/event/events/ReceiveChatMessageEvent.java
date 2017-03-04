package me.ialistannen.testchat.shared.event.events;

import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;

/**
 * A {@link PacketEvent} for a received chat message
 *
 * @author I Al Istannen
 */
public class ReceiveChatMessageEvent extends PacketEvent<PacketChatMessage> {

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public ReceiveChatMessageEvent(Object source, PacketChatMessage packet, Direction direction) {
        super(source, packet, direction);
    }

    /**
     * @return The Chat message
     */
    public String getMessage() {
        return getPacket().getValue();
    }
}
