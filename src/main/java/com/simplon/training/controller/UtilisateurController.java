package com.simplon.training.controller;

import com.simplon.training.Service.UtilisateurService;
import com.simplon.training.model.Utilisateur;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {

    private UtilisateurService utilisateurService;
    @PostMapping(path = "signup")
    public void inscription(@RequestBody Utilisateur utilisateur) throws MessagingException {
        log.info("Inscription");
        this.utilisateurService.inscription(utilisateur);
    }  @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.inscription(activation();
    }
}
