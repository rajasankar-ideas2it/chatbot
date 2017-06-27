package com.ideas2it.chatbot.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideas2it.chatbot.service.EmployeeService;
import com.ideas2it.chatbot.service.IdeasWebHookService;
import com.ideas2it.chatbot.service.TimeSheetService;

@Service
public class IdeasWebHookServiceImpl implements IdeasWebHookService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TimeSheetService timeSheetService;
	
	@Autowired
	private EmployeeService employeeService;

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> addEmployee(Map<String, Object> requestParameters) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> requestAge = (Map<String, Object>) requestParameters.get("age");
		StringBuilder speech = new StringBuilder();
		if (null != requestParameters.get("gender") && !"".equals(requestParameters.get("gender"))) {
			speech.append("Employee Added Successfully..!\n");
			speech.append(" Employee Name : ");
			speech.append(requestParameters.get("name"));
			speech.append("\n Age : ");
			speech.append(requestAge.get("amount"));
			response.put("displayText", speech);
		} else {
			speech.append("Tell me the gender of the employee?");
			List<Map<String, Object>> contextOutList = new ArrayList<>();
			Map<String, Object> contextOutMap = new HashMap<>();
			contextOutMap.put("name", "addGender");
			contextOutMap.put("lifespan", 10);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("name", requestParameters.get("name"));
			parameterMap.put("age", requestAge);
			contextOutMap.put("parameters", parameterMap);
			contextOutList.add(contextOutMap);
			response.put("contextOut", contextOutList);
		}
		response.put("speech", speech);
		response.put("source", "ideasbot");
		return response;
	}

	@Override
	public Map<String, Object> getDailyReport(Map<String, Object> requestParameters) throws Exception {
		Map<String, Object> response = new HashMap<>();
		List<List<Object>> res = timeSheetService.getAllDaily();
		if (res == null || res.size() == 0)
			logger.info("No data found.");
		else {
			logger.info("read data");
			for (final List<Object> row : res) {
				for (int c = 0; c < row.size(); c++)
					logger.info(row.get(c).toString() + " ");
				logger.info("\n");
			}
		}
		return response;
	}

	@Override
	public Map<String, Object> addDailyReport(Map<String, Object> requestParameters) throws Exception {
		Map<String, Object> response = new HashMap<>();
		timeSheetService.addDailyReport(requestParameters);
		return response;
	}

	@Override
	public Map<String, Object> updateEmployee(Map<String, Object> requestParameters) throws Exception {
		Map<String, Object> response = new HashMap<>();
		employeeService.updateEmployee(requestParameters);
		return response;
	}

}
