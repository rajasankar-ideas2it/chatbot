package com.ideas2it.chatbot.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.service.TimeSheetService;

@RestController
@RequestMapping(value = "/timesheet")
public class TimeSheetController {

	@Autowired
	private TimeSheetService timeSheetService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<List<Object>>> get(final HttpServletResponse response) throws Exception {
		// final List<List<Object>> responseBody = timeSheetService.getAll();
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
	}
}
