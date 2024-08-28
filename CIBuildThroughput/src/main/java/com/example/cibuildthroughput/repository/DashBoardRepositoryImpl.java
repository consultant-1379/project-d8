package com.example.cibuildthroughput.repository;

import com.example.cibuildthroughput.models.BuildEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class DashBoardRepositoryImpl implements DashboardRepository{

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Collection<BuildEntity> getBuildData() {
        String jpql = "SELECT p from BuildEntity p";
        TypedQuery<BuildEntity> query = entityManager.createQuery(jpql, BuildEntity.class);
        return query.getResultList();
    }

    @Override
    public Collection<BuildEntity> getDashboardData(String jobName) {
        return entityManager.createQuery("select e from BuildEntity e where e.jobName = :jobName",
                BuildEntity.class).setParameter("jobName", jobName).getResultList();
    }

    @Override
    @Transactional
    public void insertBuild(BuildEntity b) {
        entityManager.persist(b);
        entityManager.flush();
    }

    @Override
    @Transactional
    public String addNewJob(String job) {
        return null;
    }
}
