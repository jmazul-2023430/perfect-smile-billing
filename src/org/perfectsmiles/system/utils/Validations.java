package org.perfectsmiles.system.utils;

import java.util.regex.Pattern;

public class Validations {

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 50;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static String getEmailError(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "El correo electrónico es obligatorio.";
        }
        if (!isValidEmail(email)) {
            return "El correo electrónico no tiene un formato válido. Ejemplo: usuario@dominio.com";
        }
        return null;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }

    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            return "La contraseña debe tener al menos " + PASSWORD_MIN_LENGTH + " caracteres.";
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            return "La contraseña no puede exceder " + PASSWORD_MAX_LENGTH + " caracteres.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "La contraseña debe incluir al menos una letra mayúscula.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "La contraseña debe incluir al menos una letra minúscula.";
        }
        if (!password.matches(".*\\d.*")) {
            return "La contraseña debe incluir al menos un número.";
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return "La contraseña debe incluir al menos un carácter especial (!@#$%^&*...).";
        }
        return null;
    }

    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    public static String getConfirmPasswordError(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Debes confirmar la contraseña.";
        }
        if (!doPasswordsMatch(password, confirmPassword)) {
            return "Las contraseñas no coinciden.";
        }
        return null;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String getRequiredFieldError(String value, String fieldName) {
        if (isNullOrEmpty(value)) {
            return "El campo '" + fieldName + "' es obligatorio.";
        }
        return null;
    }

    public static boolean isValidDpi(String dpi) {
        if (dpi == null || dpi.trim().isEmpty()) {
            return false;
        }
        return dpi.trim().matches("\\d{13}");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleaned = phone.replaceAll("[\\s\\-()]", "");
        return cleaned.matches("\\d{8,15}");
    }
}
