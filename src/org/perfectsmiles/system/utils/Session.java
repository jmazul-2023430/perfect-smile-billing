package org.perfectsmiles.system.utils;

import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.model.User;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static User currentUser;
    private static List<Permission> currentPermissions = new ArrayList<>();

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentRoleId() {
        return currentUser != null ? currentUser.getIdRole() : -1;
    }

    public static void setCurrentPermissions(List<Permission> permissions) {
        currentPermissions = permissions != null ? permissions : new ArrayList<>();
    }

    public static List<Permission> getCurrentPermissions() {
        return currentPermissions;
    }

    public static boolean hasPermission(String permissionName) {
        if (currentUser == null || permissionName == null) {
            return false;
        }
        return currentPermissions.stream()
                .anyMatch(p -> permissionName.equalsIgnoreCase(p.getPermissionName()));
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Cierra sesión
     */
    public static void clear() {
        currentUser = null;
        currentPermissions = new ArrayList<>();
    }
}
