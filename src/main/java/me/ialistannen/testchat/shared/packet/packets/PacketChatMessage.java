package me.ialistannen.testchat.shared.packet.packets;

import me.ialistannen.networktest.shared.packet.Packet;

/**
 * A packet representing a chat message
 *
 * @author I Al Istannen
 */
public class PacketChatMessage extends PacketString {

    /**
     * Forced by {@link Packet}
     */
    public PacketChatMessage() {
    }

    /**
     * @param value The value of the {@link PacketString}
     */
    public PacketChatMessage(String value) {
        super(value);
    }

    /**
     * @return The Chat message
     */
    public String getMessage() {
        return super.getValue();
    }
}
