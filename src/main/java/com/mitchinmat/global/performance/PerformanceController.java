package com.mitchinmat.global.performance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@RestController
public class PerformanceController {


	private final Timer apiTimer;  // Micrometer Timer 객체

	public PerformanceController(MeterRegistry registry) {
		this.apiTimer = registry.timer("api.execution.time");  // "api.execution.time" 메트릭 생성
	}

	@GetMapping("/actuator/test")
	public String testApi(@RequestParam(name = "count") int count) {
		return apiTimer.record(() -> {  // 실행시간 측정 시작
			for (int i = 0; i < count; i++) {
				Math.sqrt(i);  // 더미 연산
			}
			return "Done";  // 실행시간 측정 종료
		});
	}



}
