package com.example.takehome.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.takehome.model.Continent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContinentService {

	// TODO decide how to get
	public static final String URI = "https://countries.trevorblades.com/graphql";

	// TODO maybe lazy load cache on first request, and then on circuit breaker schedule?
	public final HashMap<String, Continent> localContinentsCacheByCountryCode = new HashMap<>();
	
	// TODO refactor test code
	{
		Continent northAmerica = new Continent("North America");
		northAmerica.getOtherCountries().add("CA");
		northAmerica.getOtherCountries().add("US");
		Continent southAmerica = new Continent("South America");
		southAmerica.getOtherCountries().add("BZ");
		localContinentsCacheByCountryCode.put("CA", northAmerica);
		localContinentsCacheByCountryCode.put("US", northAmerica);
		localContinentsCacheByCountryCode.put("BZ", southAmerica);
	}

	public List<Continent> getContinentsFor(List<String> inputCountries) {
		// parse inputs and lookup continent(s) from local cache
		HashMap<String, Continent> responseContinents = new HashMap<>();
		for (var country : inputCountries) {
			Continent continent = localContinentsCacheByCountryCode.get(country);
			if (continent == null) {
				log.warn("Unknown country code '" + country + "', skipping");
				continue;
			}

			Continent responseContinent = responseContinents.get(continent.getName());
			if (responseContinent == null) {
				responseContinent = continent.deepClone();
			}
			responseContinent.remove(country); // remove from set of 'otherCountries', add to set of 'countries' for response
			responseContinents.put(continent.getName(), responseContinent);
		}
		return responseContinents.values().stream().toList();
	}

}
