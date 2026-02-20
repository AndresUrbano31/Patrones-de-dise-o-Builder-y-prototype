package com.rpgforge.dto;

import jakarta.validation.constraints.*;

import java.util.List;

/**
 * DTO para el formulario del wizard de creación de personaje.
 */
public class CharacterFormDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

    private String race;

    @NotBlank(message = "La clase es obligatoria")
    private String characterClass;

    @Min(1)
    @Max(100)
    private int level = 1;

    @Min(1)
    @Max(100)
    private int strength = 1;

    @Min(1)
    @Max(100)
    private int agility = 1;

    @Min(1)
    @Max(100)
    private int intelligence = 1;

    @Min(1)
    @Max(100)
    private int vitality = 1;

    @Min(1)
    @Max(100)
    private int luck = 1;

    @Size(max = 4, message = "Máximo 4 habilidades")
    private List<String> skills = List.of();

    @Size(max = 3, message = "Máximo 3 items de equipamiento")
    private List<String> equipment = List.of();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? skills : List.of();
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment != null ? equipment : List.of();
    }
}
