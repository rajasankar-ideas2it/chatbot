/*
 *
 */
package com.ideas2it.chatbot.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.model.Employee;
import com.ideas2it.chatbot.service.EmployeeService;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController extends BaseController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> get(final HttpServletResponse response) throws Exception {
		final List<Employee> responseBody = employeeService.getAll();
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@RequestMapping(value = "/getIncompleteList", method = RequestMethod.POST)
	public ResponseEntity<List<Employee>> getIncompleteEmployeeList(@RequestBody Map<String, Object> response) throws Exception {
		final List<Employee> responseBody = employeeService.getIncompleteEmployeeList(response);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}
}
