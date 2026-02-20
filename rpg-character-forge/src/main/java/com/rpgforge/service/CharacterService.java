package com.rpgforge.service;

import com.rpgforge.domain.Character;
import com.rpgforge.dto.CharacterFormDTO;
import com.rpgforge.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    /**
     * ═══ PATRÓN BUILDER ═══
     * Construye un Character a partir del DTO del formulario usando Character.Builder.
     */
    @Transactional
    public Character buildFromForm(CharacterFormDTO form) {
        String skillsStr = form.getSkills() != null && !form.getSkills().isEmpty()
                ? String.join(", ", form.getSkills())
                : "";
        String equipmentStr = form.getEquipment() != null && !form.getEquipment().isEmpty()
                ? String.join(", ", form.getEquipment())
                : "";

        Character character = new Character.Builder()
                .name(form.getName())
                .race(form.getRace())
                .characterClass(form.getCharacterClass())
                .level(form.getLevel())
                .strength(form.getStrength())
                .agility(form.getAgility())
                .intelligence(form.getIntelligence())
                .vitality(form.getVitality())
                .luck(form.getLuck())
                .skills(skillsStr)
                .equipment(equipmentStr)
                .build();

        return save(character);
    }

    @Transactional
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    public List<Character> findAll() {
        return characterRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Character> findById(Long id) {
        return characterRepository.findById(id);
    }
}
