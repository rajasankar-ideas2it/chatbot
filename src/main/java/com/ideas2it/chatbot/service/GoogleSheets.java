/*
 * 
 */
package com.ideas2it.chatbot.service;

import java.util.List;

public interface GoogleSheets {
    
    List<List<Object>> readTable(GoogleConnection gc) throws Exception;
}
