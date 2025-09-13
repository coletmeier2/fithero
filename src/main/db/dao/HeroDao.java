package com.fithero.fithero.db.dao;

import com.fithero.fithero.model.entity.Hero;
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
public class HeroDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Hero findById(Long id) {
        return entityManager.find(Hero.class, id);
    }
    
    public Optional<Hero> findByIdOptional(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        return Optional.ofNullable(hero);
    }
    
    public Hero findByName(String name) {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h WHERE h.name = :name", Hero.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Hero> findAll() {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h", Hero.class);
        return query.getResultList();
    }
    
    public List<Hero> findByLevelGreaterThan(Integer level) {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h WHERE h.level > :level", Hero.class);
        query.setParameter("level", level);
        return query.getResultList();
    }
    
    public List<Hero> findByLevel(Integer level) {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h WHERE h.level = :level", Hero.class);
        query.setParameter("level", level);
        return query.getResultList();
    }
    
    public Hero save(Hero hero) {
        if (hero.getId() == null) {
            entityManager.persist(hero);
        } else {
            hero = entityManager.merge(hero);
        }
        return hero;
    }
    
    public void delete(Long id) {
        Hero hero = findById(id);
        if (hero != null) {
            entityManager.remove(hero);
        }
    }
    
    public boolean existsById(Long id) {
        return findById(id) != null;
    }
    
    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(h) FROM Hero h WHERE h.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
    
    public List<Hero> findStrongHeroesInGroup(MuscleGroup group, Integer minStrength) {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h JOIN h.muscleStats ms WHERE ms.key = :group AND ms.value.strength > :minStrength", 
            Hero.class);
        query.setParameter("group", group);
        query.setParameter("minStrength", minStrength);
        return query.getResultList();
    }
    
    public List<Hero> findByLevelOrderByExperienceDesc(Integer level) {
        TypedQuery<Hero> query = entityManager.createQuery(
            "SELECT h FROM Hero h WHERE h.level = :level ORDER BY h.experience DESC", Hero.class);
        query.setParameter("level", level);
        return query.getResultList();
    }
    
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(h) FROM Hero h", Long.class);
        return query.getSingleResult();
    }
}
