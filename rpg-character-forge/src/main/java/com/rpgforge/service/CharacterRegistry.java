package com.rpgforge.service;

import com.rpgforge.domain.Character;
import com.rpgforge.dto.CharacterPatchDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que orquesta la clonación de personajes.
 * ═══ PATRÓN PROTOTYPE ═══ — usa Character.clone() y aplica un patch opcional.
 */
@Service
public class CharacterRegistry {

    private final CharacterService characterService;

    public CharacterRegistry(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * ═══ PATRÓN PROTOTYPE ═══
     * 1. Busca el personaje original por ID
     * 2. Llama a original.clone()
     * 3. Aplica los cambios del patch (name obligatorio; clase, skills, equipment opcionales)
     * 4. Persiste el clon y lo retorna
     */
    @Transactional
    public Character cloneCharacter(Long id, CharacterPatchDTO patch) {
        Character original = characterService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Personaje no encontrado: " + id));

        Character clone = original.clone();

        clone.setName(patch.getName());

        if (patch.getCharacterClass() != null && !patch.getCharacterClass().isBlank()) {
            clone.setCharacterClass(patch.getCharacterClass());
        }
        if (patch.getSkills() != null && !patch.getSkills().isEmpty()) {
            clone.setSkills(String.join(", ", patch.getSkills()));
        }
        if (patch.getEquipment() != null && !patch.getEquipment().isEmpty()) {
            clone.setEquipment(String.join(", ", patch.getEquipment()));
        }

        return characterService.save(clone);
    }
}
