package com.custom.dialog.lab.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
// import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;
import org.json.XML;

/**
 * Utils
 */
public class Utils {

    public static String prelogString(String dataStruct, int lineNumber, long tat, String status, String logMessage) {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String file = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String method = Thread.currentThread().getStackTrace()[2].getMethodName();
		return null;
	}

	/**
	 * getCodelineNumber
	 *
	 * @return lineNumber
	 */
	public static int getCodelineNumber() {
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
		return lineNumber;
	}

	/**
	 * Convert json string to json Object
	 * 
	 * @param jsonstring
	 * @return
	 */
	public static JSONObject stringToJson(String jsonstring) {
		return new JSONObject(jsonstring);
	}

	/**
	 * Replace all newline and tab spaces in a string
	 * 
	 * @param input
	 * @return
	 */
	public static String rmNewlineTab(String input) {
		return input.replace("\n", "").replace("\r", "").replace("\t", "");
	}

	/**
	 * Convert xml string to JsonObject
	 * 
	 * @param xmlString
	 * @return
	 */
	public static JSONObject xmlToJson(String xmlString) {
		JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
		return xmlJSONObj;
	}

	/**
	 * Read file
	 * 
	 * @param path
	 * @param encoding Default encoding is UTF-8
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			throw new IllegalArgumentException("Path not found : " + e.getMessage());
		}
		String tmplt = new String(encoded, StandardCharsets.UTF_8);
		return tmplt;
	}
	// StandardCharsets.UTF_8

	/**
	 * Convert current datetime to date and time String array
	 * 
	 * @return
	 */
	public static String[] dateToString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		return reportDate.split("\\s+");
	}

		/**
	 * Convert current datetime to date and time String array
	 * 
	 * @return
	 */
	public static String curdateToString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		return reportDate;
	}
        
        public static JSONObject responseDisplay(String code,String Message){
            JSONObject error = new JSONObject();
            error.put("statusCode", code);
            error.put("message", Message);
            return error;
        }
}