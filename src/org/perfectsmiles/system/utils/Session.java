package org.perfectsmiles.system.utils;

import org.perfectsmiles.system.model.Budget;
import org.perfectsmiles.system.model.Patient;
import org.perfectsmiles.system.model.Permission;
import org.perfectsmiles.system.model.User;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static User currentUser;
    private static List<Permission> currentPermissions = new ArrayList<>();
    private static Budget budgetToEdit;
    private static Patient patientForHistory;

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

    public static boolean hasRole(int idRole) {
        return currentUser != null && currentUser.getIdRole() == idRole;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void setBudgetToEdit(Budget budget) {
        budgetToEdit = budget;
    }

    public static Budget getBudgetToEdit() {
        return budgetToEdit;
    }

    public static void clearBudgetToEdit() {
        budgetToEdit = null;
    }

    public static void setPatientForHistory(Patient patient) {
        patientForHistory = patient;
    }

    public static Patient getPatientForHistory() {
        return patientForHistory;
    }

    public static void clearPatientForHistory() {
        patientForHistory = null;
    }

    public static void clear() {
        currentUser = null;
        currentPermissions = new ArrayList<>();
        budgetToEdit = null;
        patientForHistory = null;
    }
}
