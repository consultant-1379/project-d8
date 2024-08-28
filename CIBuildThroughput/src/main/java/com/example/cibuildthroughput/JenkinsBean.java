package com.example.cibuildthroughput;

import com.example.cibuildthroughput.models.BuildEntity;
import com.example.cibuildthroughput.repository.DashBoardRepositoryImpl;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Async
public class JenkinsBean {

    @Autowired
    private DashBoardRepositoryImpl repository;

    @Scheduled(fixedDelay = 900000)
    public void scheduledTask() throws URISyntaxException, IOException {
        // Username and password for authenticating Jenkins API
        String user = "ekotkon";
        String pass = "1133194bd5e8b61bafa9b77d3aba047fbc";

        List<String> jenkinJobNames = new ArrayList<>();
        jenkinJobNames.add("eric-oss-ran-topology-adapter_Publish");
        jenkinJobNames.add("eric-oss-ran-topology-adapter_PreCodeReview");
        jenkinJobNames.add("eric-oss-enm-discovery-adapter_Publish");
        jenkinJobNames.add("eric-oss-enm-discovery-adapter_PreCodeReview");
        jenkinJobNames.add("eric-oss-enm-model-adapter_Publish");
        jenkinJobNames.add("eric-oss-enm-model-adapter_PreCodeReview");
        jenkinJobNames.add("eric-oss-enm-notification-adapter_Publish");
        jenkinJobNames.add("eric-oss-enm-notification-adapter_PreCodeReview");

        // Create a Jenkins server connection
        JenkinsServer jenkinsServer = connectToJenkinsServer(user, pass);

        // Get all jobs and store them in a map
        Map<String, Job> jobs = retrieveJenkinsJobs(jenkinsServer);

        for (String jobName : jenkinJobNames) {
            // Get builds by job name
            List<Build> builds = retrieveSuccessfulBuildsByJobName(jobs, jobName);

            // Retrieve build metrics
            retrieveBuildDetails(jobName, builds);
        }
        // Close Jenkins connection
        jenkinsServer.close();
    }

    public static JenkinsServer connectToJenkinsServer(String user, String pass) throws URISyntaxException {
        // URI for the Ericsson Jenkins server
        String uri = "https://fem1s11-eiffel216.eiffel.gic.ericsson.se:8443/jenkins/";

        return  new JenkinsServer(new URI(uri), user, pass);
    }

    public static Map<String, Job> retrieveJenkinsJobs(JenkinsServer jenkinsServer) throws IOException {
        return jenkinsServer.getJobs();
    }

    public static List<Build> retrieveSuccessfulBuildsByJobName(Map<String, Job> jobs, String jobName) throws IOException {
        JobWithDetails job = jobs.get(jobName).details();
        List<Build> builds = job.getAllBuilds();
        List<Build> successfulBuilds = new ArrayList<>();

        for (int i = 0; i < builds.size(); i++) {
            if (builds.get(i).details().getResult() == BuildResult.SUCCESS) {
                successfulBuilds.add(builds.get(i));
            }
        }

        return successfulBuilds;
    }

    public void retrieveBuildDetails(String jobName, List<Build> builds) throws IOException {
        for (int i = 0; i < builds.size(); i++) {
            // Num of the build
            long buildNum = builds.get(i).getNumber();
            // Build duration of the build
            long buildDuration = builds.get(i).details().getDuration();
            // Start time of the build
            long buildStart = builds.get(i).details().getTimestamp();
            // End time of the build
            long buildEnd = buildStart + buildDuration;
            // Build interval between N+1 builds
            long buildInterval = 0;

            // Try calculating the interval
            try {
                buildInterval = calculateBuildInterval(buildStart, (builds.get(i + 1).details().getTimestamp()));
            } catch (IndexOutOfBoundsException e) {
                // If the index is out of bounds, we are at the last build, thus assign build interval of 0
                buildInterval = 0;
            }

            // Insert build data to database
            BuildEntity b = new BuildEntity(jobName, buildNum, buildStart, buildEnd, buildDuration, buildInterval);
            try {
                repository.insertBuild(b);
            } catch (DataIntegrityViolationException e) {
                // Do nothing when exception occurs
            }
        }
    }

    public static long calculateBuildInterval(long succeedingBuildStart, long precedingBuildEnd) {
        return succeedingBuildStart - precedingBuildEnd;
    }

}
