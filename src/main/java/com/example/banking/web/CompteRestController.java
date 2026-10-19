
package com.example.banking.web;

import com.example.banking.entities.Compte;
import com.example.banking.repositories.CompteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@Tag(
        name = "Gestion des comptes bancaires",
        description = "Operations CRUD sur les comptes bancaires"
)
public class CompteRestController {

    private final CompteRepository compteRepository;

    // ==============================
    // GET : Tous les comptes
    // ==============================

    @Operation(
            summary = "Lister tous les comptes",
            description = "Retourne la liste des comptes bancaires"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des comptes recuperee"
    )
    @GetMapping
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    // ==============================
    // GET : Compte par ID
    // ==============================

    @Operation(
            summary = "Rechercher un compte",
            description = "Recherche un compte bancaire par ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Compte trouve"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compte introuvable"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(
            @Parameter(description = "Identifiant du compte")
            @PathVariable String id
    ) {
        return compteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // ==============================
    // POST : Creation
    // ==============================

    @Operation(
            summary = "Creer un compte",
            description = "Cree un nouveau compte bancaire"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Compte cree avec succes"
    )
    @PostMapping
    public ResponseEntity<Compte> createCompte(
            @RequestBody Compte compte
    ) {
        compte.setId(UUID.randomUUID().toString());
        compte.setDateCreation(new Date());

        Compte savedCompte = compteRepository.save(compte);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCompte);
    }

    // ==============================
    // PUT : Modification
    // ==============================

    @Operation(
            summary = "Modifier un compte",
            description = "Modifie le solde et le type d'un compte"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Compte modifie"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compte introuvable"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(
            @Parameter(description = "ID du compte a modifier")
            @PathVariable String id,
            @RequestBody Compte updatedCompte
    ) {
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

    // ==============================
    // DELETE : Suppression
    // ==============================

    @Operation(
            summary = "Supprimer un compte",
            description = "Supprime un compte bancaire par ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Compte supprime"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compte introuvable"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(
            @Parameter(description = "ID du compte a supprimer")
            @PathVariable String id
    ) {
        if (!compteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        compteRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
