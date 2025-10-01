package com.fithero.fithero.business.services;

import com.fithero.fithero.db.dao.HeroDao;
import com.fithero.fithero.model.entity.Hero;
import com.fithero.fithero.model.enums.MuscleGroup;
import com.fithero.fithero.model.entity.MuscleAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HeroService {
    /*
     * Future Idea, can come up with different difficulties, can do like hardcore where you can also track food and macros
     * Lots of work involved, more end to end product
     */
    
    @Autowired
    private HeroDao heroDao;
    
    public Hero createHero(String name, Integer height, Integer weight) {
        if (heroDao.existsByName(name)) {
            throw new IllegalArgumentException("Hero with name '" + name + "' already exists");
        }
        
        Hero hero = new Hero();
        hero.setName(name);
        hero.setHeight(height);
        hero.setWeight(weight);
        hero.setLevel(1);
        hero.setExperience(0L);
        
        return heroDao.save(hero);
    }

    @Transactional(readOnly = true)
    public Hero getHeroById(Long id) {
        Hero hero = heroDao.findById(id);
        if (hero == null) {
            throw new IllegalArgumentException("Hero with id " + id + " not found");
        }
        return hero;
    }
    
    @Transactional(readOnly = true)

    public List<Hero> getAllHeroes() {
        return heroDao.findAll();
    }
    
    public Hero updateHero(Long id, String name, Integer height, Integer weight) {
        Hero hero = getHeroById(id);
        hero.setName(name);
        hero.setHeight(height);
        hero.setWeight(weight);
        return heroDao.save(hero);
    }
    
    public void deleteHero(Long id) {
        Hero hero = getHeroById(id);
        heroDao.delete(id);
    }
    
    // Game Progression Logic
    public void addExperience(Long heroId, Long experience) {
        Hero hero = getHeroById(heroId);
        hero.setExperience(hero.getExperience() + experience);
        checkLevelUp(hero);
        heroDao.save(hero);
    }
    
    public void levelUpHero(Long heroId) {
        Hero hero = getHeroById(heroId);
        hero.setLevel(hero.getLevel() + 1);
        hero.setExperience(0L); // Reset experience for next level
        
        // TODO: Add stat points or other level-up rewards
        // This is where you'll add your level-up logic
        
        heroDao.save(hero);
    }
    
    public void updateMuscleStats(Long heroId, MuscleGroup muscleGroup, int strengthGain, int staminaGain) {
        Hero hero = getHeroById(heroId);
        MuscleAttributes stats = hero.getMuscleStats().get(muscleGroup);
        
        if (stats != null) {
            stats.setStrength(stats.getStrength() + strengthGain);
            stats.setStamina(stats.getStamina() + staminaGain);
        }
        
        heroDao.save(hero);
    }

    @Transactional(readOnly = true)
    public MuscleGroup getStrongestMuscleGroup(Long heroId) {
        Hero hero = getHeroById(heroId);
        return hero.getMuscleStats().entrySet().stream()
            .max((entry1, entry2) -> Integer.compare(
                entry1.getValue().getStrength(), 
                entry2.getValue().getStrength()))
            .map(entry -> entry.getKey())
            .orElse(MuscleGroup.CHEST);
    }
    
    @Transactional(readOnly = true)
    public List<Hero> getHeroesByLevel(Integer level) {
        return heroDao.findByLevel(level);
    }
    
    @Transactional(readOnly = true)
    public List<Hero> getStrongHeroesInGroup(MuscleGroup group, Integer minStrength) {
        return heroDao.findStrongHeroesInGroup(group, minStrength);
    }
    
    // Private helper methods
    private void checkLevelUp(Hero hero) {
        long requiredExp = calculateRequiredExperience(hero.getLevel());
        if (hero.getExperience() >= requiredExp) {
            levelUpHero(hero.getId());
        }
    }
    
    /*
     * TODO: Required Experience
     * Consider time in the calculation
     * Consider do I want experience to be more of how experienced you are in the gym or more strength based or more based on gameplay???
     * NOTE: If its time based it can be steady experience gain, with jumps in trying new workouts;
     * If strength and experience are separate then there can be multipliers based on strength level v experience, eg weak but knowledgable multiplier 
     * The question is do I want this to be more of a game that is fueled by gym progress or a gym progress tracker that is a game?
     * PROS: 
     * CONS:
     * Ref: https://gamedev.stackexchange.com/questions/13638/algorithm-for-dynamically-calculating-a-level-based-on-experience-points
     * Question: Should the logic for time be calculated here or at the point of when the workout/experience is applied
     * -> Character level
     * PROS: Tight coupling with character stats eg stamina and time played potentially
     * CONS: Potenially called lots of times, used in checkLevelUp
     * WORKAROUND: ???
     * -> Workout level
     * PROS: Less data will be sent around
     * CONS: Bloated code with repeated if statements
     * WORKAROUND: Helper method (still slight bloating)
     */
    private long calculateRequiredExperience(Integer level) {
        // TODO: Implement your leveling formula
        // Example: 1000 * level * level (quadratic growth)
        return 1000L * level * level;
    }
}
