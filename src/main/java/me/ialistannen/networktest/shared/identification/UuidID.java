package me.ialistannen.networktest.shared.identification;

import java.util.Objects;
import java.util.UUID;

/**
 * An {@link ID} using a {@link UUID} internally
 *
 * @author I Al Istannen
 */
public class UuidID implements ID {
    private UUID uuid;

    /**
     * Creates a new {@link UuidID} with the given {@link UUID}
     *
     * @param uuid The {@link UUID} to use
     */
    public UuidID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UuidID)) {
            return false;
        }
        UuidID uuidID = (UuidID) o;
        return Objects.equals(uuid, uuidID.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "UuidID{" +
                "uuid=" + uuid +
                '}';
    }
}
