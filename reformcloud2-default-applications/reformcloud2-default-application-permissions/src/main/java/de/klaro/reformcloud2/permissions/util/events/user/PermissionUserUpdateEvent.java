package de.klaro.reformcloud2.permissions.util.events.user;

import de.klaro.reformcloud2.executor.api.common.event.Event;
import de.klaro.reformcloud2.permissions.util.user.PermissionUser;

public class PermissionUserUpdateEvent extends Event {

    public PermissionUserUpdateEvent(PermissionUser permissionUser) {
        this.permissionUser = permissionUser;
    }

    private final PermissionUser permissionUser;

    public PermissionUser getPermissionUser() {
        return permissionUser;
    }
}