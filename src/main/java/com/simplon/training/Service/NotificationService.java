package com.simplon.training.Service;

import com.simplon.training.model.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@expediteur.com");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre code d'activation");
        String text = String.format("Bonjour %s, <br /> Votre code d'activation es %s; A bientôt,",
            validation.getUtilisateur().getNom(),
            validation.getCode()
        );
        message.setText(text);

        javaMailSender.send(message);
    }
}
