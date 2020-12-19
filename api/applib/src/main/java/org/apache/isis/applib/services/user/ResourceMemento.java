package org.apache.isis.applib.services.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.commons.internal.collections._Lists;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Immutable serializable value held by {@link UserMemento}.
 * @since 2.0 {@index}
 */
@Value
public class ResourceMemento implements Serializable {

    private static final long serialVersionUID = 3876856609238378274L;

    @MemberOrder(sequence = "1.1")
    @Getter
    private final String name;

    @MemberOrder(sequence = "1.2")
    @Getter
    private final String description;


    /**
     * The roles played by this user under this specific resource
     */
    @MemberOrder(sequence = "1.3")
    private final List<RoleMemento> roles;
    public List<RoleMemento> getRoles() {
        return roles;
    }

    /**
     * Creates a new role with the specified name and description.
     */
    public ResourceMemento(final String name, final String description,final @NonNull Stream<RoleMemento> roles) {
        if (name == null) {
            throw new IllegalArgumentException("Name not specified");
        }
        this.name = name;
        if (description == null) {
            throw new IllegalArgumentException("Description not specified");
        }
        this.description = description;

        this.roles = roles.collect(_Lists.toUnmodifiable());
    }

}
