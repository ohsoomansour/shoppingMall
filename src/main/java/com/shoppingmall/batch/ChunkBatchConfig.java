package com.shoppingmall.batch;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.shoppingmall.toaf.object.DataMap;

/**
 *@SpringBoot3이상: java17이상써야한다.(따라서 부트 프로젝트이면 17버전을 사용할 것으로 예상) 
 * - 변경내용: 'javax' -> 'jakarta'로 패키지가 변경
 *@Batch방식 :  Chunk 방식
 * - Reader, DB에서 데이터를 읽는다. -> Processor(읽어온 데이터를 처리) -> Writer(데이터를 저장) 방식
 * - 하나의 어플리케이션에서 '2개'의 Batch job은 지원하지 않는다.
 *@AS IS :JobBuilderFactory Deprecated : '5버전 이후 사용하지 않'
 *  - The type JobBuilderFactory has been deprecated since version 5.0.0 and marked for removal 
 *  
 *@TO BE: 5.0 
 *  변경된 내용1.return new JobBuilder("osmTest", jobRepository).incrementer(...)
 *  변경된 내용2. @Step의 'chunk', 'tasklet'에서 @transactionManager를 인자로 넣어줘야 한다. 
 *   -> @chunk(10, transactionManager) 
 *   -> 10개의 의미는 한 번에 처리할 아이템의 수를 의미
 *@TransactionManager: 대량의 데이터 처리 과정에서 트랜잭션을 관리하고, 일괄 처리(batch processing)의 안전성과 일관성을 유지
 * 역할1. Step 내 트랜잭션 관리, @Step은 데이터 읽기, 처리, 쓰기 등의 작업을 포함하는 하나의 작업 단위. step은 독립적인 트랜잭션 경계를 가짐
 *       TransactionManager은 이 트랜잭션의 시작과 종료를 제어
 * 역할2. Chunk 기반 트랜잭션 관리, 주로 chunk 단위로 데이터를 처리하며 각 chunk는 데이터 읽기, 처리, 쓰기 사이클로 구성
 *      transactionManager는 각 chunk에 대해트랜잭션을 관리, 특정 chunk가 성공적으로 처리되면 DB에 반영되도록 한다.
 * 역할3. SpringBatch는 여러 step이 연속적으로 실행될 수 있다. transactionManager는 각 Step 간의 트랜잭션 전파를 관리하고, 트랜잭션 격리 수준을 설정
 * 역할4. SpringBatch는 @RetryTemplate 또는 @SkipPolicy와 같은 기능을 사용 트랜잭션 오류 시 재시도하거나 특정 예외를 무시하고 다음 작업으로 넘어갈 수 있다.
 * #메타 데이터 테이블의 이해
 *  @BATCH_JOB_INSTANCE: 배치 작업은 Job이 실행될 때마다 새로운 인스턴스가 생성. 해당 Job 인스턴스를 저장하기 위한 테이블
 *  @BATCH_JOB_EXECUTION: 'Job 인스턴스에 대한 실행 정보'를 저장, 작업이 성공적으로 완료되었는지 또는 실패했는지와 같은 정보 
 *  @BATCH_STEP_EXECUTION: '각 배치 Step의 실행 정보를 저장', 하나 이상의 테스크를 수행한 Step의 실행 정보가 저장.
 *  @BATCH_STEP_EXECUTION_PARAMS: 배치 작업 실행에 전달된 매개변수를 저장. 배치 작업이 실행될 때 전달된 매개변수가 여기에 저장
 *  @BATCH_JOB_SEQ: 배치 작업 인스턴스 및 실행 정보에 대한 고유 식별자를 생성하는 데 사용
 *@PazeSize: 한번에 조회할 item의 양  vs @ChunkSize: '한번에 처리될 트랜잭션 단위' 
 *  JobRepository jobRepository, PlatformTransactionManager transactionManager
 *
 * */

@Configuration
public class ChunkBatchConfig {

  @Bean
  public Job memberJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, Step memberStep) {
	  return new JobBuilder("memberJob", jobRepository)
			  .start(memberStep)
			  .build();
  }
  
  @Bean
  public Step memberStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
		  MyBatisPagingItemReader<DataMap> myBatisPagingItemReader,
          ItemProcessor<DataMap, DataMap> memberProcessor,
          ItemWriter<DataMap> memberWriter) {
	  return new StepBuilder("memberStep", jobRepository)
			  .<DataMap, DataMap>chunk(10, transactionManager)
			  .reader(myBatisPagingItemReader)
			  .processor(memberProcessor)
			  .writer(memberWriter)
			  .build();
  }
  
 /**
   *@Explain:  
   * */
  @Bean
  public MyBatisPagingItemReader<DataMap> myBatisPagingItemReader(SqlSessionFactory sqlSessionFactory){
	  MyBatisPagingItemReader<DataMap> reader = new MyBatisPagingItemReader<>();
	  reader.setSqlSessionFactory(sqlSessionFactory); 
	  reader.setQueryId("SecMemberSQL.getOneMemberByEmail");
	  //파라미터 설정 (email을 매퍼 쿼리에 전달)
	  DataMap parameterValues = new DataMap();
	  parameterValues.put("email", "admin@naver.com");
	  reader.setParameterValues(parameterValues);
	  reader.setPageSize(10);  //페이지 크기 설정
	  return reader;
  }
  @Bean
  public ItemProcessor<DataMap, DataMap> memberProcessor() {
	  return member -> {
		  String user_name = member.getstr("user_name");
		  String updatedUserName = user_name + "10.13";
	        
	        // 변경된 user_name을 DataMap에 다시 설정합니다.
	      member.put("user_name", updatedUserName);
	      System.out.println("Processed user_name: " + updatedUserName); // 로그 추가
		return member;
	  };
  };
  //처리된 데이터를 출력
  @Bean
  public ItemWriter<DataMap> memberWriter(SqlSessionFactory sqlSessionFactory) {
      return items -> {
          try (var session = sqlSessionFactory.openSession()) {
              for (DataMap item : items) {
            	  System.out.println("ing.."); //일단 여기 안 들어오고 있음
                  session.update("SecMemberSQL.updateUserName", item);
              }
              System.out.println("========== commit ==========");
              session.commit();
          }
      };
  }

  @Bean
  public JobExecutionListener listener() {
      return new JobExecutionListener() {
          @Override
          public void beforeJob(JobExecution jobExecution) {
              System.out.println("Job started: " + jobExecution.getJobInstance().getJobName());
          }

          @Override
          public void afterJob(JobExecution jobExecution) {
              System.out.println("Job finished: " + jobExecution.getJobInstance().getJobName());
          }
      };
  }
  
}
