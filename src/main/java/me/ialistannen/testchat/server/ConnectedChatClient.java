package me.ialistannen.testchat.server;

import java.util.UUID;

import me.ialistannen.networktest.server.ConnectedClient;
import me.ialistannen.networktest.shared.identification.ID;
import me.ialistannen.networktest.shared.identification.UuidID;

/**
 * A chat client
 *
 * @author I Al Istannen
 */
public class ConnectedChatClient extends ConnectedClient {

    private final ID ID = new UuidID(UUID.randomUUID());

    private String nick;

    /**
     * @return The nick name
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick The new Nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Returns the client's unique {@link ID}
     *
     * @return The {@link ID} of this client
     */
    public ID getID() {
        return ID;
    }
}
