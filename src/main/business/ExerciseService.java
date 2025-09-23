package com.fithero.fithero.business.services;

import com.fithero.fithero.db.dao.ExerciseDao;
import com.fithero.fithero.model.entity.Exercise;
import com.fithero.fithero.model.enums.ExerciseType;
import com.fithero.fithero.model.enums.MuscleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExerciseService {
    
    @Autowired
    private ExerciseDao exerciseDao;
    
    public Exercise createExercise(String name, String description, ExerciseType exerciseType, 
                                 MuscleGroup primaryMuscleGroup, Map<MuscleGroup, Integer> targetWeights) {
        if (exerciseDao.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '" + name + "' already exists");
        }
        
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setExerciseType(exerciseType);
        exercise.setMuscleGroup(primaryMuscleGroup);
        exercise.setTargetWeights(targetWeights);
        
        return exerciseDao.save(exercise);
    }
    
    public Exercise getExerciseById(Long id) {
        Exercise exercise = exerciseDao.findById(id);
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise with id " + id + " not found");
        }
        return exercise;
    }
    
    public List<Exercise> getAllExercises() {
        return exerciseDao.findAll();
    }
    
    public Exercise updateExercise(Long id, String name, String description, ExerciseType exerciseType, 
                                 MuscleGroup primaryMuscleGroup, Map<MuscleGroup, Integer> targetWeights) {
        Exercise exercise = getExerciseById(id);
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setExerciseType(exerciseType);
        exercise.setMuscleGroup(primaryMuscleGroup);
        exercise.setTargetWeights(targetWeights);
        
        return exerciseDao.save(exercise);
    }
    
    public void deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exerciseDao.delete(id);
    }
    
    // Query Methods
    public List<Exercise> getExercisesByMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseDao.findByMuscleGroup(muscleGroup);
    }
    
    public List<Exercise> getExercisesByType(ExerciseType exerciseType) {
        return exerciseDao.findByExerciseType(exerciseType);
    }
    
    public List<Exercise> getExercisesByMuscleGroupAndType(MuscleGroup muscleGroup, ExerciseType exerciseType) {
        return exerciseDao.findByMuscleGroupAndExerciseType(muscleGroup, exerciseType);
    }
    
    public List<Exercise> searchExercisesByName(String name) {
        return exerciseDao.findByNameContaining(name);
    }
    
    // Game-specific methods
    // worth looking back at if I want to make it a front end or back end filter
    public List<Exercise> getRecommendedExercisesForMuscleGroup(MuscleGroup muscleGroup) {
        // TODO: Implement recommendation logic
        // Could be based on hero's current stats, preferences, etc.
        return exerciseDao.findByMuscleGroup(muscleGroup);
    }
}