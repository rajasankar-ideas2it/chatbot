package com.ideas2it.chatbot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ideas2it.chatbot.convertor.ProjectConvertor;
import com.ideas2it.chatbot.model.Project;
import com.ideas2it.chatbot.service.GoogleConnection;
import com.ideas2it.chatbot.service.GoogleSheets;
import com.ideas2it.chatbot.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Value("${google.spreadsheet.project.sheet.name}")
	private String sheetName;

	@Autowired
	private GoogleSheets sheetsService;

	@Autowired
	private GoogleConnection connection;

	@Autowired
	private ProjectConvertor projectConvertor;

	@Override
	public List<Project> getAll() throws Exception {
		List<List<Object>> projectObjectList = sheetsService.readTable(connection, sheetName);
		return projectConvertor.convertObjectToProjectList(projectObjectList);
	}

	@Override
	public Project getProjectByName(String projectName) throws Exception {
		List<List<Object>> projectObjectList = sheetsService.readTable(connection, sheetName);
		List<Project> projectList = projectConvertor.convertObjectToProjectList(projectObjectList);
		for (Project project : projectList) {
			if (project.getName().equalsIgnoreCase(projectName)) {
				return project;
			}
		}
		return null;
	}

}
