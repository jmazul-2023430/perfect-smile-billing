package org.perfectsmiles.system.model;

import java.time.LocalDateTime;

public class User {

    private int idUser;
    private int idRole;
    private String userName;
    private String passwordHash;
    private String completeName;
    private String email;
    private String phone;
    private LocalDateTime lastAccess;
    private boolean active;

    public User() {
    }

    public User(int idUser, int idRole, String userName, String passwordHash,
            String completeName, String email, String phone,
            LocalDateTime lastAccess, boolean active) {
        this.idUser = idUser;
        this.idRole = idRole;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.completeName = completeName;
        this.email = email;
        this.phone = phone;
        this.lastAccess = lastAccess;
        this.active = active;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{id=" + idUser + ", userName='" + userName + "', name='" + completeName + "'}";
    }
}
