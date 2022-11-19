package com.example.takehome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.takehome.model.Continent;
import com.example.takehome.service.ContinentService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
public class ContinentController {

	@Autowired ContinentService continentService;
	
	/** http://localhost:8080/continents?countries=CA,US,BZ */
	@ResponseBody
	@GetMapping(path = "/continents")
	public List<Continent> getContinents(@RequestParam(name="countries", required=true) List<String> countries) {
		// TODO authenticate request and apply rate limit and/or use built in spring niceties

		countries = countries.stream().map(String::toUpperCase).toList();
		log.trace("getContinent() request of " + countries + " countries");

		List<Continent> responseContinents = continentService.getContinentsFor(countries);

		// TODO tweak final response to exactly match specified format
		return responseContinents;
	}
}
