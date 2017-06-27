package com.ideas2it.chatbot.service;

import java.util.List;
import java.util.Map;

public interface TimeSheetService {

	public List<List<Object>> getAllDaily() throws Exception;

	void addDailyReport(Map<String, Object> requestParameters) throws Exception;

}
