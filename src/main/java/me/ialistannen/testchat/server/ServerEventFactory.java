package me.ialistannen.testchat.server;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.event.PacketEvent;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.testchat.server.events.ReceiveNickNameEvent;
import me.ialistannen.testchat.shared.event.PlaceholderEventFactory;
import me.ialistannen.testchat.shared.event.events.ReceiveChatMessageEvent;
import me.ialistannen.testchat.shared.event.events.SendChatMessageEvent;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;
import me.ialistannen.testchat.shared.packet.packets.PacketSetNickName;

/**
 * An {@link EventFactory} for the {@link ChatServer}
 *
 * @author I Al Istannen
 */
class ServerEventFactory extends PlaceholderEventFactory {

    {
        addPacketFactory(PacketSetNickName.class, (packet, source, direction) -> {
            if (direction == Direction.TO_SERVER) {
                return new ReceiveNickNameEvent(source, packet, direction);
            }
            return new PacketEvent(source, packet, direction);
        });
        addPacketFactory(
                PacketChatMessage.class,
                (packet, source, direction) -> {
                    if (direction == Direction.TO_SERVER) {
                        return new ReceiveChatMessageEvent(source, packet, direction);
                    }
                    return new SendChatMessageEvent(source, packet, direction);
                }
        );

    }
}
