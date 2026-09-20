package org.perfectsmiles.system.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncryptor {

    private static final String PEPPER = "SonrisaPerfecta_2026_$ecret!";

    /**
     * Encripta una contraseña en texto plano usando SHA-256 + Pepper + Base64.
     *
     * @param plainPassword Contraseña sin encriptar (ej: "Owner2026!")
     * @return Hash listo para guardar en password_hash (44 caracteres aprox.)
     */
    public static String encrypt(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String toHash = PEPPER + plainPassword;
            byte[] hash = md.digest(toHash.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible en este sistema.", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash guardado.
     */
    public static boolean matches(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        return encrypt(plainPassword).equals(storedHash);
    }
}
