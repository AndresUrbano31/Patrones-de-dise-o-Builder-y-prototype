package com.rpgforge.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un personaje RPG.
 * Implementa Cloneable para el patrón Prototype y contiene un Builder estático interno.
 */
@Entity
@Table(name = "characters")
public class Character implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String race;

    @Column(name = "character_class", nullable = false)
    private String characterClass;

    private int level = 1;

    private int strength = 1;
    private int agility = 1;
    private int intelligence = 1;
    private int vitality = 1;
    private int luck = 1;

    @Column(length = 1000)
    private String skills = "";

    @Column(length = 1000)
    private String equipment = "";

    @Column(name = "cloned_from_id")
    private Long clonedFromId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Character() {
    }

    // ═══ PATRÓN PROTOTYPE ═══
    // Crea una copia del personaje para clonación; id y createdAt se reinician, clonedFromId apunta al original.
    @Override
    public Character clone() {
        try {
            Character clone = (Character) super.clone();
            clone.id = null;
            clone.createdAt = LocalDateTime.now();
            clone.clonedFromId = this.id;
            clone.skills = this.skills == null ? "" : new String(this.skills);
            clone.equipment = this.equipment == null ? "" : new String(this.equipment);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Character supports cloning", e);
        }
    }

    // ═══ PATRÓN BUILDER ═══
    // Clase estática interna que permite construir un Character paso a paso con validación.
    public static class Builder {
        private final Character character;

        public Builder() {
            this.character = new Character();
        }

        public Builder name(String name) {
            character.name = name;
            return this;
        }

        public Builder race(String race) {
            character.race = race;
            return this;
        }

        public Builder characterClass(String characterClass) {
            character.characterClass = characterClass;
            return this;
        }

        public Builder level(int level) {
            character.level = Math.max(1, Math.min(100, level));
            return this;
        }

        public Builder strength(int strength) {
            character.strength = Math.max(1, Math.min(100, strength));
            return this;
        }

        public Builder agility(int agility) {
            character.agility = Math.max(1, Math.min(100, agility));
            return this;
        }

        public Builder intelligence(int intelligence) {
            character.intelligence = Math.max(1, Math.min(100, intelligence));
            return this;
        }

        public Builder vitality(int vitality) {
            character.vitality = Math.max(1, Math.min(100, vitality));
            return this;
        }

        public Builder luck(int luck) {
            character.luck = Math.max(1, Math.min(100, luck));
            return this;
        }

        public Builder skills(String skills) {
            character.skills = skills == null ? "" : skills;
            return this;
        }

        public Builder equipment(String equipment) {
            character.equipment = equipment == null ? "" : equipment;
            return this;
        }

        public Character build() {
            if (character.name == null || character.name.isBlank()) {
                throw new IllegalStateException("name no puede ser nulo ni vacío");
            }
            if (character.characterClass == null || character.characterClass.isBlank()) {
                throw new IllegalStateException("characterClass no puede ser nulo ni vacío");
            }
            character.createdAt = LocalDateTime.now();
            return character;
        }
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Long getClonedFromId() {
        return clonedFromId;
    }

    public void setClonedFromId(Long clonedFromId) {
        this.clonedFromId = clonedFromId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(id, character.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
