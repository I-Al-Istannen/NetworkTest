package me.ialistannen.networktest.shared.event;

import java.util.EnumMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;

/**
 * The events manager.
 *
 * @author I Al Istannen
 */
public class EventManager {

    private Map<State, EventBus> buses = new EnumMap<>(State.class);

    /**
     * Creates a new {@link EventManager}
     */
    public EventManager() {
        initBuses();
    }

    /**
     * Registers a listener
     *
     * @param state The {@link State} to register in
     * @param listener The listener to register
     */
    public void register(State state, Object listener) {
        buses.get(state).register(listener);
    }

    /**
     * Unregisters a listener
     *
     * @param state The {@link State} to unregister in
     * @param listener The listener to unregister
     */
    public void unregister(State state, Object listener) {
        buses.get(state).unregister(listener);
    }

    /**
     * Posts an {@link PacketEvent}
     *
     * @param event The events to post
     * @param state The state to post it at
     */
    public void postEvent(PacketEvent<?> event, State state) {
        EventBus eventBus = buses.get(state);
        if (eventBus != null) {
            eventBus.post(event);
        }
    }

    /**
     * Creates the {@link EventBus}es
     */
    private void initBuses() {
        for (State state : State.values()) {
            buses.put(state, new EventBus());
        }
    }

    /**
     * The state of the listener
     */
    public enum State {
        /**
         * Can filter events
         */
        FILTER,
        /**
         * Called last, just listens for them
         */
        LISTEN
    }
}
