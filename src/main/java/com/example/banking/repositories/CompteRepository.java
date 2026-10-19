
package com.example.banking.repositories;

import com.example.banking.entities.Compte;
import com.example.banking.entities.Typecompte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompteRepository
        extends JpaRepository<Compte, String> {

    // Rechercher les comptes par type
    List<Compte> findByType(Typecompte type);

    // Rechercher les comptes dont le solde
    // est superieur a une valeur
    List<Compte> findBySoldeGreaterThan(Double montant);

    // Rechercher les comptes par type et solde
    List<Compte> findByTypeAndSoldeGreaterThan(
            Typecompte type,
            Double montant
    );
}
