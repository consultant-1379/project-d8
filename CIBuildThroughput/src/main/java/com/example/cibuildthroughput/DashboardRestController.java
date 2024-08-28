package com.example.cibuildthroughput;

import com.example.cibuildthroughput.models.BuildEntity;
import com.example.cibuildthroughput.repository.DashboardRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardRestController {

    @Autowired
    private DashboardRepository repository;

    @GetMapping(produces = {"application/json","application/xml"})
    public ResponseEntity<String> getAllDashboardData() {
        Collection<BuildEntity> data = repository.getBuildData();

        if(data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(getJsonData(data));
    }

    @GetMapping(value = "/job", produces = {"application/json"})
    public ResponseEntity<String> getDashboardData(@RequestParam String jobName) {
        Collection<BuildEntity> data = repository.getDashboardData(jobName);

        if(data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(getJsonData(data));
    }

    private String getJsonData(Collection<BuildEntity> data) {
        List<Long> buildDuration = new ArrayList<>();
        List<Long> buildIntervals = new ArrayList<>();
        LocalDateTime[] buildTime = new LocalDateTime[data.size()];
        ConvertDateTime convert = new ConvertDateTime();

        for (int i=0; i<data.size(); i++) {
            BuildEntity b = (BuildEntity) data.toArray()[i];
            buildDuration.add(b.getDurationTime());
            buildIntervals.add(b.getIntervalTime());
            buildTime[i] = convert.convertToDateTime(b.getStartTime());
        }

        Metrics metrics = new Metrics(buildDuration, buildIntervals, buildTime);
        return new Gson().toJson(metrics);
    }
}
