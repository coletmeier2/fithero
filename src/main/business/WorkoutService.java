package com.fithero.fithero.business.services;

import com.fithero.fithero.db.dao.WorkoutDao;
import com.fithero.fithero.db.dao.HeroDao;
import com.fithero.fithero.db.dao.ExerciseDao;
import com.fithero.fithero.model.entity.Workout;
import com.fithero.fithero.model.entity.Hero;
import com.fithero.fithero.model.entity.Exercise;
import com.fithero.fithero.model.enums.MuscleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WorkoutService {
    
    @Autowired
    private WorkoutDao workoutDao;
    
    @Autowired
    private HeroDao heroDao;
    
    @Autowired
    private ExerciseDao exerciseDao;
    
    @Autowired
    private HeroService heroService;
    
    // Basic CRUD Operations
    public Workout recordWorkout(Long heroId, Long exerciseId, Integer sets, Integer reps, 
                                Double weight, String notes) {
        Hero hero = heroDao.findById(heroId);
        if (hero == null) {
            throw new IllegalArgumentException("Hero with id " + heroId + " not found");
        }
        
        Exercise exercise = exerciseDao.findById(exerciseId);
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise with id " + exerciseId + " not found");
        }
        
        Workout workout = new Workout();
        workout.setHero(hero);
        workout.setExercise(exercise);
        workout.setSets(sets);
        workout.setReps(reps);
        workout.setWeight(weight);
        workout.setPerformedAt(LocalDateTime.now());
        workout.setNotes(notes);
        
        // Apply progression gains
        applyWorkoutGains(workout);
        
        return workoutDao.save(workout);
    }
    
    public Workout getWorkoutById(Long id) {
        Workout workout = workoutDao.findById(id);
        if (workout == null) {
            throw new IllegalArgumentException("Workout with id " + id + " not found");
        }
        return workout;
    }
    
    public List<Workout> getWorkoutsByHero(Long heroId) {
        return workoutDao.findByHeroId(heroId);
    }
    
    public List<Workout> getWorkoutsByExercise(Long exerciseId) {
        return workoutDao.findByExerciseId(exerciseId);
    }
    
    public List<Workout> getWorkoutsByDateRange(LocalDateTime start, LocalDateTime end) {
        return workoutDao.findByPerformedAtBetween(start, end);
    }
    
    public List<Workout> getHeroWorkoutsByDateRange(Long heroId, LocalDateTime start, LocalDateTime end) {
        return workoutDao.findByHeroIdAndDateRange(heroId, start, end);
    }
    
    public void deleteWorkout(Long id) {
        Workout workout = getWorkoutById(id);
        workoutDao.delete(id);
    }
    
    // Game Progression Logic
    private void applyWorkoutGains(Workout workout) {
        Hero hero = workout.getHero();
        Exercise exercise = workout.getExercise();
        
        // Calculate intensity (this is where you'll fine-tune the formula)
        double intensity = calculateIntensity(workout);
        
        // Apply gains to each targeted muscle group
        Map<MuscleGroup, Integer> targetWeights = exercise.getTargetWeights();
        for (Map.Entry<MuscleGroup, Integer> entry : targetWeights.entrySet()) {
            MuscleGroup group = entry.getKey();
            double weightPercent = entry.getValue() / 100.0;
            
            // Calculate gains (these formulas need fine-tuning)
            int strengthGain = calculateStrengthGain(intensity, weightPercent);
            int staminaGain = calculateStaminaGain(workout.getReps(), workout.getSets(), weightPercent);
            
            // Apply gains to hero
            heroService.updateMuscleStats(hero.getId(), group, strengthGain, staminaGain);
        }
        
        // Add experience based on workout
        long experienceGain = calculateExperienceGain(workout);
        heroService.addExperience(hero.getId(), experienceGain);
    }
    
    // Calculation methods (these need fine-tuning based on your game balance)
    private double calculateIntensity(Workout workout) {
        // TODO: Fine-tune this formula
        // Current: weight * reps * sets
        // You might want to add hero level, exercise difficulty, etc.
        return workout.getWeight() * workout.getReps() * workout.getSets();
    }
    
    private int calculateStrengthGain(double intensity, double weightPercent) {
        // TODO: Fine-tune this formula
        // Current: (intensity * weightPercent * 0.002) + 1
        // You might want different formulas for different exercise types
        return Math.max(1, (int) Math.round(intensity * weightPercent * 0.002));
    }
    
    private int calculateStaminaGain(Integer reps, Integer sets, double weightPercent) {
        // TODO: Fine-tune this formula
        // Current: (reps * sets * weightPercent * 0.1) + 1
        // You might want different formulas for different exercise types
        return Math.max(1, (int) Math.round(reps * sets * weightPercent * 0.1));
    }
    
    private long calculateExperienceGain(Workout workout) {
        // TODO: Fine-tune this formula
        // Current: (intensity / 100) + 1
        // You might want to consider hero level, exercise difficulty, etc.
        double intensity = calculateIntensity(workout);
        return Math.max(1, (long) Math.round(intensity / 100));
    }
    
    // Analytics methods
    public List<Workout> getRecentWorkouts(Long heroId, int limit) {
        List<Workout> workouts = workoutDao.findByHeroId(heroId);
        return workouts.stream()
            .limit(limit)
            .toList();
    }
    
    public long getTotalWorkoutsByHero(Long heroId) {
        return workoutDao.countByHeroId(heroId);
    }
}