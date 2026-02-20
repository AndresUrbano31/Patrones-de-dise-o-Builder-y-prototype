package com.rpgforge.controller;

import com.rpgforge.domain.Character;
import com.rpgforge.dto.CharacterFormDTO;
import com.rpgforge.dto.CharacterPatchDTO;
import com.rpgforge.service.CharacterRegistry;
import com.rpgforge.service.CharacterService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CharacterController {

    private static final String SESSION_WIZARD_FORM = "wizardForm";

    private static final List<String> ALL_SKILLS = List.of(
            "Golpe brutal", "Escudo divino", "Flecha de fuego", "Teletransporte", "Curación",
            "Veneno letal", "Rayo de hielo", "Furia berserker", "Invisibilidad", "Invocar familiar");
    private static final List<String> ALL_EQUIPMENT = List.of(
            "Espada larga", "Armadura de placas", "Arco élfico", "Bastón mágico",
            "Daga envenenada", "Escudo de roble", "Capa de invisibilidad", "Botas veloces");

    private final CharacterService characterService;
    private final CharacterRegistry characterRegistry;

    public CharacterController(CharacterService characterService, CharacterRegistry characterRegistry) {
        this.characterService = characterService;
        this.characterRegistry = characterRegistry;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/gallery";
    }

    // --- Wizard Step 1: Identidad ---
    @GetMapping("/wizard/step1")
    public String wizardStep1(Model model, HttpSession session) {
        CharacterFormDTO form = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (form == null) {
            form = new CharacterFormDTO();
        }
        model.addAttribute("form", form);
        return "wizard-step1";
    }

    @PostMapping("/wizard/step1")
    public String wizardStep1Submit(@Valid @ModelAttribute("form") CharacterFormDTO form,
                                    BindingResult result,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "wizard-step1";
        }
        session.setAttribute(SESSION_WIZARD_FORM, form);
        return "redirect:/wizard/step2";
    }

    // --- Wizard Step 2: Estadísticas ---
    @GetMapping("/wizard/step2")
    public String wizardStep2(Model model, HttpSession session) {
        CharacterFormDTO form = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (form == null) {
            return "redirect:/wizard/step1";
        }
        model.addAttribute("form", form);
        return "wizard-step2";
    }

    @PostMapping("/wizard/step2")
    public String wizardStep2Submit(@ModelAttribute("form") CharacterFormDTO form,
                                    HttpSession session) {
        CharacterFormDTO existing = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (existing == null) {
            return "redirect:/wizard/step1";
        }
        existing.setStrength(form.getStrength());
        existing.setAgility(form.getAgility());
        existing.setIntelligence(form.getIntelligence());
        existing.setVitality(form.getVitality());
        existing.setLuck(form.getLuck());
        session.setAttribute(SESSION_WIZARD_FORM, existing);
        return "redirect:/wizard/step3";
    }

    // --- Wizard Step 3: Habilidades (máximo 4) ---
    @GetMapping("/wizard/step3")
    public String wizardStep3(Model model, HttpSession session) {
        CharacterFormDTO form = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (form == null) {
            return "redirect:/wizard/step1";
        }
        model.addAttribute("form", form);
        model.addAttribute("allSkills", ALL_SKILLS);
        return "wizard-step3";
    }

    @PostMapping("/wizard/step3")
    public String wizardStep3Submit(@RequestParam(value = "skills", required = false) List<String> skills,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        CharacterFormDTO existing = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (existing == null) {
            return "redirect:/wizard/step1";
        }
        if (skills != null && skills.size() > 4) {
            redirectAttributes.addFlashAttribute("error", "Máximo 4 habilidades.");
            return "redirect:/wizard/step3";
        }
        existing.setSkills(skills != null ? skills : new ArrayList<>());
        session.setAttribute(SESSION_WIZARD_FORM, existing);
        return "redirect:/wizard/step4";
    }

    // --- Wizard Step 4: Equipamiento (máximo 3) — construye con Builder y persiste ---
    @GetMapping("/wizard/step4")
    public String wizardStep4(Model model, HttpSession session) {
        CharacterFormDTO form = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (form == null) {
            return "redirect:/wizard/step1";
        }
        model.addAttribute("form", form);
        model.addAttribute("allEquipment", ALL_EQUIPMENT);
        return "wizard-step4";
    }

    @PostMapping("/wizard/step4")
    public String wizardStep4Submit(@RequestParam(value = "equipment", required = false) List<String> equipment,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        CharacterFormDTO existing = (CharacterFormDTO) session.getAttribute(SESSION_WIZARD_FORM);
        if (existing == null) {
            return "redirect:/wizard/step1";
        }
        if (equipment != null && equipment.size() > 3) {
            redirectAttributes.addFlashAttribute("error", "Máximo 3 items de equipamiento.");
            return "redirect:/wizard/step4";
        }
        existing.setEquipment(equipment != null ? equipment : new ArrayList<>());

        Character character = characterService.buildFromForm(existing);
        session.removeAttribute(SESSION_WIZARD_FORM);
        redirectAttributes.addFlashAttribute("createdName", character.getName());
        return "redirect:/gallery";
    }

    // --- Galería ---
    @GetMapping("/gallery")
    public String gallery(Model model,
                          @RequestParam(value = "cloneId", required = false) Long cloneId) {
        List<Character> characters = characterService.findAll();
        Map<Long, List<Character>> clonesByOriginalId = characters.stream()
                .filter(c -> c.getClonedFromId() != null)
                .collect(Collectors.groupingBy(Character::getClonedFromId));
        Map<Long, String> originalNames = characters.stream()
                .collect(Collectors.toMap(Character::getId, Character::getName));
        model.addAttribute("characters", characters);
        model.addAttribute("clonesByOriginalId", clonesByOriginalId);
        model.addAttribute("originalNames", originalNames);
        model.addAttribute("allSkills", ALL_SKILLS);
        model.addAttribute("allEquipment", ALL_EQUIPMENT);
        model.addAttribute("showCloneModal", cloneId != null);
        model.addAttribute("cloneTargetId", cloneId);
        if (cloneId != null) {
            characterService.findById(cloneId).ifPresent(c -> model.addAttribute("cloneTarget", c));
        }
        if (!model.containsAttribute("patch")) {
            model.addAttribute("patch", new CharacterPatchDTO());
        }
        return "gallery";
    }

    @GetMapping("/clone/{id}")
    public String cloneGet(@PathVariable Long id) {
        return "redirect:/gallery?cloneId=" + id;
    }

    @PostMapping("/clone/{id}")
    public String clonePost(@PathVariable Long id,
                            @Valid @ModelAttribute("patch") CharacterPatchDTO patch,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.patch", result);
            redirectAttributes.addFlashAttribute("patch", patch);
            return "redirect:/gallery?cloneId=" + id;
        }
        if (patch.getSkills() != null && patch.getSkills().size() > 4) {
            redirectAttributes.addFlashAttribute("error", "Máximo 4 habilidades.");
            return "redirect:/gallery?cloneId=" + id;
        }
        if (patch.getEquipment() != null && patch.getEquipment().size() > 3) {
            redirectAttributes.addFlashAttribute("error", "Máximo 3 items de equipamiento.");
            return "redirect:/gallery?cloneId=" + id;
        }
        characterRegistry.cloneCharacter(id, patch);
        redirectAttributes.addFlashAttribute("cloned", true);
        return "redirect:/gallery";
    }

    @GetMapping("/compare")
    public String compare(@RequestParam Long original,
                          @RequestParam Long clone,
                          Model model) {
        Character originalChar = characterService.findById(original)
                .orElseThrow(() -> new IllegalArgumentException("Personaje original no encontrado"));
        Character cloneChar = characterService.findById(clone)
                .orElseThrow(() -> new IllegalArgumentException("Personaje clon no encontrado"));
        model.addAttribute("original", originalChar);
        model.addAttribute("clone", cloneChar);

        List<String> cloneSkills = cloneChar.getSkills() == null || cloneChar.getSkills().isBlank()
                ? List.of()
                : List.of(cloneChar.getSkills().split(",\\s*"));
        List<String> originalSkills = originalChar.getSkills() == null || originalChar.getSkills().isBlank()
                ? List.of()
                : List.of(originalChar.getSkills().split(",\\s*"));
        List<String> uniqueToClone = cloneSkills.stream()
                .map(String::trim)
                .filter(s -> !originalSkills.stream().map(String::trim).toList().contains(s))
                .collect(Collectors.toList());
        model.addAttribute("uniqueSkillsClone", uniqueToClone);

        return "compare";
    }
}
