package com.example.cibuildthroughput.repository;

import com.example.cibuildthroughput.models.BuildEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DashboardRepository {

    Collection<BuildEntity> getBuildData();

    // get data for individual build
    Collection<BuildEntity> getDashboardData(String jobname);

    @Transactional
    void insertBuild(BuildEntity b);

    @Transactional
    String addNewJob(String job);
}
