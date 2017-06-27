package com.ideas2it.chatbot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.ideas2it.chatbot.convertor.EmployeeConvertor;
import com.ideas2it.chatbot.model.Employee;
import com.ideas2it.chatbot.model.Project;
import com.ideas2it.chatbot.service.EmployeeService;
import com.ideas2it.chatbot.service.GoogleConnection;
import com.ideas2it.chatbot.service.GoogleSheets;
import com.ideas2it.chatbot.utils.Constants;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Value("${google.spreadsheet.employee.sheet.name}")
	private String sheetName;

	@Autowired
	private GoogleSheets sheetsService;

	@Autowired
	private GoogleConnection connection;

	@Autowired
	private EmployeeConvertor employeeConvertor;

	@Override
	public List<Employee> getAll() throws Exception {
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		return employeeList;
	}

	@Override
	public Employee getEmployeeByConversationId(String conversationId) throws Exception {
		int index = 1;
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		for (Employee employee : employeeList) {
			index++;
			if (employee.getConversationId().equalsIgnoreCase(conversationId)) {
				employee.setIndex(index);
				return employee;
			}
		}
		return null;
	}

	@Override
	public Employee getEmployeeByEmailId(String emailId) throws Exception {
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		for (Employee employee : employeeList) {
			if (employee.getEmailId().equalsIgnoreCase(emailId)) {
				return employee;
			}
		}
		return null;
	}

	@Override
	public Employee getEmployeeByChatId(String chatId) throws Exception {
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		for (Employee employee : employeeList) {
			if (employee.getChatId().equalsIgnoreCase(chatId)) {
				return employee;
			}
		}
		return null;
	}

	@Override
	public Employee getEmployeeByEmployeeId(String employeeId) throws Exception {
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		for (Employee employee : employeeList) {
			if (employee.getId().equalsIgnoreCase(employeeId)) {
				return employee;
			}
		}
		return null;
	}

	@Override
	public void updateCurrentProjectInEmployee(Employee employee, Project project) throws Exception {
		String currentProjectRange = getCurrentProjectRange(employee);
		List<List<Object>> currentProjectValue = getValueListForUpdate(project.getName());
		UpdateValuesResponse updateResponse = sheetsService.updateData(connection, sheetName, currentProjectRange,
				Constants.RAW_VALUE_INPUT_OPTION, currentProjectValue);
		String isRespondedRange = getIsRespondedRange(employee);
		List<List<Object>> isRespondedValue = getValueListForUpdate(Constants.YES);
		sheetsService.updateData(connection, sheetName, isRespondedRange, Constants.RAW_VALUE_INPUT_OPTION,
				isRespondedValue);
		System.out.println(updateResponse.toPrettyString());
	}

	private String getIsRespondedRange(Employee employee) {
		return "M" + employee.getIndex();
	}

	private List<List<Object>> getValueListForUpdate(String value) {
		List<List<Object>> valueLists = new ArrayList<List<Object>>();
		List<Object> values = new ArrayList<>();
		values.add(value);
		valueLists.add(values);
		return valueLists;
	}

	private String getCurrentProjectRange(Employee employee) {
		return "L" + employee.getIndex();
	}

	@Override
	public void updateEmployee(Map<String, Object> requestParameters) throws Exception {
		Employee employee = getEmployeeByRequest(requestParameters);
		updateEmployeeConversationId(employee);
		updateEmployeeChatId(employee);
	}

	private void updateEmployeeChatId(Employee employee) {

	}

	private void updateEmployeeConversationId(Employee employee) {

	}

	@Override
	public Employee getEmployeeByRequest(Map<String, Object> requestParameters) throws Exception{
		String conversationId = String.valueOf(requestParameters.get("conversationId"));
		String emailId = String.valueOf(requestParameters.get("emailId"));
		String chatId = String.valueOf(requestParameters.get("chatId"));
		String employeeId = String.valueOf(requestParameters.get("employeeId"));
		Employee employee = new Employee();
		if (null != conversationId) {
			employee = getEmployeeByConversationId(conversationId);
		}
		if (null != employee && !"null".equals(chatId)) {
			employee = getEmployeeByChatId(chatId);
		}
		if (null != employee && !"null".equals(emailId)) {
			employee = getEmployeeByEmailId(emailId);
		}
		if (null != employee && !"null".equals(employeeId)) {
			employee = getEmployeeByEmployeeId(employeeId);
		}
		return employee;
	}

	@Override
	public List<Employee> getIncompleteEmployeeList(Map<String, Object> response) throws Exception {
		List<Employee> inCompleteEmployeeList = new ArrayList<>();
		List<List<Object>> employeeObjectList = sheetsService.readTable(connection, sheetName);
		List<Employee> employeeList = employeeConvertor.convertObjectToEmployeeList(employeeObjectList);
		for (Employee employee : employeeList) {
			if (true) {
				inCompleteEmployeeList.add(employee);
			}
		}
		return inCompleteEmployeeList;
	}

}
