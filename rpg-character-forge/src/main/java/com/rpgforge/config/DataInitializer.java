package com.rpgforge.config;

import com.rpgforge.domain.Character;
import com.rpgforge.repository.CharacterRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Crea 3 personajes de ejemplo al arrancar usando Character.Builder.
 */
@Component
public class DataInitializer {

    private final CharacterRepository characterRepository;

    public DataInitializer(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @PostConstruct
    public void init() {
        if (characterRepository.count() > 0) {
            return;
        }

        // ═══ PATRÓN BUILDER ═══ — Construcción de personajes con el Builder
        Character ragnar = new Character.Builder()
                .name("Ragnar el Conquistador")
                .race("Humano")
                .characterClass("Guerrero")
                .level(45)
                .strength(90)
                .agility(60)
                .intelligence(30)
                .vitality(80)
                .luck(40)
                .skills("Golpe brutal, Escudo divino, Furia berserker")
                .equipment("Espada larga, Armadura de placas, Escudo de roble")
                .build();
        characterRepository.save(ragnar);

        Character sylvana = new Character.Builder()
                .name("Sylvana")
                .race("Elfo")
                .characterClass("Mago")
                .level(30)
                .strength(20)
                .agility(70)
                .intelligence(95)
                .vitality(40)
                .luck(75)
                .skills("Rayo de hielo, Teletransporte, Invocar familiar")
                .equipment("Bastón mágico, Capa de invisibilidad")
                .build();
        characterRepository.save(sylvana);

        Character drak = new Character.Builder()
                .name("Drak")
                .race("Orco")
                .characterClass("Arquero")
                .level(22)
                .strength(65)
                .agility(88)
                .intelligence(25)
                .vitality(55)
                .luck(60)
                .skills("Flecha de fuego, Veneno letal, Invisibilidad")
                .equipment("Arco élfico, Daga envenenada, Botas veloces")
                .build();
        characterRepository.save(drak);
    }
}
