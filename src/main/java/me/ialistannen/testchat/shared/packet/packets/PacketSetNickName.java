package me.ialistannen.testchat.shared.packet.packets;

/**
 * A packet setting the nickname
 *
 * @author I Al Istannen
 */
public class PacketSetNickName extends PacketString {

    /**
     * Forced by {@link PacketSetNickName}
     */
    protected PacketSetNickName() {
    }

    /**
     * @param value The value of the {@link PacketString}
     */
    public PacketSetNickName(String value) {
        super(value);
    }

    /**
     * @return The NickName
     */
    public String getNick() {
        return super.getValue();
    }
}
