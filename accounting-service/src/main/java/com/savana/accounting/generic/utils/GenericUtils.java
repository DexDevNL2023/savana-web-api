package com.savana.accounting.generic.utils;

import  com.savana.accounting.generic.exceptions.RessourceNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;


@Component
public class GenericUtils {

    @Value("${server.address:localhost}")
    private String injectedServerAddress;

    @Value("${server.port:9000}")
    private int injectedServerPort;

    @Value("${spring.profiles.active:dev}")
    private String injectedActiveProfile;

    // Regex pour les numéros de téléphone du Gabon
    private static final String GABON_PHONE_REGEX = "^(\\+241|00241)?(\\d{8})$";

    private static String serverAddress;
    private static int serverPort;
    private static String activeProfile;
    private static final String[] GABON_PHONE_NUMBERS = {"+24112345678", "+24123456789", "+24198765432", "+24174746071"};
    private static final String[] CAMEROON_PHONE_NUMBERS = {"+237612345678", "+237698765432", "+237672345678", "+237674746071"};

    // Regex pour les numéros de téléphone du Cameroun
    private static final String CAMEROON_PHONE_REGEX = "^(\\+237|00237)?(\\d{9})$";
    private static String herokuAddress;
    @Value("${heroku.address:localhost}")
    private String injectedHerokuAddress;
    private static final Set<String> usedPhoneNumbers = new HashSet<>();
    private static final Random random = new Random();

    @PostConstruct
    public void init() {
        serverAddress = injectedServerAddress;
        serverPort = injectedServerPort;
        activeProfile = injectedActiveProfile;
        herokuAddress = injectedHerokuAddress;
    }

    public static String getServerAbsoluteUrl() {
        // Définir les variables de base
        String scheme = "http";
        String serverName = serverAddress;

        // Déterminer le schéma et le nom du serveur en fonction du profil actif
        boolean isProd = "prod".equalsIgnoreCase(activeProfile);
        if (isProd) {
            scheme = "https";
            serverName = herokuAddress;
        }

        // Construire l'URL de base avec le schéma et le nom du serveur
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(scheme).append("://").append(serverName);

        // En mode développement, ajouter le port s'il est défini et s'il ne s'agit pas du port par défaut (80 pour HTTP, 443 pour HTTPS)
        if (!isProd && serverPort > 0) {
            if (serverPort != 80) {
                urlBuilder.append(":").append(serverPort);
            }
        }

        return urlBuilder.toString();
    }

    public static String generatedPassWord() {
        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int) (AlphaNumericStr.length() * Math.random());
            sb.append(AlphaNumericStr.charAt(index));
        }
        return sb.toString();
    }

    public static String generateTokenNumber() {
        String AlphaNumericStr = "0123456789";

        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = (int) (AlphaNumericStr.length() * Math.random());
            sb.append(AlphaNumericStr.charAt(index));
        }
        return "T-" + sb.toString();
    }

    public static String GenerateNumero(String prefixe) {
        Random random = new Random();
        String number = String.format("%04d", random.nextInt(100000));
        return prefixe+"-"+number;
    }

    // Générer un numéro NUI au format "NUI-XXXXXXXXX" (9 chiffres)
    public static String generateNumNiu() {
        String prefix = "NUI-";
        String randomDigits = RandomStringUtils.randomNumeric(9); // Génère 9 chiffres aléatoires
        return prefix + randomDigits;
    }

    // Générer un numéro CNI au format "CNIXXXXXXX" (7 chiffres)
    public static String generateNumCni() {
        String prefix = "CNI";
        String randomDigits = RandomStringUtils.randomNumeric(7); // Génère 7 chiffres aléatoires
        return prefix + randomDigits;
    }

    public static String sha256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hashage SHA-256", e);
        }
    }

    public static int calculAge(Date dateNaissance) {
        Calendar current = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(dateNaissance);
        int yearDiff = current.get((Calendar.YEAR) - birthday.get(Calendar.YEAR));
        if (birthday.after(current)) {
            yearDiff = yearDiff - 1;
        }
        return yearDiff;
    }

    public static boolean isValidNumeroTelephone(String telephone) {
        // On retire les espaces et les caractères non numériques
        telephone = telephone.replaceAll("\\s+", "").trim();

        // Vérifier le format pour le Gabon
        if (Pattern.matches(GABON_PHONE_REGEX, telephone)) {
            return true; // Retourne true si le format est valide pour le Gabon
        }

        // Vérifier le format pour le Cameroun
        if (Pattern.matches(CAMEROON_PHONE_REGEX, telephone)) {
            return true; // Retourne true si le format est valide pour le Cameroun
        }

        return false; // Retourne false si le format ne correspond à aucun pays
    }

    public static void validatePageNumberAndSize(final Integer page, final Integer size) {
        if (page < 0) {
            throw new RessourceNotFoundException("Le numéro de page ne peut pas être inférieur à zéro.");
        }

        if (size <= 0) {
            throw new RessourceNotFoundException("La taille de la page ne doit pas être inférieur à zéro.");
        }
    }

    public static Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public static String storeFile(MultipartFile file, Path fileStorageLocation) {
        // Normalize file name
        String fileName = new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Désolé! Le nom du fichier contient une séquence de chemin non valide" + fileName);
            }

            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    public static String verifieFormatLangue(String langKey) {
        return switch (langKey) {
            case "En" -> "En";
            case "Esp" -> "Esp";
            default -> "Fr"; // default language
        };
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static String generateRandomValidPhoneNumber() {
        String generatedPhone;
        int attempts = 0;
        int maxAttempts = GABON_PHONE_NUMBERS.length + CAMEROON_PHONE_NUMBERS.length;

        do {
            // Choisir aléatoirement entre les numéros du Gabon et du Cameroun
            if (random.nextBoolean()) {
                generatedPhone = GABON_PHONE_NUMBERS[random.nextInt(GABON_PHONE_NUMBERS.length)];
            } else {
                generatedPhone = CAMEROON_PHONE_NUMBERS[random.nextInt(CAMEROON_PHONE_NUMBERS.length)];
            }
            attempts++;
        } while (usedPhoneNumbers.contains(generatedPhone) && attempts < maxAttempts);

        // Si un numéro unique est trouvé, l'ajouter à la liste des numéros utilisés
        if (!usedPhoneNumbers.contains(generatedPhone)) {
            usedPhoneNumbers.add(generatedPhone);
            return generatedPhone;
        } else {
            throw new RuntimeException("Aucun numéro de téléphone unique disponible pour la génération.");
        }
    }
}
