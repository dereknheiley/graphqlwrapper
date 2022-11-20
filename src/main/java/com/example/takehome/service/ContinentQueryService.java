package com.example.takehome.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.takehome.models.graphql.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

// TODO figure out how to get all the fancy graphql-java-generated magic working re: missing _Any implementation
@Slf4j
@Service
public class ContinentQueryService {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	static final String URL = "https://countries.trevorblades.com/graphql"; // TODO make configurable in application.properties

	//TODO
	/** {@value #URL}?query={continents {name, countries {name in (inputCodes)}}}"; */
	public Response query(List<String> inputCountryCodes) {
		if (true!=false) throw new RuntimeException("TODO not yet implemented");
		return query("{continents {name, countries {code}}}");
	}

	/** {@value #URL}?query={continents {name, countries {name}}}"; */
	public Response queryAll() {
		return query("{continents {name, countries {code}}}");
	}

	// TODO mock this for testing to return parse(test-all-string) which is complicated because of how i init the cache with postconstruct :(
	public Response query(String query) {
		log.trace("query() " + query);
		//TODO circuitBreaker.check();

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			URI uri = new URIBuilder(request.getURI()).addParameter("query", query).build();
			request.setURI(uri);
			HttpResponse httpResponse = httpClient.execute(request);
			int status = httpResponse.getStatusLine().getStatusCode();
			log.debug("query() '" + query + "' response StatusCode : '" + status + "'");
			String responseContent = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
			if (status == HttpStatus.OK.value()) {
				Response response = parse(responseContent);
				return response;
			} else {
				throw new RuntimeException("Unexpected status: " + httpResponse.getStatusLine());
			}
		} catch (Exception e) {
			e.printStackTrace(); // TODO move into logger output
			log.error("query() " + query + " " + e.getMessage());
		}
		return null;
	}

	public static Response parse(String responseContent) throws JsonProcessingException {
		log.trace("parse() " + responseContent);
		Response reseponse = objectMapper.readValue(responseContent, Response.class);
		log.debug("parse() read " + reseponse.getData().getContinents().size() + " continents");
		return reseponse;
	}
}