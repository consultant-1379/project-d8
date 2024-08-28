package com.example.cibuildthroughput;

import com.example.cibuildthroughput.repository.DashboardRepository;
import net.minidev.json.parser.ParseException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class DashboardRepositoryTest {

    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private DashboardRestController dashboardRestController;

    @Test
    void testGetJobs() {
        assertThat(this.dashboardRestController.getAllDashboardData().getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void testGetInvalidJob() throws JSONException, ParseException {
        assertThat(this.dashboardRestController.getDashboardData("test").getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testGetValidJob() throws JSONException, ParseException {
        assertThat(this.dashboardRestController.getDashboardData("eric-oss-ran-topology-adapter_Publish").getStatusCode(), is(HttpStatus.OK));
    }
}