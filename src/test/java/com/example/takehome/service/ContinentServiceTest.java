package com.example.takehome.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.takehome.model.Continent;
import com.example.takehome.models.graphql.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
public class ContinentServiceTest {
	
	@Autowired ContinentService continentService;

	@Test
	void emptyShouldWarn() {
		List<Continent> continents = continentService.getFor(List.of(""));
		assertTrue(continents.isEmpty());
	}

	@Test
	void buildCache() throws JsonProcessingException {
		Response response = ContinentQueryService.parse(ContinentQueryServiceTest.allGraphqlResults);
		HashMap<String, Continent> continents = ContinentService.buildCountryToContinentCache(response);
		assertNotNull(continents);
		assertFalse(continents.isEmpty());
	}
	
}
