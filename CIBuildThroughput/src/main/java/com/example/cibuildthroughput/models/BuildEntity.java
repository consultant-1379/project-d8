package com.example.cibuildthroughput.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BUILDDATA")
public class BuildEntity implements Serializable {

    @Id
    private String jobName;

    @Id
    private long buildNum;

    private long startTime;
    private long endTime;
    private long durationTime;
    private long intervalTime;

    public BuildEntity() {
    }

    public BuildEntity(String jobName, long buildNum, long startTime, long endTime, long durationTime, long intervalTime) {
        this.jobName = jobName;
        this.buildNum = buildNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime = durationTime;
        this.intervalTime = intervalTime;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(long buildNum) {
        this.buildNum = buildNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    @Override
    public String toString() {
        return String.format("JOB: %s BUILDNO: %d START: %d END: %d DURATION: %d INTERVAL: %d", jobName, buildNum, startTime, endTime, durationTime, intervalTime);
    }
}
