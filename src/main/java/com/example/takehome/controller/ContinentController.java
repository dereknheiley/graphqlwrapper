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
public class ContinentController extends BaseController {

	public static final String CONTINENTS = "/continents";
	public static final String COUNTRIES = "countries";
	public static final String CONTINENTS_COUNTRIES = CONTINENTS + "?" + COUNTRIES + "=";

	@Autowired public ContinentService continentService;

	/** http://localhost:8080/continents?countries=CA 
	 * {"continent":[{"name":"North America","countries":["CA"],"otherCountries":["US",...]}]}
	 * */
	@ResponseBody
	@GetMapping(path = CONTINENTS, produces = "application/json")
	public Continents getContinents(@RequestParam(name=COUNTRIES, required=true) List<String> countryCodes) {
		rateLimit();

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
