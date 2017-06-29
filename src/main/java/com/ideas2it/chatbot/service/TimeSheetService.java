package com.ideas2it.chatbot.service;

import java.util.List;
import java.util.Map;

public interface TimeSheetService {

	public List<List<Object>> getAllDaily() throws Exception;

	void updateTimeSheetForDay(Map<String, Object> requestParameters) throws Exception;

	void updateTimeSheetForPeriod(Map<String, Object> requestParam) throws Exception;

}
