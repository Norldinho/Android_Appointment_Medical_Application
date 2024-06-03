package com.example.mkjli;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private String email, subject, message;

    // Constructeur pour initialiser les variables
    public JavaMailAPI(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Configuration des propriétés SMTP pour Gmail
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Création d'une nouvelle session avec l'authentification
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    // Utilisation des informations d'identification de Utils pour l'authentification
                    return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                }
            });

            // Création du message
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(Utils.EMAIL));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Envoi du message
            Transport.send(mimeMessage);

            // Retourne true pour indiquer que l'e-mail a été envoyé avec succès
            return true;
        } catch (MessagingException e) {
            // Gestion des exceptions
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        // Affiche un message Toast pour informer l'utilisateur du succès ou de l'échec de l'envoi de l'e-mail
        if (result) {
            Toast.makeText(context, "E-mail envoyé avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Échec de l'envoi de l'e-mail", Toast.LENGTH_SHORT).show();
        }
    }
}
