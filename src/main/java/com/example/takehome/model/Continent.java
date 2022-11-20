package com.example.takehome.model;

import java.util.HashSet;
import java.util.Set;

import com.example.takehome.models.graphql.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@ToString
public class Continent {
	
	public Continent(String name) {
		this.name = name;
	}

	@Getter @Setter private String name;
	@Getter @Setter private Set<String> countries = new HashSet<>();
	@Getter @Setter private Set<String> otherCountries = new HashSet<>();

	/** display in 'countries' instead of 'otherCountries' */
	public void highlight(String countryCode) {
		otherCountries.remove(countryCode);
		countries.add(countryCode);
	}

	/** this is only required if we want to maintain an internal cache */
	public Continent deepClone() {
		Continent clone = new Continent(this.name);
		clone.countries = new HashSet<>(this.countries);
		clone.otherCountries = new HashSet<>(this.otherCountries);
		return clone;
	}

	public static Continent from(Response.Continent responseContienent) {
		Continent continent = new Continent(responseContienent.getName());
		continent.otherCountries = new HashSet<>(responseContienent
				.getCountries()
				.stream()
				.map(Response.Country::getCode)
				.toList());
		log.trace("from(" + responseContienent.getName() + ") built: " + continent.name + ", " + continent.otherCountries.size() + " ", continent.countries.size());
		return continent;
	}
}
