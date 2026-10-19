
package com.example.banking;

import com.example.banking.entities.Compte;
import com.example.banking.entities.Typecompte;
import com.example.banking.repositories.CompteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompteRepositoryTest {

    @Autowired
    private CompteRepository repository;

    @BeforeEach
    void setUp() {

        Compte compte1 = createCompte(
                1500.0, Typecompte.COURANT
        );

        Compte compte2 = createCompte(
                7800.0, Typecompte.EPARGNE
        );

        Compte compte3 = createCompte(
                3250.0, Typecompte.COURANT
        );

        repository.saveAll(
                List.of(compte1, compte2, compte3)
        );
    }

    private Compte createCompte(
            Double solde, Typecompte type) {

        return Compte.builder()
                .id(UUID.randomUUID().toString())
                .solde(solde)
                .dateCreation(new Date())
                .type(type)
                .build();
    }

    // TEST 1 : Enregistrer et retrouver un compte
    @Test
    void shouldSaveAndFindCompte() {

        Compte compte = createCompte(
                5000.0, Typecompte.COURANT
        );

        repository.save(compte);

        Compte result = repository.findById(
                compte.getId()
        ).orElseThrow();

        assertThat(result.getSolde())
                .isEqualTo(5000.0);

        assertThat(result.getType())
                .isEqualTo(Typecompte.COURANT);
    }

    // TEST 2 : Recuperer tous les comptes
    @Test
    void shouldFindAllComptes() {

        List<Compte> comptes = repository.findAll();

        assertThat(comptes).hasSize(3);
    }

    // TEST 3 : Rechercher par type
    @Test
    void shouldFindComptesByType() {

        List<Compte> comptes = repository.findByType(
                Typecompte.COURANT
        );

        assertThat(comptes).hasSize(2);

        assertThat(comptes)
                .allMatch(compte ->
                        compte.getType() == Typecompte.COURANT
                );
    }

    // TEST 4 : Rechercher par solde minimum
    @Test
    void shouldFindComptesBySoldeGreaterThan() {

        List<Compte> comptes =
                repository.findBySoldeGreaterThan(3000.0);

        assertThat(comptes).hasSize(2);

        assertThat(comptes)
                .allMatch(compte ->
                        compte.getSolde() > 3000.0
                );
    }

    // TEST 5 : Rechercher par type et solde
    @Test
    void shouldFindByTypeAndSoldeGreaterThan() {

        List<Compte> comptes =
                repository.findByTypeAndSoldeGreaterThan(
                        Typecompte.COURANT, 2000.0
                );

        assertThat(comptes).hasSize(1);

        assertThat(comptes.get(0).getSolde())
                .isEqualTo(3250.0);
    }

    // TEST 6 : Modifier un compte
    @Test
    void shouldUpdateCompte() {

        Compte compte = repository.findAll().get(0);

        compte.setSolde(9999.0);

        repository.save(compte);

        Compte updated = repository.findById(
                compte.getId()
        ).orElseThrow();

        assertThat(updated.getSolde())
                .isEqualTo(9999.0);
    }

    // TEST 7 : Supprimer un compte
    @Test
    void shouldDeleteCompte() {

        Compte compte = repository.findAll().get(0);

        repository.deleteById(compte.getId());

        assertThat(repository.existsById(compte.getId()))
                .isFalse();

        assertThat(repository.count()).isEqualTo(2);
    }
}
