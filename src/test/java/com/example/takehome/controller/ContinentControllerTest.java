package com.example.takehome.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.example.takehome.model.Continents;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ContinentControllerTest {

	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void badRequest() throws Exception {
		for (var bad : List.of(
				"http://localhost:" + this.port + "/continents",
				"http://localhost:" + this.port + "/continents?foo"
		)) { 
			ResponseEntity<?> response = this.testRestTemplate.getForEntity(bad, Map.class);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "failed for " + bad);
		}
	}

	@Test
	void notFound() throws Exception {
		for (var nope : List.of(
				"http://localhost:" + this.port + "/countries",
				"http://localhost:" + this.port + "/countries?continents"
		)) { 
			ResponseEntity<?> response = this.testRestTemplate.getForEntity(nope, Map.class);
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "failed for " + nope);
		}
	}

	@Test
	void controller_single() throws Exception {
		String url = "http://localhost:" + this.port + "/continents?countries=CA";
		ResponseEntity<?> response = this.testRestTemplate.getForEntity(url, Continents.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// TODO force unit test to request/get json instead
		//browser: {"continent":[{"name":"North America","countries":["CA"],"otherCountries":["US"]}]}
		//unitest: Continents(continent=[Continent(name=North America, countries=[CA], otherCountries=[US])])
		assertTrue(response.getBody().toString().contains("Continents(continent=[Continent(name=North America, countries=[CA], otherCountries=["));
	}

	@Test
	void controller_double() throws Exception {
		String url = "http://localhost:" + this.port + "/continents?countries=cA,br";
		ResponseEntity<?> response = this.testRestTemplate.getForEntity(url, Continents.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		// TODO force unit test to request/get json instead
		//browser: {"continent":[{"name":"South America","countries":["BR"],"otherCountries":[]},{"name":"North America","countries":["CA"],"otherCountries":["US"]}]}
		//unitest: Continents(continent=[Continent(name=South America, countries=[BR], otherCountries=[]), Continent(name=North America, countries=[CA], otherCountries=[US])])
		assertTrue(response.getBody().toString().contains("Continents(continent=[Continent("));
		assertTrue(response.getBody().toString().contains("Continent(name=North America, countries=[CA], otherCountries=["));
		assertTrue(response.getBody().toString().contains("Continent(name=South America, countries=[BR], otherCountries=["));
	}

	@Test
	void management() throws Exception {
		String url = "http://localhost:" + this.mgt + "/actuator";
		ResponseEntity<?> response = this.testRestTemplate.getForEntity(url, Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}