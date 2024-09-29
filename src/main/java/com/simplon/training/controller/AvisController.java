package com.simplon.training.controller;

import com.simplon.training.Service.AvisService;
import com.simplon.training.model.Avis;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avis")
@AllArgsConstructor
public class AvisController {
    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
        this.avisService.creer(avis);
    }

}
