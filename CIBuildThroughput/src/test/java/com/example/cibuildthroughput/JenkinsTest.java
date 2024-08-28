package com.example.cibuildthroughput;

import com.example.cibuildthroughput.models.BuildEntity;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.Job;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class JenkinsTest {
    String user = "ekotkon";
    String pass = "1133194bd5e8b61bafa9b77d3aba047fbc";
    String[] jenkinsJobs = {"eric-oss-ran-topology-adapter_Publish", "eric-oss-ran-topology-adapter_PreCodeReview"};

    @Test
    void testServerConnected() throws URISyntaxException {
        assertThat(JenkinsBean.connectToJenkinsServer(user, pass).isRunning(), is(Boolean.TRUE));
    }

    @Test
    void testRetrievingJobs() throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsBean.connectToJenkinsServer(user, pass);
        Map<String, Job> jobs = JenkinsBean.retrieveJenkinsJobs(jenkinsServer);

        assertThat(jobs.size(), is(notNullValue()));
    }

    @Test
    void testRetrievingSuccessfulBuilds() throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsBean.connectToJenkinsServer(user, pass);
        Map<String, Job> jobs = JenkinsBean.retrieveJenkinsJobs(jenkinsServer);
        List<Build> successfulBuilds = JenkinsBean.retrieveSuccessfulBuildsByJobName(jobs, "eric-oss-ran-topology-adapter_Publish");

        org.assertj.core.api.AssertionsForClassTypes.assertThat(successfulBuilds.stream().anyMatch(element -> {
            try {
                return element.details().getResult() == (BuildResult.SUCCESS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testCalculatingBuildInterval() {
        long actualInterval = JenkinsBean.calculateBuildInterval(10000, 5000);
        assertThat(5000L, is(actualInterval));
    }

}