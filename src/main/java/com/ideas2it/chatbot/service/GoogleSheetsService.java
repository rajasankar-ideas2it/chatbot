/*
 *
 */
package com.ideas2it.chatbot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ideas2it.chatbot.Global;
import com.ideas2it.chatbot.utils.Constants;

@Service
public class GoogleSheetsService implements GoogleSheets {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${google.app.name}")
	String appName;

	@Value("${google.spreadsheet.id}")
	String spreadsheetId;

	@Value("${google.spreadsheet.sheet.name}")
	String sheetName;

	private final Sheets sheetsService = null;

	@Override
	public List<List<Object>> readTable(final GoogleConnection connection) throws Exception {
		final Sheets service = getSheetsService(connection);
		return readTable(service, spreadsheetId, sheetName);
	}

	private Sheets getSheetsService(final GoogleConnection gc) throws Exception {
		if (this.sheetsService == null)
			try {
				return new Sheets.Builder(Global.HTTP_TRANSPORT, Global.JSON_FACTORY, gc.getCredentials())
						.setApplicationName(appName).build();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		else
			return this.sheetsService;
		return sheetsService;
	}

	private List<List<Object>> readTable(final Sheets service, final String spreadsheetId, final String sheetName)
			throws IOException {
		final ValueRange table = service.spreadsheets().values().get(spreadsheetId, sheetName).execute();

		final List<List<Object>> values = table.getValues();
		// printTable(values);

		return values;
	}

	private void printTable(final List<List<Object>> values) {
		if (values == null || values.size() == 0)
			logger.info("No data found.");
		else {
			logger.info("read data");
			for (final List<Object> row : values) {
				for (int c = 0; c < row.size(); c++)
					logger.info(row.get(c).toString() + " ");
				logger.info("\n");
			}
		}
	}

	@Override
	public List<List<Object>> readTable(GoogleConnection connection, String sheetName) throws Exception {
		final Sheets service = getSheetsService(connection);
		List<List<Object>> values = readTable(service, spreadsheetId, sheetName);
		removeHeaderRow(values);
		return values;
	}

	@Override
	public List<Object> readHeader(GoogleConnection connection, String sheetName) throws Exception {
		final Sheets service = getSheetsService(connection);
		List<List<Object>> values = readTable(service, spreadsheetId, sheetName);
		if (null != values && values.size() > 0) {
			return values.get(0);
		}
		return null;
	}

	private void removeHeaderRow(List<List<Object>> values) {
		if (null != values && values.size() > 0) {
			values.remove(0);
		}
	}

	@Override
	public UpdateValuesResponse updateData(GoogleConnection connection, String sheetName, String range,
			String valueInputOption, List<List<Object>> value) throws Exception {
		ValueRange requestBody = new ValueRange();
		Sheets sheetsService = getSheetsService(connection);
		requestBody.setValues(value);
		requestBody.setMajorDimension(Constants.ROWS_MAJOR_DIMENSION);
		Sheets.Spreadsheets.Values.Update request = sheetsService.spreadsheets().values().update(spreadsheetId,
				sheetName + "!" + range, requestBody);
		request.setValueInputOption(valueInputOption);
		UpdateValuesResponse response = request.execute();
		return response;
	}

	public Spreadsheet createSheetsService(final GoogleConnection gc, String documentName, String sheetName)
			throws Exception {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		Spreadsheet requestBody = new Spreadsheet();
		Sheets sheetsService = new Sheets.Builder(httpTransport, jsonFactory, gc.getCredentials())
				.setApplicationName(documentName + "/" + sheetName).build();
		SpreadsheetProperties properties = new SpreadsheetProperties();
		properties.setTitle(documentName);
		requestBody.setProperties(properties);
		List<Sheet> sheetList = setSheetName(requestBody, sheetName);
		requestBody.setSheets(sheetList);
		Sheets.Spreadsheets.Create request = sheetsService.spreadsheets().create(requestBody);
		Spreadsheet response = request.execute();
		return response;
	}

	private List<Sheet> setSheetName(Spreadsheet requestBody, String sheetName) {
		List<Sheet> sheetList = new ArrayList<>();
		Sheet sheet = new Sheet();
		SheetProperties sheetProperties = new SheetProperties();
		sheetProperties.setTitle(sheetName);
		sheet.setProperties(sheetProperties);
		sheetList.add(sheet);
		return sheetList;
	}
}
