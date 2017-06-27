package com.ideas2it.chatbot.convertor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ideas2it.chatbot.model.Project;

@Service
public class ProjectConvertor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Project> convertObjectToProjectList(List<List<Object>> projectObjectList) throws Exception {
		List<Project> projectList = new ArrayList<>();
		if (projectObjectList == null || projectObjectList.size() == 0)
			logger.info("No data found.");
		else {
			for (final List<Object> projectObject : projectObjectList) {
				projectList.add(convertObjectToProject(projectObject));
			}
		}
		return projectList;
	}

	private Project convertObjectToProject(List<Object> projectObjectList) {
		Project project = new Project();
		for (int i = 0; i < projectObjectList.size(); i++) {
			if (i == 0) {
				project.setId(Integer.valueOf(String.valueOf(projectObjectList.get(i))));
			} else if (i == 1) {
				project.setName(String.valueOf(projectObjectList.get(i)));
			} else if (i == 2) {
				project.setProjectDescription(String.valueOf(projectObjectList.get(i)));
			} else if (i == 3) {
				project.setClientDetail(String.valueOf(projectObjectList.get(i)));
			} else if (i == 4) {
				project.setStatus(String.valueOf(projectObjectList.get(i)));
			} else if (i == 5) {
				project.setTags(Arrays.asList(String.valueOf(projectObjectList.get(i)).split(",")));
			}
		}
		return project;
	}

}
