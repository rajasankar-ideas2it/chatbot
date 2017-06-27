package com.ideas2it.chatbot.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.common.base.Ascii;
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
	public void addDailyReport(Map<String, Object> requestParameters) throws Exception {
		String projectName = String.valueOf(requestParameters.get("project"));
		Employee employee = employeeService.getEmployeeByRequest(requestParameters);
		Project project = projectService.getProjectByName(projectName);
		updateTimeSheet(employee, project);
		employeeService.updateCurrentProjectInEmployee(employee, project);
	}

	private void updateTimeSheet(Employee employee, Project project) throws Exception {
		String range = getRange(employee);
		List<List<Object>> values = getValueListForUpdate(project);
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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy"); // 06-28-2017
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
