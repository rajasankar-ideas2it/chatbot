package com.ideas2it.chatbot.service;

import java.util.Map;

public interface IdeasWebHookService {

	Map<String, Object> addEmployee(Map<String, Object> requestParameters);

	Map<String, Object> getDailyReport(Map<String, Object> requestParameters) throws Exception;

	Map<String, Object> updateTimeSheetForDay(Map<String, Object> requestParameters) throws Exception;

	Map<String, Object> updateEmployee(Map<String, Object> requestParameters) throws Exception;

	Map<String, Object> updateTimeSheetForPeriod(Map<String, Object> requestParameters) throws Exception;

}
