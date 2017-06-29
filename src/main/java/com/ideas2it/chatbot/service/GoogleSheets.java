/*
 * 
 */
package com.ideas2it.chatbot.service;

import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;

public interface GoogleSheets {

	List<List<Object>> readTable(GoogleConnection gc) throws Exception;

	List<List<Object>> readTable(GoogleConnection connection, String sheetName) throws Exception;

	UpdateValuesResponse updateData(GoogleConnection connection, String sheetName, String range,
			String valueInputOption, List<List<Object>> value) throws Exception;

	List<Object> readHeader(GoogleConnection connection, String sheetName) throws Exception;

	public Spreadsheet createSheetsService(final GoogleConnection gc, String documentName, String sheetName)
			throws Exception;
}
