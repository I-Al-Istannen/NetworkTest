package me.ialistannen.networktest.shared.packet;

/**
 * The direction a {@link Packet} is travelling in
 *
 * @author I Al Istannen
 */
public enum Direction {

    /**
     * A {@link Packet} heading to the client
     */
    TO_CLIENT,
    /**
     * A {@link Packet} heading to the server
     */
    TO_SERVER;
}
