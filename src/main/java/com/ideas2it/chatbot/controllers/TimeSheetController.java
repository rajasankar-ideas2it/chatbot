package com.ideas2it.chatbot.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.service.TimeSheetService;

@RestController
@RequestMapping(value = "/timesheet")
public class TimeSheetController {

	@Autowired
	private TimeSheetService timeSheetService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateForDay", method = RequestMethod.POST)
	public ResponseEntity<String> get(final @RequestBody Map<String, Object> requestBody) throws Exception {
		Map<String, Object> requestParam = (Map<String, Object>) requestBody.get("parameters");
		timeSheetService.updateTimeSheetForDay(requestParam);
		return new ResponseEntity<>("Project Updated successfully", HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateForPeriod", method = RequestMethod.POST)
	public ResponseEntity<String> updateTimeSheetForPeriod(final @RequestBody Map<String, Object> requestBody)
			throws Exception {
		Map<String, Object> requestParam = (Map<String, Object>) requestBody.get("parameters");
		timeSheetService.updateTimeSheetForPeriod(requestParam);
		return new ResponseEntity<>("Project Updated successfully", HttpStatus.OK);
	}
}
