package com.example.takehome.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.takehome.model.Continent;
import com.example.takehome.model.Continents;
import com.example.takehome.service.ContinentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ContinentController {

	@Autowired public ContinentService continentService;
	
	/** http://localhost:8080/continents?countries=CA 
	 * {"continent":[{"name":"North America","countries":["CA"],"otherCountries":["US",...]}]}
	 * */
	@ResponseBody
	@GetMapping(path = "/continents", produces = "application/json")
	public Continents getContinents(@RequestParam(name="countries", required=true) List<String> countryCodes) {
		// TODO authenticate request and apply rate limit and/or use built in spring niceties

		List<String> cleanedCountryCodes = countryCodes.stream()
				.filter(Objects::nonNull)
				.filter(s -> !s.isBlank())
				.map(String::toUpperCase)
				.toList();
		if (countryCodes.size() > cleanedCountryCodes.size()) {
			log.debug("getContinents() cleaned " + countryCodes + " to " + cleanedCountryCodes);
		}

		log.trace("getContinents() request of " + countryCodes + " country codes cleaned to " + cleanedCountryCodes);

		List<Continent> continents = continentService.getFor(cleanedCountryCodes);
		return new Continents(continents);
	}
}
