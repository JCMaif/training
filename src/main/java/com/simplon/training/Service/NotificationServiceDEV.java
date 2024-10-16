package com.simplon.training.Service;

import com.simplon.training.model.Validation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.X509Certificate;

@Service
@Slf4j
public class NotificationServiceDEV {

    private final OkHttpClient client;

    @Value("${mailtrap.api-url}")
    private String apiUrl;

    @Value("${mailtrap.api-token}")
    private String apiToken;

    @Value("${mailtrap.from-email}")
    private String fromEmail;

    public NotificationServiceDEV() {
        this.client = createOkHttpClient();
    }

    // Méthode pour créer un OkHttpClient sans vérification SSL
    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(createTrustAllSslSocketFactory(), createTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Ignore la vérification du nom d'hôte
                .build();
    }

    // Méthode pour créer un TrustManager qui fait confiance à tous les certificats
    private X509TrustManager createTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    // Méthode pour créer un SSLContext qui fait confiance à tous les certificats
    private SSLSocketFactory createTrustAllSslSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{createTrustManager()}, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void envoyer(Validation validation) {
        // Construire le corps de la requête
        String jsonBody = String.format(
                "{\"from\":{\"email\":\"%s\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"Votre code d'activation\",\"text\":\"Bonjour %s,%n%nVotre code d'activation est : %s%n%nA bientôt.\"}",
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
