package com.ideas2it.chatbot.service;

import java.util.List;
import java.util.Map;

import com.ideas2it.chatbot.model.Employee;
import com.ideas2it.chatbot.model.Project;

public interface EmployeeService {

	List<Employee> getAll() throws Exception;

	Employee getEmployeeByConversationId(String conversationId) throws Exception;

	Employee getEmployeeByEmailId(String emailId) throws Exception;

	Employee getEmployeeByChatId(String chatId) throws Exception;

	Employee getEmployeeByEmployeeId(String employeeId) throws Exception;

	void updateCurrentProjectInEmployee(Employee employee, Project project) throws Exception;

	void updateEmployee(Map<String, Object> requestParameters) throws Exception;

	Employee getEmployeeByRequest(Map<String, Object> requestParameters) throws Exception;

	List<Employee> getIncompleteEmployeeList(Map<String, Object> response) throws Exception;

}
