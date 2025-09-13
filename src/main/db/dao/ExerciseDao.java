package com.fithero.fithero.db.dao;

import com.fithero.fithero.model.entity.Exercise;
import com.fithero.fithero.model.enums.ExerciseType;
import com.fithero.fithero.model.enums.MuscleGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ExerciseDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Exercise findById(Long id) {
        return entityManager.find(Exercise.class, id);
    }
    
    public Optional<Exercise> findByIdOptional(Long id) {
        Exercise exercise = entityManager.find(Exercise.class, id);
        return Optional.ofNullable(exercise);
    }
    
    public List<Exercise> findAll() {
        TypedQuery<Exercise> query = entityManager.createQuery(
            "SELECT e FROM Exercise e", Exercise.class);
        return query.getResultList();
    }
    
    public List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup) {
        TypedQuery<Exercise> query = entityManager.createQuery(
            "SELECT e FROM Exercise e WHERE e.muscleGroup = :muscleGroup", Exercise.class);
        query.setParameter("muscleGroup", muscleGroup);
        return query.getResultList();
    }
    
    public List<Exercise> findByExerciseType(ExerciseType exerciseType) {
        TypedQuery<Exercise> query = entityManager.createQuery(
            "SELECT e FROM Exercise e WHERE e.exerciseType = :exerciseType", Exercise.class);
        query.setParameter("exerciseType", exerciseType);
        return query.getResultList();
    }
    
    public List<Exercise> findByMuscleGroupAndExerciseType(MuscleGroup muscleGroup, ExerciseType exerciseType) {
        TypedQuery<Exercise> query = entityManager.createQuery(
            "SELECT e FROM Exercise e WHERE e.muscleGroup = :muscleGroup AND e.exerciseType = :exerciseType", 
            Exercise.class);
        query.setParameter("muscleGroup", muscleGroup);
        query.setParameter("exerciseType", exerciseType);
        return query.getResultList();
    }
    
    public List<Exercise> findByNameContaining(String name) {
        TypedQuery<Exercise> query = entityManager.createQuery(
            "SELECT e FROM Exercise e WHERE e.name LIKE :name", Exercise.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    public Exercise save(Exercise exercise) {
        if (exercise.getId() == null) {
            entityManager.persist(exercise);
        } else {
            exercise = entityManager.merge(exercise);
        }
        return exercise;
    }
    
    public void delete(Long id) {
        Exercise exercise = findById(id);
        if (exercise != null) {
            entityManager.remove(exercise);
        }
    }
    
    public boolean existsById(Long id) {
        return findById(id) != null;
    }
    
    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Exercise e WHERE e.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
    
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Exercise e", Long.class);
        return query.getSingleResult();
    }
}
