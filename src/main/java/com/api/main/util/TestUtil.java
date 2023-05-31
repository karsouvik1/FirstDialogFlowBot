package com.api.main.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("testUtil")
public class TestUtil {

	private final RestTemplate restTemplate;

	@Autowired
	public TestUtil(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	public String stockPrice(String ticker) {
		ticker = ticker+".NS";
		String url = "https://query1.finance.yahoo.com/v11/finance/quoteSummary";
		url = url+"/"+ticker+"?modules=financialData";
		System.out.println("Printing Rest url:"+url);
		
		String currentPrice = "";
		System.out.println("Before try block......");
		try {
			System.out.println("Calling rest to get stock price");
			String jsonString = restTemplate.getForObject(url, String.class);
			System.err.println("After Calling rest to get stock price:"+jsonString);
			JSONObject jsonObj = new JSONObject(jsonString);

			currentPrice = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) jsonObj
					.get("quoteSummary")).get("result")).get(0)).get("financialData")).get("currentPrice")).get("fmt");
			System.out.println("price for stock "+ticker+" is "+currentPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error getting stock price"+e.getMessage());
			e.printStackTrace();
		}
		if(currentPrice != null && !currentPrice.equals("")) {
			return "Price for Stock "+ ticker + " is "+ currentPrice+ " \n Do you want to know price for another stock?";
		}
		
        return "Please enter proper Stock symbol";
	}
}
