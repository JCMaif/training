package com.simplon.training.Service;

import com.simplon.training.model.Validation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class NotificationService {

    private final OkHttpClient client = new OkHttpClient();

    // Injecter les propriétés depuis application.yml dans les champs d'instance
    @Value("${mailtrap.api-url}")
    private String apiUrl;

    @Value("${mailtrap.api-token}")
    private String apiToken;

    @Value("${mailtrap.from-email}")
    private String fromEmail;

    public void envoyer(Validation validation) {
        // Construire le corps de la requête
        String jsonBody = String.format(
                "{\"from\":{\"email\":\"%s\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"Votre code d'activation\",\"text\":\"Bonjour %s,\\n\\nVotre code d'activation est : %s\\n\\nA bientôt.\"}",
                fromEmail,
                validation.getUtilisateur().getEmail(),
                validation.getUtilisateur().getNom(),
                validation.getCode()
        );

        // Construire la requête HTTP
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Erreur lors de l'envoi de l'email : {}", response);
                throw new IOException("Echec de l'envoi de l'email");
            } else {
                log.info("Email envoyé avec succès à : {}", validation.getUtilisateur().getEmail());
            }
        } catch (IOException e) {
            log.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }
    }
}
