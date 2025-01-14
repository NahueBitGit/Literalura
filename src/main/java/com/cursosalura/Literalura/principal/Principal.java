package com.cursosalura.Literalura.principal;

import com.cursosalura.Literalura.model.*;
import com.cursosalura.Literalura.service.ConsumoAPI;
import com.cursosalura.Literalura.service.ConvierteDatos;
import com.cursosalura.Literalura.repository.LibroRepository;
import com.cursosalura.Literalura.repository.AutorRepository;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                0 - Salir
                """;
            System.out.println(menu);

            try {
                System.out.print("Seleccione una opción: ");
                opcion = teclado.nextInt();
                teclado.nextLine(); // Limpia el buffer
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entre 0 y 5.");
                teclado.nextLine(); // Limpia el buffer en caso de entrada inválida
                continue; // Reinicia el ciclo del menú
            }

            switch (opcion) {
                case 1:
                    registrarLibro();
                    break;
                case 2:
                    LibrosRegistrados();
                    break;
                case 3:
                    AutoresRegistrados();
                    break;
                case 4:
                    AutoresVivos();
                    break;
                case 5:
                    LibrosIdioma();
                    break;
                case 0:
                    System.out.println("Fin de la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un número entre 0 y 5.");
            }
        }
    }

    private Optional<DatosLibro> getDatosLibro() {
        System.out.println("Nombre del libro a buscar");

        String nombreLibro;

        try {
            nombreLibro = teclado.nextLine();
            if (nombreLibro.isBlank()) {
                System.out.println("El nombre del libro no puede estar vacío. Inténtalo de nuevo.");
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("Error al leer el nombre del libro. Inténtalo de nuevo.");
            return Optional.empty();
        }

        var url = URL_BASE + nombreLibro.replace(" ", "+");
        System.out.println("Buscando en la URL: " + url);

        try {
            var json = consumoAPI.obtenerDatos(url);
            DatosResultado respuestaAPI = conversor.obtenerDatos(json, DatosResultado.class);
            List<DatosLibro> libros = respuestaAPI.resultado();

            if (libros.isEmpty()) {
                System.out.println("No se encontraron resultados para el libro especificado.");
                return Optional.empty();
            }

            return libros.stream().findFirst();
        } catch (Exception e) {
            System.out.println("Ocurrió un error al acceder a la API o procesar los datos: " + e.getMessage());
            return Optional.empty();
        }
    }

    private void registrarLibro() {
        Optional<DatosLibro> datosLibro = getDatosLibro();
        if (datosLibro.isEmpty()) {
            System.out.println("Libro no encontrado");
        } else {
            DatosAutor datosAutor = datosLibro.get().autor().get(0);
            Autor autor = new Autor(datosAutor);

            Optional<Libro> libroExistente = libroRepository.findById(datosLibro.get().id());
            if (libroExistente.isPresent()) {
                Libro libro = libroExistente.get();
                System.out.println("El libro ya existe en la base de datos " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor().getNombre());
                return;
            }

            List<Idioma> idiomas = datosLibro.get().idiomas().stream()
                    .map(String::toLowerCase)
                    .filter(lang -> lang.equals("es") || lang.equals("en") || lang.equals("fr"))
                    .map(Idioma::valueOf)
                    .toList();

            autorRepository.save(autor);
            System.out.println("Autor registrado " + autor);
            Libro unLibro = new Libro(datosLibro.get(), autor, idiomas);
            libroRepository.save(unLibro);
            System.out.println("Libro registrado " + unLibro);
        }
    }

    private void LibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        System.out.println("=".repeat(50));
        System.out.printf("| %-5s | %-30s | %-10s |\n", "ID", "Título", "Descargas");
        System.out.println("=".repeat(50));
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> System.out.printf("| %-5d | %-30s | %-10d |\n",
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getDescargas()));
        System.out.println("=".repeat(50));
    }

    private void AutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        Set<String> nombresUnicos = new HashSet<>();

        autores.stream()
                .filter(autor -> nombresUnicos.add(autor.getNombre())) // Agrega el nombre al Set si aún no está
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> System.out.printf("Nombre: %s | Año de Nacimiento: %d | Año de Muerte: %s%n",
                        autor.getNombre(),
                        autor.getNacimiento(),
                        autor.getMuerte() != null ? autor.getMuerte() : "N/A"));
    }

    private void AutoresVivos() {
        System.out.println("Año que desea buscar autores vivos ");
        int anio;

        try {
            anio = teclado.nextInt();
            teclado.nextLine(); // Limpia el buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido para el año.");
            teclado.nextLine(); // Limpia el buffer para evitar bucles infinitos
            return;
        }

        List<Autor> autoresVivos = autorRepository.findAll().stream()
                .filter(autor -> autor.getNacimiento() <= anio &&
                        (autor.getMuerte() == null || autor.getMuerte() > anio))
                .sorted(Comparator.comparing(Autor::getNombre))
                .toList();
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos");
        } else {
            autoresVivos.forEach(autor -> System.out.printf("Nombre: %s | Año de Nacimiento: %d | Año de Muerte: %s%n",
                    autor.getNombre(),
                    autor.getNacimiento(),
                    autor.getMuerte() != null ? autor.getMuerte() : "N/A"));
        }
    }

    private void LibrosIdioma() {
        System.out.println("Idioma: ");
        System.out.println("1. es");
        System.out.println("2. en");
        System.out.println("3. fr");

        int opcion;
        try {
            opcion = teclado.nextInt();
            teclado.nextLine(); // Limpia el buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número entre 1 y 3.");
            teclado.nextLine(); // Limpia el buffer para evitar bucles infinitos
            return;
        }

        String idioma;
        switch (opcion) {
            case 1 -> idioma = "es";
            case 2 -> idioma = "en";
            case 3 -> idioma = "fr";
            default -> {
                System.out.println("Opción inválida. Ingresar un número entre 1 y 3.");
                return;
            }
        }

        List<Libro> librosPorIdioma = libroRepository.findAll().stream()
                .filter(libro -> libro.getIdiomas().contains(Idioma.valueOf(idioma)))
                .sorted(Comparator.comparing(Libro::getTitulo))
                .toList();

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            librosPorIdioma.forEach(libro -> System.out.printf(
                    "ID: %d | Título: %s | Autor: %s | Idiomas: %s | Descargas: %d%n",
                    libro.getId(), libro.getTitulo(), libro.getAutor().getNombre(), libro.getIdiomas(), libro.getDescargas()));
        }
    }

}

