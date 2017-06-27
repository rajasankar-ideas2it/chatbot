package com.ideas2it.chatbot.convertor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ideas2it.chatbot.model.Employee;

@Service
public class EmployeeConvertor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Employee> convertObjectToEmployeeList(List<List<Object>> employeeObjectList) throws Exception {
		List<Employee> employeeList = new ArrayList<>();
		if (employeeObjectList == null || employeeObjectList.size() == 0)
			logger.info("No data found.");
		else {
			for (final List<Object> employeeObject : employeeObjectList) {
				employeeList.add(convertObjectToEmployee(employeeObject));
			}
		}
		return employeeList;
	}

	public Employee convertObjectToEmployee(List<Object> employeeObject) throws Exception {
		Employee employee = new Employee();
		for (int i = 0; i < employeeObject.size(); i++) {
			if (i == 0) {
				employee.setId(String.valueOf(employeeObject.get(i)));
			} else if (i == 1) {
				employee.setName(String.valueOf(employeeObject.get(i)));
			} else if (i == 2) {
				employee.setEmailId(String.valueOf(employeeObject.get(i)));
			} else if (i == 3) {
				employee.setStatus(String.valueOf(employeeObject.get(i)));
			} else if (i == 4) {
				employee.setRole(String.valueOf(employeeObject.get(i)));
			} else if (i == 5) {
				employee.setSkillSet(String.valueOf(employeeObject.get(i)));
			} else if (i == 6) {
				// employee.setDob(String.valueOf(employeeObject.get(i)));
			} else if (i == 7) {
				// employee.setDateOfJoining(String.valueOf(employeeObject.get(i)));
			} else if (i == 8) {
				employee.setConversationId(String.valueOf(employeeObject.get(i)));
			} else if (i == 9) {
				employee.setChatId(String.valueOf(employeeObject.get(i)));
			} else if (i == 10) {
				employee.setIsAdmin(String.valueOf(employeeObject.get(i)));
			} else if (i == 11) {
				employee.setCurrentProject(String.valueOf(employeeObject.get(i)));
			} else if (i == 12) {
				employee.setIsResponded(String.valueOf(employeeObject.get(i)));
			} else if (i == 13) {
				employee.setLastUpdated(String.valueOf(employeeObject.get(i)));
			}
		}
		return employee;
	}
}
