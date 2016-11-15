package com.springmvc.services;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.springmvc.models.OperationLog;
import com.springmvc.models.OperationLogEvent;
import com.springmvc.models.OperationLogCategory;
import com.springmvc.models.OperationLogRiskLevel;
import com.springmvc.repositories.OperationLogRepository;
import com.springmvc.utils.DateTimeUtil;

@Service
public class OperationLogService implements IOperationLogService {

	@Autowired OperationLogRepository operationLogRepository;
	@Autowired ISecurityService securityService;
	
	@Override
	public void logCreateOption(Object createdObj) {
		// 这里执行mustable套对应的模板
		String message = getMustableContent("create", createdObj);
		
		OperationLog operationLog = new OperationLog();
		operationLog.setEvent(OperationLogEvent.CREATE);
		operationLog.setMessage(message);
		operationLog.setCategory(OperationLogCategory.ADMIN);
		operationLog.setRiskLevel(OperationLogRiskLevel.SAFT);
		operationLog.setUser(securityService.findLoggedInUser());
		operationLog.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		operationLogRepository.save(operationLog);
	}

	@Override
	public void logUpdateOption(Object oldObj, Object updatedObj) {
		// 这里执行mustable套对应的模板
		String message = getMustableContent("update", updatedObj);
		
		OperationLog operationLog = new OperationLog();
		operationLog.setEvent(OperationLogEvent.UPDATE);
		operationLog.setMessage(message);
		operationLog.setCategory(OperationLogCategory.ADMIN);
		operationLog.setRiskLevel(OperationLogRiskLevel.LOW);
		operationLog.setUser(securityService.findLoggedInUser());
		operationLog.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		operationLogRepository.save(operationLog);
	}

	@Override
	public void logDeleteOption(Object deletedObj) {
		
	}
	
	private String getMustableContent(String optAction, Object obj) {
		StringBuffer fileNameBuf = new StringBuffer();
		String basePath = "operation_log_templates/";
		// operation_logs/user_created_log.mustable
		String clazzName = obj.getClass().getSimpleName().toLowerCase();
		fileNameBuf = fileNameBuf.append(basePath).append(clazzName).append("_").append(optAction).append("_log.mustache");
		
	    Mustache mustache = readMustacheFromTemplate(fileNameBuf.toString());
	    
	    String renderResult = null;
	    try {
	    	StringWriter writer = new StringWriter(); // new PrintWriter(System.out)
			mustache.execute(writer, obj).flush();
			
			renderResult = writer.toString();
		} catch (IOException e) {
			// log the error
		}
	    
	    return renderResult;
	}
	
	private Mustache readMustacheFromTemplate(String templateFilePath) {
		MustacheFactory mf = new DefaultMustacheFactory();
	    Mustache mustache = mf.compile(templateFilePath);
	    return mustache;
	}
	
    
}
