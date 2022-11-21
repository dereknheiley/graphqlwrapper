package com.example.takehome.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** wrapper class to get the output to match exactly */
@NoArgsConstructor
@ToString
public class Continents {
	
	@Getter @Setter private List<Continent> continent;
	
	public Continents(List<Continent> continents) {
		this.continent = continents;
	}

}
