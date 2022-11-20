package com.example.takehome.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class Continents {
	
	@Getter @Setter private List<Continent> continent;
	
	public Continents(List<Continent> continents) {
		this.continent = continents;
	}

}
