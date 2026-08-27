/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.time.Year;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class Validator {
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidRating(double rating) {
        return rating >= 0.0 && rating <= 10.0;
    }

    public static boolean isValidYear(int year) {
        int currentYear = Year.now().getValue();
        return year >= 1888 && year <= currentYear + 1;
    }

    public static boolean isValidDuration(int duration) {
        return duration > 0;
    }
}
