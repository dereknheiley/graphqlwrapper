package com.example.takehome.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

// mostly followed https://www.baeldung.com/java-call-graphql-service
// TODO figure out how to get all the fancy graphql-java-generated magic working got stuck at missing _Any implementation errors
@Slf4j
@Service
public class ContinentQueryService {
	// TODO make configurable in application.properties
	public static final String URL = "https://countries.trevorblades.com/graphql";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/** {@value #URL}?query={countries (filter: {code: {in: [?]}}) {code, continent {name}}}"; */
	public Response query(List<String> inputCountryCodes) {
		// TODO get graphql-java working or manually create another class to parse this
//		return queryCountry("{countries (filter: {code: {in: [?]}}) {code, continent {name}}");
		throw new RuntimeException("TODO");
	}

	/** {@value #URL}?query={continents {name, countries {name}}}"; */
	public Response queryAll() {
		String response = query("{continents {name, countries {code}}}");
		return parseContinents(response);
	}

	private String query(String query) {
		log.trace("query() " + query);

		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(URL);
			URI uri = new URIBuilder(request.getURI()).addParameter("query", query).build();
			request.setURI(uri);
			HttpResponse httpResponse = httpClient.execute(request);
			int status = httpResponse.getStatusLine().getStatusCode();
			log.debug("query() '" + query + "' response StatusCode : '" + status + "'");
			String responseContent = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
			if (status == HttpStatus.OK.value()) {
				return responseContent;
			} else {
				throw new RuntimeException("Unexpected status: " + httpResponse.getStatusLine());
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Response parseContinents(String responseContent) {
		try {
			log.trace("parse() " + responseContent);
			Response response = objectMapper.readValue(responseContent, Response.class);
			log.debug("parse() read " + response.getData().getContinents().size() + " continents");
			return response;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}