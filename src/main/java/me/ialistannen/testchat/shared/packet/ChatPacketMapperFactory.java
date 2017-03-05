package me.ialistannen.testchat.shared.packet;

import me.ialistannen.networktest.shared.packet.protocol.PacketMapper;
import me.ialistannen.testchat.server.ChatServer;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;
import me.ialistannen.testchat.shared.packet.packets.PacketSetNickName;

/**
 * Generates the {@link PacketMapper} for the {@link ChatServer}
 *
 * @author I Al Istannen
 */
public final class ChatPacketMapperFactory extends PacketMapper {

    private static final PacketMapper ourInstance = new ChatPacketMapperFactory();

    /**
     * Singletons like private constructors!
     */
    private ChatPacketMapperFactory() {
        registerPacket(1, PacketSetNickName.class);
        registerPacket(2, PacketChatMessage.class);
    }

    /**
     * Returns the {@link PacketMapper}
     *
     * @return The {@link PacketMapper}
     */
    public static PacketMapper getMapper() {
        return ourInstance;
    }
}
