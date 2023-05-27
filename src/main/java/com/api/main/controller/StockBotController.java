package com.api.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class StockBotController {
	
	private final RestTemplate restTemplate;

	@Autowired
	public StockBotController(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	@PostMapping(path = "/api/stock/stockPrice/{ticker}")
    public String getStockPrice(@PathVariable(value="ticker") String ticker)
    {
		ticker = ticker+".NS";
		String url = "https://query1.finance.yahoo.com/v11/finance/quoteSummary";
		url = url+"/"+ticker+"?modules=financialData";
		
		String currentPrice = "";
		try {
			String jsonString = restTemplate.getForObject(url, String.class);
			
			JSONObject jsonObj = new JSONObject(jsonString);

			currentPrice = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) jsonObj
					.get("quoteSummary")).get("result")).get(0)).get("financialData")).get("currentPrice")).get("fmt");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentPrice != null && !currentPrice.equals(""))
			return currentPrice;
		
        return "Please enter proper Stock symbol";
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/")
	public String getMessage() {
		return "Welcome";
	}
}

