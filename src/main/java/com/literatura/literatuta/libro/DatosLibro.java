package com.literatura.literatuta.libro;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.literatura.literatuta.autor.Autor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<Autor> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double numeroDescargas) {

    public String getTitle() {
        return titulo;
    }

    public String getAuthors() {
        return autores.isEmpty() ? "Desconocido" : autores.get(0).nombre();
    }

    public String getLanguage() {
        return idiomas.isEmpty() ? "Desconocido" : idiomas.get(0);
    }
}

