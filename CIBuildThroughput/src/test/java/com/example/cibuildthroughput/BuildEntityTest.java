package com.example.cibuildthroughput;

import com.example.cibuildthroughput.models.BuildEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@SpringBootTest
class BuildEntityTest {
    private BuildEntity testEntity = new BuildEntity("job", 10, 60, 1234, 4321, 20);


    @Test
    void testGetJobDetails() {
        assertEquals("job", testEntity.getJobName());
        assertEquals(10, testEntity.getBuildNum());
        assertEquals(4321, testEntity.getDurationTime());
        assertEquals(60, testEntity.getStartTime());
        assertEquals(1234, testEntity.getEndTime());
        assertEquals(20, testEntity.getIntervalTime());
    }

    @Test
    void testSetJobDetails() {
        testEntity.setJobName("ChangedName");
        testEntity.setBuildNum(20);
        testEntity.setDurationTime(8642);
        testEntity.setStartTime(120);
        testEntity.setEndTime(2468);
        testEntity.setIntervalTime(40);

        assertEquals("ChangedName", testEntity.getJobName());
        assertEquals(20, testEntity.getBuildNum());
        assertEquals(8642, testEntity.getDurationTime());
        assertEquals(120, testEntity.getStartTime());
        assertEquals(2468, testEntity.getEndTime());
        assertEquals(40, testEntity.getIntervalTime());
    }

    @Test
    void testToString() {
        String testString = testEntity.toString();
        assertThat(testString, is("JOB: job BUILDNO: 10 START: 60 END: 1234 DURATION: 4321 INTERVAL: 20"));
    }

}
