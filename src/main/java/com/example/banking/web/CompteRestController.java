
package com.example.banking.web;

import com.example.banking.entities.Compte;
import com.example.banking.repositories.CompteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
public class CompteRestController {

    private final CompteRepository compteRepository;

    // 1. GET : Recuperer tous les comptes
    @GetMapping
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    // 2. GET : Recuperer un compte par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(
            @PathVariable String id) {

        return compteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // 3. POST : Creer un nouveau compte
    @PostMapping
    public ResponseEntity<Compte> createCompte(
            @RequestBody Compte compte) {

        // Generer un identifiant unique
        compte.setId(UUID.randomUUID().toString());

        // Initialiser la date de creation
        compte.setDateCreation(new Date());

        // Enregistrer dans H2
        Compte savedCompte = compteRepository.save(compte);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCompte);
    }

    // 4. PUT : Modifier un compte existant
    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(
            @PathVariable String id,
            @RequestBody Compte updatedCompte) {

        return compteRepository.findById(id)
                .map(compte -> {

                    compte.setSolde(updatedCompte.getSolde());
                    compte.setType(updatedCompte.getType());

                    Compte savedCompte =
                            compteRepository.save(compte);

                    return ResponseEntity.ok(savedCompte);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // 5. DELETE : Supprimer un compte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(
            @PathVariable String id) {

        if (!compteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        compteRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
