package com.mitchinmat.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mitchinmat.domain.elasticsearch.application.ElasticDataConsistencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseScheduler {

	private final ElasticDataConsistencyService consistencyService;

	@Scheduled(fixedRate = 6000000)
	public void scheduleDataConsistencyCheck() {
		log.info("Starting data consistency check...");
		// consistencyService.checkAndFixDataConsistency();
		log.info("Data consistency check completed.");
	}

}
