package com.example.takehome.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Continent {
	
	public Continent(String name) {
		this.name = name;
	}

	@Getter @Setter private String name;
	@Getter @Setter private Set<String> countries = new HashSet<>();
	@Getter @Setter private Set<String> otherCountries = new HashSet<>();

	public void remove(String country) {
		otherCountries.remove(country);
		countries.add(country);
	}

	/** this is only required if we want to maintain an internal cache */
	public Continent deepClone() {
		Continent clone = new Continent(this.name);
		clone.countries = new HashSet<>(this.countries);
		clone.otherCountries = new HashSet<>(this.otherCountries);
		return clone;
	}

}
