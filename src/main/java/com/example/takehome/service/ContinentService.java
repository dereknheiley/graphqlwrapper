package com.example.takehome.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.takehome.model.Continent;
import com.example.takehome.models.graphql.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContinentService {

	@Autowired public ContinentQueryService queryService;

	private Map<String, Continent> cachedContinentsByCountryCode = new HashMap<>();

	@PostConstruct
	void init() {
		log.trace("init");
		refreshCache();
	}

	@Scheduled(initialDelay = 5, fixedRate = 5, timeUnit = TimeUnit.MINUTES)
	private synchronized int refreshCache() {
		log.trace("refreshCache");
		Response allContinents = queryService.queryAll();
		HashMap<String, Continent> newCache = buildCountryToContinentCache(allContinents);
		if (!newCache.isEmpty()) {
			log.info("refreshCache built " + newCache.size() + " countries from " + allContinents.getData().getContinents().size() + " continents");
			this.cachedContinentsByCountryCode = newCache;
		} else {
			log.warn("refreshCache built nothing " + newCache.size() + " contries from " + allContinents.getData().getContinents().size()  + " continents");
		}
		return newCache.size();
	}

	public static HashMap<String, Continent> buildCountryToContinentCache(Response continents) {
		log.trace("loadCache() " + continents.getData().getContinents().size());
		HashMap<String, Continent> newCache = new HashMap<>();
		for (Response.Continent responseContienent : continents.getData().getContinents()) {
			Continent localContinent = Continent.from(responseContienent);
			log.debug("loadCache() checking '" + localContinent.getName() + "'");
			for (String countryCode : localContinent.getOtherCountries()) {
				Continent existingContinent = newCache.put(countryCode, localContinent);
				log.trace("loadCache() put '" + countryCode + "' : '" + localContinent.getName() + "' mapping");
				if (existingContinent != null) {
					log.warn("loadCache() country appeared in both '" + existingContinent.getName() + "' and '"
							+ localContinent.getName() + "', using the latter");
				}
			}
		}
		return newCache;
	}

	public List<Continent> getFor(List<String> inputCountryCodes) {
		List<Continent> results =  getFor(inputCountryCodes, this.cachedContinentsByCountryCode);
		
		// TODO see queryService.query(countryCodes), this isn't as simple as i had hoped with graphql
//		int worked = results.stream().mapToInt(r -> r.getCountries().size()).sum();
//		if (results.isEmpty() && !inputCountryCodes.isEmpty() && worked < inputCountryCodes.size()) {
//			log.debug("calling datasource directly because only " + worked + "/" + inputCountryCodes.size() + " country codes were found");
//		
//			Response queryResponse = queryService.query(inputCountryCodes);
//			HashMap<String, Continent> tempCache = buildCountryToContinentCache(queryResponse);
//			List<Continent> directResults =  getFor(inputCountryCodes, tempCache);
//			int workedBetter = directResults.stream().mapToInt(r -> r.getCountries().size()).sum();
//		
//			log.debug("calling datasource directly resulted in " + worked + " -> " + workedBetter + " country codes matched");
//			if (workedBetter > worked) {
//				results = directResults;
//			}
//		}

		return results;
	}

	private List<Continent> getFor(List<String> inputCountryCodes, Map<String, Continent> cache) {
		log.trace("getFor(" + inputCountryCodes + ") from cache with size " + cache.size());
		HashMap<String, Continent> responseBuilder = new HashMap<>();

		for (var countryCode : inputCountryCodes) {
			Continent cachedContinent = cache.get(countryCode);
			if (cachedContinent == null) {
				log.warn("Unknown country code '" + countryCode + "' from cache of " + cache.size() + ", skipping");
				continue;
			}

			Continent responseContinent = responseBuilder.get(cachedContinent.getName());
			if (responseContinent == null) {
				responseContinent = cachedContinent.deepClone();
			}
			responseContinent.highlight(countryCode);
			responseBuilder.put(cachedContinent.getName(), responseContinent);
		}

		List<Continent> continents = responseBuilder.values().stream().toList();
		return continents;
	}

}
