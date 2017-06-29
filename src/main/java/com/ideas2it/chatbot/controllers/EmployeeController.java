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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getIncompleteList", method = RequestMethod.POST)
	public ResponseEntity<List<Employee>> getIncompleteEmployeeList(@RequestBody Map<String, Object> request)
			throws Exception {
		Map<String, Object> requestParam = (Map<String, Object>) request.get("parameters");
		final List<Employee> responseBody = employeeService.getIncompleteEmployeeList(requestParam);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	public ResponseEntity<Employee> getEmployee(@RequestBody Map<String, Object> request) throws Exception {
		Map<String, Object> requestParam = (Map<String, Object>) request.get("parameters");
		final Employee responseBody = employeeService.getEmployeeByRequest(requestParam);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateEmployeeByEmailId", method = RequestMethod.POST)
	public ResponseEntity<String> updateEmployeeByEmailId(@RequestBody Map<String, Object> request) throws Exception {
		Map<String, Object> requestParam = (Map<String, Object>) request.get("parameters");
		employeeService.updateEmployee(requestParam);
		return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
	}
}
