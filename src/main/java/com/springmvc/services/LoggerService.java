package com.springmvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.models.OperationLog;
import com.springmvc.models.OperationLogRiskLevel;
import com.springmvc.repositories.OperationLogRepository;

@Service
public class LoggerService {
	// private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);
	@Autowired OperationLogRepository operationLogRepository;

    public void lowRisk(String message) {
    	OperationLog log = new OperationLog();
    	log.setRiskLevel(OperationLogRiskLevel.LOW);
    	saveLog(log);
    }
    
    private void saveLog(OperationLog log) {
    	operationLogRepository.save(log);
    }
}
