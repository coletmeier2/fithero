package com.fithero.fithero.db.dao;

import com.fithero.fithero.model.entity.Workout;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class WorkoutDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Workout findById(Long id) {
        return entityManager.find(Workout.class, id);
    }
    
    public Optional<Workout> findByIdOptional(Long id) {
        Workout workout = entityManager.find(Workout.class, id);
        return Optional.ofNullable(workout);
    }
    
    public List<Workout> findAll() {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w", Workout.class);
        return query.getResultList();
    }
    
    public List<Workout> findByExerciseId(Long exerciseId) {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w WHERE w.exercise.id = :exerciseId ORDER BY w.performedAt DESC", Workout.class);
        query.setParameter("exerciseId", exerciseId);
        return query.getResultList();
    }
    
    public List<Workout> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end) {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w WHERE w.performedAt BETWEEN :start AND :end ORDER BY w.performedAt DESC", 
            Workout.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        return query.getResultList();
    }
    
    public List<Workout> findByHeroIdAndDateRange(Long heroId, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w WHERE w.hero.id = :heroId AND w.performedAt BETWEEN :start AND :end ORDER BY w.performedAt DESC", 
            Workout.class);
        query.setParameter("heroId", heroId);
        query.setParameter("start", start);
        query.setParameter("end", end);
        return query.getResultList();
    }
    
    public List<Workout> findByHeroIdAndExerciseId(Long heroId, Long exerciseId) {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w WHERE w.hero.id = :heroId AND w.exercise.id = :exerciseId ORDER BY w.performedAt DESC", 
            Workout.class);
        query.setParameter("heroId", heroId);
        query.setParameter("exerciseId", exerciseId);
        return query.getResultList();
    }
    
    public Workout save(Workout workout) {
        if (workout.getId() == null) {
            entityManager.persist(workout);
        } else {
            workout = entityManager.merge(workout);
        }
        return workout;
    }
    
    public void delete(Long id) {
        Workout workout = findById(id);
        if (workout != null) {
            entityManager.remove(workout);
        }
    }
    
    public boolean existsById(Long id) {
        return findById(id) != null;
    }
    
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(w) FROM Workout w", Long.class);
        return query.getSingleResult();
    }
    
    public long countByHeroId(Long heroId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(w) FROM Workout w WHERE w.hero.id = :heroId", Long.class);
        query.setParameter("heroId", heroId);
        return query.getSingleResult();
    }

    public List<Workout> findRecentWorkoutsByHeroId(Long heroId, int limit) {
        TypedQuery<Workout> query = entityManager.createQuery(
            "SELECT w FROM Workout w WHERE w.hero.id = :heroId ORDER BY w.performedAt DESC", Workout.class);
        query.setParameter("heroId", heroId);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
