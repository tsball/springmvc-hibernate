package com.springmvc.services;


public interface IOperationLogService {

	void logCreateOption(Object createdObj);
	
	void logUpdateOption(Object oldObj, Object updatedObj);
	
	void logDeleteOption(Object deletedObj);
	
}
