package me.ialistannen.testchat.client;

import me.ialistannen.networktest.shared.event.EventFactory;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.testchat.shared.event.PlaceholderEventFactory;
import me.ialistannen.testchat.shared.event.events.PacketStringEvent;
import me.ialistannen.testchat.shared.event.events.ReceiveChatMessageEvent;
import me.ialistannen.testchat.shared.event.events.SendChatMessageEvent;
import me.ialistannen.testchat.shared.packet.packets.PacketChatMessage;
import me.ialistannen.testchat.shared.packet.packets.PacketString;

/**
 * An {@link EventFactory} for the {@link ChatClient}
 *
 * @author I Al Istannen
 */
class ClientEventFactory extends PlaceholderEventFactory {

    /**
     * Creates a new {@link ClientEventFactory}
     */
    ClientEventFactory() {
        addPacketFactory(
                PacketString.class,
                (packet, source, direction) -> new PacketStringEvent(source, packet, direction)
        );

        addPacketFactory(
                PacketChatMessage.class,
                (packet, source, direction) -> {
                    if (direction == Direction.TO_CLIENT) {
                        return new ReceiveChatMessageEvent(source, packet, direction);
                    }
                    return new SendChatMessageEvent(source, packet, direction);
                }
        );
    }
}
