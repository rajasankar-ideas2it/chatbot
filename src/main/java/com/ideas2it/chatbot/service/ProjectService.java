package com.ideas2it.chatbot.service;

import java.util.List;

import com.ideas2it.chatbot.model.Project;

public interface ProjectService {

	List<Project> getAll() throws Exception;

	Project getProjectByName(String valueOf) throws Exception;

}
