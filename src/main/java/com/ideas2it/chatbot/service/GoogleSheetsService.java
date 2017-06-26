/*
 *
 */
package com.ideas2it.chatbot.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ideas2it.chatbot.Global;

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
        printTable(values);
        
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
    
}
