package com.simplon.training.Service;

import com.simplon.training.model.Role;
import com.simplon.training.model.Utilisateur;
import com.simplon.training.model.enums.TypeRole;
import com.simplon.training.repository.UtilisateurRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;

    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;

    public void inscription(Utilisateur utilisateur) throws MessagingException {
//        Verif sur le mail :
        if(!utilisateur.getEmail().contains("@") || !utilisateur.getEmail().contains(".")){
            throw new RuntimeException("Email invalide");
        }

//        Verifier que l'utilisateur n'est pas déjà dans la base
        final Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if(utilisateurOptional.isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

//        Encryptage du passwor avant de l'enregistrer
        final String passwordEncrypted = this.passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(passwordEncrypted);

//        Affectation d'un rôle
        final Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeRole.UTILISATEUR);
        if (utilisateur.getRole() != null && utilisateur.getRole().getLibelle().equals(TypeRole.ADMIN)) {
            roleUtilisateur.setLibelle(TypeRole.ADMIN);
            utilisateur.setActif(true);
        }

        utilisateur.setRole(roleUtilisateur);

//        Enregistrement
        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregitrer(utilisateur);
    }
}
