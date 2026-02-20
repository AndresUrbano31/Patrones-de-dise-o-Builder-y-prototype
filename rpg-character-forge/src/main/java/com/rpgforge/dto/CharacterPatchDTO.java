package com.rpgforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO para aplicar cambios al clonar un personaje (patch).
 * Solo name es obligatorio; el resto son opcionales.
 */
public class CharacterPatchDTO {

    @NotBlank(message = "El nuevo nombre es obligatorio")
    @Size(max = 100)
    private String name;

    private String characterClass;

    @Size(max = 4, message = "Máximo 4 habilidades")
    private List<String> skills;

    @Size(max = 3, message = "Máximo 3 items de equipamiento")
    private List<String> equipment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }
}
