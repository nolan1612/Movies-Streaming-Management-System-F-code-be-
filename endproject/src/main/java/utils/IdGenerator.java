/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.UUID;
/**
 *
 * @author nguyenhoangminhnhat
 */

public class IdGenerator {
    public static String generateId(String prefix) {
        return prefix.toUpperCase() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
