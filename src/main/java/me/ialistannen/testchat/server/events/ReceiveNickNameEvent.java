package me.ialistannen.testchat.server.events;

import com.google.common.base.Preconditions;

import me.ialistannen.networktest.server.ConnectedClient;
import me.ialistannen.networktest.shared.packet.Direction;
import me.ialistannen.networktest.shared.packet.Packet;
import me.ialistannen.testchat.shared.event.events.PacketStringEvent;
import me.ialistannen.testchat.shared.packet.packets.PacketSetNickName;

/**
 * Posted when the client wants to nick himself
 *
 * @author I Al Istannen
 */
public class ReceiveNickNameEvent extends PacketStringEvent {

    /**
     * Creates a {@link ReceiveNickNameEvent}
     *
     * @param source The source
     * @param packet The {@link Packet}
     * @param direction The {@link Direction}
     */
    public ReceiveNickNameEvent(Object source, Packet packet, Direction direction) {
        super(source, packet, direction);

        Preconditions.checkArgument(direction == Direction.TO_SERVER, "direction must be TO_SERVER");
        Preconditions.checkArgument(
                source instanceof ConnectedClient,
                "source must be an instance of ConnectedClient"
        );
        Preconditions.checkArgument(
                packet instanceof PacketSetNickName,
                "packet must be an instance of PacketSetNickName"
        );
    }

    /**
     * @return The new NickName
     */
    public String getNickName() {
        return getPacket().getNick();
    }

    @Override
    public PacketSetNickName getPacket() {
        return (PacketSetNickName) super.getPacket();
    }

    @Override
    public ConnectedClient getSource() {
        return (ConnectedClient) super.getSource();
    }
}
