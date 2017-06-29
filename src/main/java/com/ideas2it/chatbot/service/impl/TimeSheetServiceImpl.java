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
			String leaveDateString = String.valueOf(requestParameters.get("date"));
			LocalDate leaveDate = LocalDate.parse(leaveDateString, dtf);
			updateTimeSheetForLeave(employee, leaveDate);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateForDayList(Map<String, Object> requestParameters) throws Exception {
		String projectName = String.valueOf(requestParameters.get("project"));
		Employee employee = employeeService.getEmployeeByRequest(requestParameters);
		if (!Constants.LEAVE.equalsIgnoreCase(projectName)) {
			Project project = projectService.getProjectByName(projectName);
			List<String> projectList = (List<String>) (requestParameters.get("dateList"));
			for (String projectDateString : projectList) {
				LocalDate leaveDate = LocalDate.parse(projectDateString, dtf);
				updateTimeSheetForGivenDate(employee, project, leaveDate);
			}
			employeeService.updateCurrentProjectInEmployee(employee, project);
		} else {
			updateTimeSheetForLeaveList(employee, requestParameters);
		}

	}

	private String getRangeForGivenDate(Employee employee, LocalDate leaveDate) throws Exception {
		List<Object> timeSheet = sheetsService.readHeader(connection, dailySheetName);
		String column = "";
		if (null != timeSheet && timeSheet.size() > 0) {
			String dateString = String.valueOf(timeSheet.get(2));
			LocalDate firstDate = LocalDate.parse(dateString, dtf);
			long daysBetween = ChronoUnit.DAYS.between(firstDate, leaveDate);
			if (daysBetween > 23) {
				column = getSpreadSheetRowIndex(daysBetween);
			} else {
				column = Character.toString((char) (daysBetween + Constants.ASCII_C));
			}
		}
		return column + employee.getIndex();
	}

	private void updateTimeSheet(Employee employee, Project project) throws Exception {
		String range = getRange(employee);
		List<List<Object>> values = getValueListForUpdate(project);
		sheetsService.updateData(connection, dailySheetName, range, Constants.RAW_VALUE_INPUT_OPTION, values);
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

	@Override
	public void bulkUpdateSpreadSheet(Map<String, Object> requestParam) throws Exception {
		String projectName = String.valueOf(requestParam.get("project"));
		Employee employee = employeeService.getEmployeeByRequest(requestParam);
		Project project = projectService.getProjectByName(projectName);
		String projectDate = String.valueOf(requestParam.get("projectDate"));
		if (!"null".equals(projectDate)) {
			bulkUpdateSpreadSheetForGivenDate(employee, project, LocalDate.parse(projectDate, dtf));
		} else {
			bulkUpdateSpreadSheet(employee, project);
		}
		employeeService.updateCurrentProjectInEmployee(employee, project);
	}

	private void bulkUpdateSpreadSheetForGivenDate(Employee employee, Project project, LocalDate projectDate)
			throws Exception {
		updateProjectGivenRange(employee, employee.getCurrentProject(),
				LocalDate.parse(employee.getLastUpdated(), dtf).plusDays(1), projectDate.minusDays(1));
		updateProjectGivenRange(employee, project.getName(), projectDate, LocalDate.now());
	}

	private void updateProjectGivenRange(Employee employee, String projectName, LocalDate startDate, LocalDate endDate)
			throws Exception {
		long dateDiff = ChronoUnit.DAYS.between(startDate.minusDays(1), endDate);
		List<List<Object>> values = getValueListForUpdateInRange(projectName, dateDiff);
		String firstColumn = getRangeForGivenDate(employee, startDate);
		String lastColumn = getRangeForGivenDate(employee, endDate);
		String range = firstColumn + ":" + lastColumn;
		sheetsService.updateData(connection, dailySheetName, range, Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	private void bulkUpdateSpreadSheet(Employee employee, Project project) throws Exception {
		LocalDate now = LocalDate.now();
		LocalDate lastUpdated = LocalDate.parse(employee.getLastUpdated(), dtf);
		long dateDiff = ChronoUnit.DAYS.between(lastUpdated, now);
		String range = getProjectRange(employee);
		List<List<Object>> values = getValueListForUpdateInRange(project.getName(), dateDiff);
		sheetsService.updateData(connection, dailySheetName, range, Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	private String getProjectRange(Employee employee) throws Exception {
		String firstColumn = getRangeForGivenDate(employee,
				LocalDate.parse(employee.getLastUpdated(), dtf).plusDays(1));
		String lastColumn = getRangeForGivenDate(employee, LocalDate.now());
		return firstColumn + ":" + lastColumn;
	}

	private List<List<Object>> getValueListForUpdateInRange(String name, long limit) {
		List<List<Object>> valueLists = new ArrayList<List<Object>>();
		List<Object> values = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			values.add(name);
		}
		valueLists.add(values);
		return valueLists;
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
		for (LocalDate date = lastUpdated.plusDays(1); date
				.isBefore(currentDate.plusDays(1)); date = date.plusDays(1)) {
			updateTimeSheetForDay(employee, project.getName(), date);
		}
	}

	private void updateTimeSheetForDay(Employee employee, String name, LocalDate date) throws Exception {
		String range = getRangeForGivenDate(employee, date);
		List<List<Object>> values = getValueListForUpdate(name);
		sheetsService.updateData(connection, dailySheetName, range, Constants.RAW_VALUE_INPUT_OPTION, values);
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
			String dateString = String.valueOf(timeSheet.get(2));
			LocalDate firstDate = LocalDate.parse(dateString, dtf);
			long daysBetween = ChronoUnit.DAYS.between(firstDate, date);
			if (daysBetween > 23) {
				column = getSpreadSheetRowIndex(daysBetween);
			} else {
				column = Character.toString((char) (daysBetween + Constants.ASCII_C));
			}
		}
		return column + employee.getIndex();
	}

	@SuppressWarnings("unchecked")
	private void updateTimeSheetForLeaveList(Employee employee, Map<String, Object> requestParameters)
			throws Exception {
		List<String> leaveDateList = (List<String>) (requestParameters.get("dateList"));
		for (String leaveDateString : leaveDateList) {
			LocalDate leaveDate = LocalDate.parse(leaveDateString, dtf);
			updateTimeSheetForLeave(employee, leaveDate);
		}
	}

	private void updateTimeSheetForLeave(Employee employee, LocalDate leaveDate) throws Exception {
		String range = getRangeForGivenDate(employee, leaveDate);
		List<List<Object>> values = getValueListForUpdate(Constants.LEAVE);
		sheetsService.updateData(connection, dailySheetName, range, Constants.RAW_VALUE_INPUT_OPTION, values);
	}

	private List<List<Object>> getValueListForUpdate(String name) {
		List<List<Object>> valueLists = new ArrayList<List<Object>>();
		List<Object> values = new ArrayList<>();
		values.add(name);
		valueLists.add(values);
		return valueLists;
	}

	private String getSpreadSheetRowIndex(long daysBetween) {
		String column = "";
		if (daysBetween == 24) {
			column = "AA";
		} else if (daysBetween == 25) {
			column = "AB";
		} else if (daysBetween == 26) {
			column = "AC";
		} else if (daysBetween == 27) {
			column = "AD";
		} else if (daysBetween == 28) {
			column = "AE";
		} else if (daysBetween == 29) {
			column = "AF";
		} else if (daysBetween == 30) {
			column = "AG";
		}
		return column;
	}

}
