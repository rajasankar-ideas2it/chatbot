/*
 *
 */
package com.ideas2it.chatbot.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideas2it.chatbot.service.GoogleConnection;
import com.ideas2it.chatbot.service.GoogleSheets;

@RestController
public class EmployeeController extends BaseController {

    @Autowired
    private GoogleConnection connection;

    @Autowired
    private GoogleSheets sheetsService;

    @RequestMapping(value = "/api/sheet", method = RequestMethod.GET)
    public ResponseEntity<List<List<Object>>> read(final HttpServletResponse response) throws Exception {
        System.out.println("/api/sheet");
        // read spreadsheet
        final List<List<Object>> responseBody = sheetsService.readTable(connection);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
