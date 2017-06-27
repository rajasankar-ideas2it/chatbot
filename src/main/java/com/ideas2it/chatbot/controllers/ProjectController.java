package com.ideas2it.chatbot.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.model.Project;
import com.ideas2it.chatbot.service.ProjectService;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public ResponseEntity<String> save(final HttpServletResponse response) throws Exception {
		return new ResponseEntity<>("Project Added successfully", HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Project>> get(final HttpServletResponse response) throws Exception {
		final List<Project> responseBody = projectService.getAll();
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}
}
