package com.example.takehome.models.graphql;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** TODO auto generate graphql data structure from magic libraries
 * {"data":{"continents":[{"name":"Africa","countries":[{"code":"AO"}, ... */

@NoArgsConstructor
@ToString
public class Response {
	@Getter @Setter private Data data;
	
	@NoArgsConstructor
	@ToString
	public static class Data {
		@Getter @Setter private List<Continent> continents;
	}

	@NoArgsConstructor
	@ToString
	public static class Continent {
		@Getter @Setter private String name;
		@Getter @Setter private List<Country> countries;
	}

	@NoArgsConstructor
	@ToString
	public static class Country {
		@Getter @Setter private String code;
	}
}
