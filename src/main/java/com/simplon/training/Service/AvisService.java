package com.simplon.training.Service;

import com.simplon.training.model.Avis;
import com.simplon.training.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AvisService {
    private final AvisRepository avisRepository;

    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }



}
