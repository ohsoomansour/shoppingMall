package com.shoppingmall.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *@Date: 24.10.14 (월)
 *@Explain:  Start a job execution for the given Job and JobParameters
 * - Job은 이름이 다르게 관리가능? 가능하다.
 * <ChunkBatchConfig>
 * @Bean
 * public Job job1(){};
 * public Job job2(){};
 * <BatchController>
 *   @Autowired Job job1;
 *   @Autowired Job job2; 
 *   @Autowired JobLauncher jobLauncher;
 *   -> jobLauncher.run(job1, jobParameters);
 *   -> jobLauncher.run(job2, jobParameters);
 * */
@RestController
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher; // 특정 배치 작업을 실행하는 기능을 제공. job 객체와 

    @Autowired
    private Job memberJob;

    @GetMapping("/run-batch")
    public Map<String, Object> runBatchJob() {
    	Map<String, Object> response = new HashMap<>();
        try {
        	JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())  // 유니크한 파라미터로 사용 가능
                    .toJobParameters();
        	response.put("message", "Batch job has been invoked");
            jobLauncher.run(memberJob, jobParameters);
            return response;
        } catch (Exception e) {
        	  response.put("message", "Error occurred while running batch: " + e.getMessage());
        }
        return response;
    }
}
