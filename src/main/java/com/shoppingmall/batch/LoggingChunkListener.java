package com.shoppingmall.batch;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingChunkListener implements ChunkListener {


	//Chunk 실행 전
	@Override
	public void beforeChunk(ChunkContext context) {
		StepContext stepContext = context.getStepContext();
		StepExecution stepExecution = stepContext.getStepExecution();
		log.info("###### beforeChunk : " + stepExecution.getReadCount());
	}

	//Chunk 실행 후
	@Override
	public void afterChunk(ChunkContext context) {	
		StepContext stepContext = context.getStepContext();
		StepExecution stepExecution = stepContext.getStepExecution();
		log.info("###### afterChunk : " + stepExecution.getCommitCount());
	}

	//Chunk 수행 중 Error가 발생했을시
	@Override
	public void afterChunkError(ChunkContext context) {
		StepContext stepContext = context.getStepContext();
		StepExecution stepExecution = stepContext.getStepExecution();
		log.info("##### afterChunkError : " + stepExecution.getRollbackCount());
	}

}
