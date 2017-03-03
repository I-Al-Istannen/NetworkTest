package me.ialistannen.testchat.shared.event.events;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;

/**
 * A {@link PacketEvent} for a received chat message
 *
 * @author I Al Istannen
 */
public class ReceiveChatMessageEvent extends PacketEvent {

    /**
     * Creates a {@link PacketEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public ReceiveChatMessageEvent(Object source, Packet packet, Direction direction) {
        super(source, packet, direction);

        Preconditions.checkArgument(
                packet instanceof PacketChatMessage,
                "packet must be an instance of PacketChatMessage"
        );
    }

    /**
     * @return The Chat message
     */
    public String getMessage() {
        return getPacket().getValue();
    }

    @Override
    public PacketChatMessage getPacket() {
        return (PacketChatMessage) super.getPacket();
    }
}
