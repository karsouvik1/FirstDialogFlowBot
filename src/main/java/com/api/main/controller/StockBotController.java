package com.api.main.controller;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.main.util.MANHJsonUtil;
import com.api.main.util.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class StockBotController {
	
	@Autowired
	TestUtil testUtil;
	
	@Autowired
	MANHJsonUtil manhJsonUtil;
	
	@PostMapping(path = "/api/webhook")
    public String processWebhook(@RequestBody String request) throws JsonMappingException, JsonProcessingException
    {
		String retString = "";
		System.out.println("Request from dialogflow:"+request);
		JSONObject dialoflowJsonObject = new JSONObject(request);
		System.out.println("dialoflowJsonObject:"+dialoflowJsonObject);
		
		String action = (String) ((JSONObject)dialoflowJsonObject.get("queryResult")).get("action");
		System.out.println("Action from dialog flow:"+action);
		String json = (String) ((JSONObject)((JSONObject)dialoflowJsonObject.get("queryResult")).get("parameters")).get("any");
		
		if("stcok-quote".equals(action)) {
			System.out.println("ticker send :"+json);
			System.out.println("calling stock information");
			retString = testUtil.stockPrice(json);
			retString = formatResponse(retString);
		}
		if("json-to-vm".equals(action)) {
			System.out.println("Calling json to vm");
			System.out.println("json string is >>>>>>"+json);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> jsonElements = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
		        });
			StringBuffer vmTeamplate = new StringBuffer();
	        String keyVMTeamplate = "";
			StringBuffer retStringBuffer = manhJsonUtil.convertToVelocity(jsonElements, vmTeamplate, keyVMTeamplate, null);
			retString = retStringBuffer.toString();
			retString = formatResponse(retString);
		}
		
		return retString;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/")
	public String getMessage() {
		System.err.println("Welcome Message for stock app");
		System.out.println("Welcome to app");
		return "Welcome to app";
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/stock/test")
    public String getTestStockPrice(@RequestBody String name)
    {
		System.err.println("Test method"+name);
		System.out.println("Printing test"+name);
		return "test return";
    }
	
	private String formatResponse(String response) {
		JSONObject retJsonObject = new JSONObject();
		JSONArray newJsonArray = new JSONArray();
		retJsonObject.put("fulfillmentMessages", newJsonArray);
		JSONObject textJsonObjectRoot = new JSONObject();
		JSONObject textJsonObject = new JSONObject();
		JSONArray textJsonArray = new JSONArray();
		textJsonArray.put(response);
		textJsonObject.put("text", textJsonArray);
		textJsonObjectRoot.put("text", textJsonObject);
		newJsonArray.put(0, textJsonObjectRoot);
		return retJsonObject.toString();
	}
}

