package com.ideas2it.chatbot.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.service.IdeasWebHookService;
import com.ideas2it.chatbot.utils.Constants;

@RestController
public class IdeasWebHookController {

	@Autowired
	private IdeasWebHookService ideaWebHookService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ideasbot", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> webHook(@RequestBody Map<String, Object> requestPayLoad)
			throws Exception {
		Map<String, Object> responseBody = new HashMap<>();
		Map<String, Object> requestResult = (Map<String, Object>) requestPayLoad.get("result");
		Map<String, Object> requestParameters = (Map<String, Object>) requestResult.get("parameters");
		String actionName = String.valueOf(requestResult.get("action"));
		switch (actionName) {
			case Constants.ADDEMPLOYEE_ACTION:
				responseBody = ideaWebHookService.addEmployee(requestParameters);
				break;
			case Constants.GET_DAILY_REPORT:
				responseBody = ideaWebHookService.getDailyReport(requestParameters);
				break;
			case Constants.ADD_DAILY_REPORT:
				responseBody = ideaWebHookService.addDailyReport(requestParameters);
				break;
			case Constants.UPDATE_EMPLOYEE:
				responseBody = ideaWebHookService.updateEmployee(requestParameters);
				break;
		}
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}
}