package com.simplon.training.Service;

import com.simplon.training.model.Utilisateur;
import com.simplon.training.model.Validation;
import com.simplon.training.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@AllArgsConstructor
public class ValidationService {
    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    private NotificationServiceDEV notificationServiceDEV;
    public void enregitrer(Utilisateur utilisateur) {
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);

        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);

        validation.setExpiration(expiration);


        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);

        this.validationRepository.save(validation);
     //   this.notificationService.envoyer(validation);
        this.notificationServiceDEV.envoyer(validation);


    }
}
