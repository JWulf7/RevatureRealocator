package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Preference;
import com.revature.models.Property;
import com.revature.models.SimpleSearchBody;
import com.revature.services.APIParse;
import com.revature.services.PropertyService;
import com.revature.util.APIUtil;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class PropertyController {
	@Autowired 
	private PropertyService ps;
	
	@Autowired
	private APIUtil api;
	
	
	@PostMapping("/propsearch")
	public List<Property> getProperties(@RequestBody Preference pref){
		//Get POST request values and create a preference object to create an API call with
		String query = api.createQueryByPreference(pref);
		List<Property> list = APIParse.parse(api.getResponse(query).toString()); //TODO: Map the response body to Properties and return them
		return list;
	}
	
	@PostMapping("/propsearch")
	public List<Property> getPropertiesSimpleSearch(@RequestBody SimpleSearchBody ssb){
		String state_code = ssb.getState_code();
		String city = ssb.getCity();
		String query = api.createSimpleQuery(state_code, city);
		List<Property> list = APIParse.parse(api.getResponse(query).toString());
		return list;
	}
	
	@PostMapping("/propsearch")
	public List<Property> getPropertiesSimpleSearch(@RequestBody Preference pref){
		String query = api.createSimpleQuery(pref);
		List<Property> list = APIParse.parse(api.getResponse(query).toString()); //TODO: Map the response body to Properties and return them
		return list;
	}
	
	@PostMapping("/propsearch")
	public List<Property> getProperties(String state_code, String city){
		//Get POST request values and create a preference object to create an API call with
		String query = api.createSimpleQuery(state_code, city);
		List<Property> list = APIParse.parse(api.getResponse(query).toString()); //TODO: Map the response body to Properties and return them
		return list;
	}	
}