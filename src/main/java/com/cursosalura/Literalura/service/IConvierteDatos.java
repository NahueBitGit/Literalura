package com.cursosalura.Literalura.service;

public interface IConvierteDatos {
   <T> T obtenerDatos(String json, Class <T> clase);

}
