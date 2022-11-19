package com.example.takehome.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.takehome.model.Continent;

@SpringBootTest
public class ContinentServiceTest {
	
	@Autowired ContinentService continentService;
	
	@Test
	void emptyShouldWarn() {
		List<Continent> continents = continentService.getContinentsFor(List.of(""));
		assertTrue(continents.isEmpty());
	}

	@Test
	void single() {
		List<Continent> continents = continentService.getContinentsFor(List.of("CA"));
		assertFalse(continents.isEmpty());
		assertEquals(1, continents.size());
		assertEquals(Set.of("CA"), continents.get(0).getCountries());
		assertFalse(continents.get(0).getOtherCountries().isEmpty());
	}

	@Test
	void twoInSameContient() {
		List<Continent> continents = continentService.getContinentsFor(List.of("CA", "US"));
		assertFalse(continents.isEmpty());
		assertEquals(1, continents.size());
		assertEquals(Set.of("CA", "US"), continents.get(0).getCountries());
	}
	
	@Test
	void twoInDifferentContients() {
		List<Continent> continents = continentService.getContinentsFor(List.of("CA", "BZ"));
		assertFalse(continents.isEmpty());
		assertEquals(2, continents.size());
		assertEquals(Set.of("CA"), continents.get(1).getCountries());
		assertFalse(continents.get(1).getOtherCountries().isEmpty());
		assertEquals(Set.of("BZ"), continents.get(0).getCountries());
	}

	@Test
	void threeInDifferentContients() {
		List<Continent> continents = continentService.getContinentsFor(List.of("CA", "US", "BZ"));
		assertFalse(continents.isEmpty());
		assertEquals(2, continents.size());
		assertEquals(Set.of("CA", "US"), continents.get(1).getCountries());
		assertEquals(Set.of("BZ"), continents.get(0).getCountries());
	}
}
