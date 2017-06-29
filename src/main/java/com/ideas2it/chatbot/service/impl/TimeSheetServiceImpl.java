package com.ideas2it.chatbot.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.ideas2it.chatbot.model.Employee;
import com.ideas2it.chatbot.model.Project;
import com.ideas2it.chatbot.service.EmployeeService;
import com.ideas2it.chatbot.service.GoogleConnection;
import com.ideas2it.chatbot.service.GoogleSheets;
import com.ideas2it.chatbot.service.ProjectService;
import com.ideas2it.chatbot.service.TimeSheetService;
import com.ideas2it.chatbot.utils.Constants;

@Service
public class TimeSheetServiceImpl implements TimeSheetService {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy"); // 06-28-2017

	@Value("${google.spreadsheet.timesheet.sheet.name}")
	private String timeSheetName;

	@Value("${google.spreadsheet.daily.sheet.name}")
	private String dailySheetName;

	@Autowired
	private GoogleSheets sheetsService;

	@Autowired
	private GoogleConnection connection;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ProjectService projectService;

	@Override
	public List<List<Object>> getAllDaily() throws Exception {
		return sheetsService.readTable(connection, dailySheetName);
	}

	@Override
	public void updateTimeSheetForDay(Map<String, Object> requestParameters) throws Exception {
		String projectName = String.valueOf(requestParameters.get("project"));
		Employee employee = employeeService.getEmployeeByRequest(requestParameters);
		if (!Constants.LEAVE.equals(projectName)) {
			Project project = projectService.getProjectByName(projectName);
			updateTimeSheet(employee, project);
			employeeService.updateCurrentProjectInEmployee(employee, project);
		} else {
			updateTimeSheetForLeave(employee, requestParameters);
		}
	}

	private void updateTimeSheetForLeave(Employee employee, Map<String, Object> requestParameters) throws Exception {
		String leaveDateString = String.valueOf(requestParameters.get("date"));
		LocalDate leaveDate = LocalDate.parse(leaveDateString, dtf);
		String range = getRangeForGivenDate(employee, leaveDate);
		List<List<Object>> values = getValueListForUpdate(Constants.LEAVE);
		UpdateValuesResponse updateResponse = sheetsService.updateData(connection, dailySheetName, range,
				Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	private List<List<Object>> getValueListForUpdate(String leave) {
		List<List<Object>> valueLists = new ArrayList<List<Object>>();
		List<Object> values = new ArrayList<>();
		values.add(leave);
		valueLists.add(values);
		return valueLists;
	}

	private String getRangeForGivenDate(Employee employee, LocalDate leaveDate) throws Exception {
		List<Object> timeSheet = sheetsService.readHeader(connection, dailySheetName);
		String column = "";
		if (null != timeSheet && timeSheet.size() > 0) {
			String dateString = String.valueOf(timeSheet.get(1));
			LocalDate firstDate = LocalDate.parse(dateString, dtf);
			long daysBetween = ChronoUnit.DAYS.between(firstDate, leaveDate);
			column = Character.toString((char) (daysBetween + Constants.ASCII_B));
		}
		return column + employee.getIndex();
	}

	private void updateTimeSheet(Employee employee, Project project) throws Exception {
		String range = getRange(employee);
		List<List<Object>> values = getValueListForUpdate(project);
		UpdateValuesResponse updateResponse = sheetsService.updateData(connection, dailySheetName, range,
				Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	@Override
	public void updateTimeSheetForPeriod(Map<String, Object> requestParam) throws Exception {
		String projectName = String.valueOf(requestParam.get("project"));
		Employee employee = employeeService.getEmployeeByRequest(requestParam);
		Project project = projectService.getProjectByName(projectName);
		String projectDate = String.valueOf(requestParam.get("projectDate"));
		if (!"null".equals(projectDate)) {
			updateTimeSheetForGivenDate(employee, project, LocalDate.parse(projectDate, dtf));
		} else {
			updateTimeSheetTillCurrentDate(employee, project);
		}
		employeeService.updateCurrentProjectInEmployee(employee, project);
	}

	private void updateTimeSheetForGivenDate(Employee employee, Project project, LocalDate projectDate)
			throws Exception {
		LocalDate currentDate = LocalDate.now();
		LocalDate lastUpdated = LocalDate.parse(employee.getLastUpdated(), dtf);
		for (LocalDate date = lastUpdated.plusDays(1); date.isBefore(projectDate); date = date.plusDays(1)) {
			updateTimeSheetForDay(employee, employee.getCurrentProject(), date);
		}
		for (LocalDate date = projectDate; date.isBefore(currentDate.plusDays(1)); date = date.plusDays(1)) {
			updateTimeSheetForDay(employee, project.getName(), date);
		}
	}

	private void updateTimeSheetTillCurrentDate(Employee employee, Project project) throws Exception {
		LocalDate currentDate = LocalDate.now();
		LocalDate lastUpdated = LocalDate.parse(employee.getLastUpdated(), dtf);
		for (LocalDate date = lastUpdated; date.isBefore(currentDate.plusDays(1)); date = date.plusDays(1)) {
			updateTimeSheetForDay(employee, project.getName(), date);
		}
	}

	private void updateTimeSheetForDay(Employee employee, String name, LocalDate date) throws Exception {
		String range = getRangeForGivenDate(employee, date);
		List<List<Object>> values = getValueListForUpdate(name);
		UpdateValuesResponse updateResponse = sheetsService.updateData(connection, dailySheetName, range,
				Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	private List<List<Object>> getValueListForUpdate(Project project) {
		List<List<Object>> valueLists = new ArrayList<List<Object>>();
		List<Object> values = new ArrayList<>();
		values.add(project.getName());
		valueLists.add(values);
		return valueLists;
	}

	private String getRange(Employee employee) throws Exception {
		LocalDate date = LocalDate.now();
		List<Object> timeSheet = sheetsService.readHeader(connection, dailySheetName);
		String column = "";
		if (null != timeSheet && timeSheet.size() > 0) {
			String dateString = String.valueOf(timeSheet.get(1));
			LocalDate firstDate = LocalDate.parse(dateString, dtf);
			long daysBetween = ChronoUnit.DAYS.between(firstDate, date);
			column = Character.toString((char) (daysBetween + Constants.ASCII_B));
		}
		return column + employee.getIndex();
	}

}
