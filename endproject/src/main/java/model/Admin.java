/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.Role;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class Admin extends User {
    private int permissionLevel;

    public Admin() {
        super();
    }

    public Admin(String userId, String username, String passwordHash, String fullName, String email, int permissionLevel) {
        super(userId, username, passwordHash, fullName, email, Role.ADMIN);
        this.permissionLevel = permissionLevel;
    }

    public int getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(int level) { this.permissionLevel = level; }
}