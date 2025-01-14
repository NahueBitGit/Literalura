package com.cursosalura.Literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", referencedColumnName = "id")
    private Autor autor;
    @ElementCollection(targetClass = Idioma.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Enumerated(EnumType.STRING)
    private List<Idioma> idiomas;
    private long descargas;

    public Libro(DatosLibro datosLibro, Autor autor, List<Idioma> idioma){
        this.titulo = datosLibro.titulo();
        this.autor = autor;
        this.idiomas = idioma;
        this.descargas = datosLibro.descargas();
        this.id = datosLibro.id();
    }

    public Libro(DatosLibro datosLibro, List<Idioma> idioma){
        this.titulo = datosLibro.titulo();
        this.idiomas = idioma;
        this.descargas = datosLibro.descargas();
        this.id = datosLibro.id();
    }

    public Libro() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public long getDescargas() {
        return descargas;
    }

    public void setDescargas(long descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idiomas=" + idiomas +
                ", descargas=" + descargas +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
