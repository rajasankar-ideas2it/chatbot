package com.ideas2it.chatbot.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.service.RoleService;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public ResponseEntity<String> save(final HttpServletResponse response) throws Exception {
		return new ResponseEntity<>("Role Created Successfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<List<Object>>> get(final HttpServletResponse response) throws Exception {
		final List<List<Object>> responseBody = roleService.getAll();
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

}
