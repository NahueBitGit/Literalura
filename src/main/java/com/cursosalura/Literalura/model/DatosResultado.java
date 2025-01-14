package com.cursosalura.Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResultado(@JsonAlias("count") int cantidad, @JsonAlias("results") List<DatosLibro> resultado) {

}
