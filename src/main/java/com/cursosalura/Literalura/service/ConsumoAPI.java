package com.cursosalura.Literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Error en la respuesta: " + response.statusCode());
                return null;
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al realizar la solicitud: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
