package com.ideas2it.chatbot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ideas2it.chatbot.service.GoogleConnection;
import com.ideas2it.chatbot.service.GoogleSheets;
import com.ideas2it.chatbot.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Value("${google.spreadsheet.role.sheet.name}")
	private String sheetName;
	
	@Autowired
	private GoogleSheets sheetsService;
	
	@Autowired
	private GoogleConnection connection;

	@Override
	public List<List<Object>> getAll() throws Exception {
		return sheetsService.readTable(connection, sheetName);
	}
	
}
