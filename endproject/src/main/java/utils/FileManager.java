/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.*;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class FileManager {
    public static void saveObject(String path, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + path + " - " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String path, Class<T> clazz) {
        File file = new File(path);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc file: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }
}
