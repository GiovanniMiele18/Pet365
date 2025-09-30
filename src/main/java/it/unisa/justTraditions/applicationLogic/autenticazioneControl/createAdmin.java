package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class createAdmin {
    public static void main(String[] args) {
        // Scegli la tua nuova password qui:
        String plainPassword = "NuovaPasswordSicura123!";

        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String hash = encryptor.encryptPassword(plainPassword);

        System.out.println("Nuova password in chiaro: " + plainPassword);
        System.out.println("Hash da salvare nel DB: " + hash);
    }
}