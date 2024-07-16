package com.literatura.literatuta.pricipal;

import com.literatura.literatuta.libro.DatosLibro;
import com.literatura.literatuta.libro.Libro;
import com.literatura.literatuta.libro.LibroRepository;
import com.literatura.literatuta.service.ApiResponse;
import com.literatura.literatuta.service.ConsumoAPI;
import com.literatura.literatuta.service.ConvierteDatos;

import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por titulo
                    2 - Listar libros registrados
                    3 - Listar Autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibros();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosEnUnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;    
                case 0:
                    System.out.println("Hasta pronto");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }
    private List<DatosLibro> getDatosLibrosPorIdioma(String idioma) {
        String url = String.format("https://gutendex.com/books/?languages=%s", idioma);
        var json = consumoApi.obtenerDatos(url);
        ApiResponse apiResponse = conversor.obtenerDatos(json, ApiResponse.class);
        return apiResponse.results();
    }


    private void listarLibrosPorIdioma() {
        System.out.println("Seleccione el idioma en el que desea buscar libros:");
        System.out.println("1. Español (es)");
        System.out.println("2. Inglés (en)");
        System.out.println("3. Francés (fr)");
        System.out.println("4. Finlandés (fi)");
        System.out.print("Ingrese la opción (1-4): ");

        String idioma = "";
        int opcion = Integer.parseInt(teclado.nextLine());

        switch (opcion) {
            case 1:
                idioma = "es";
                break;
            case 2:
                idioma = "en";
                break;
            case 3:
                idioma = "fr";
                break;
            case 4:
                idioma = "fi";
                break;
            default:
                System.out.println("Opción no válida. Intente de nuevo.");
                return;
        }

        List<DatosLibro> datosLibros = getDatosLibrosPorIdioma(idioma);
        for (DatosLibro datosLibro : datosLibros) {
            System.out.println("------- LIBRO -----");
            System.out.println("Título: " + datosLibro.getTitle());
            System.out.println("Autor(es): " + datosLibro.getAuthors());
            System.out.println("Idioma: " + datosLibro.getLanguage());
            System.out.println("-------------------");
            System.out.println(" ");
        }
    }


    private List<DatosLibro> getDatosLibrosPorAnio(int anio) {
        String url = String.format("https://gutendex.com/books/?author_year_start=%d&author_year_end=%d", anio, anio);
        var json = consumoApi.obtenerDatos(url);
        ApiResponse apiResponse = conversor.obtenerDatos(json, ApiResponse.class);
        System.out.println("Buscando libros...");
        return apiResponse.results();
    }

    private void listarAutoresVivosEnUnDeterminadoAnio() {
        System.out.println("Introduce el año en el que desea buscar autores vivos: ");
        int anio = Integer.parseInt(teclado.nextLine());
        List<DatosLibro> datosLibros = getDatosLibrosPorAnio(anio);
        for (DatosLibro datosLibro : datosLibros) {
            System.out.println("------- LIBRO -----");
            System.out.println("Título: " + datosLibro.getTitle());
            System.out.println("Autor(es): " + datosLibro.getAuthors());
            System.out.println("-------------------");
            System.out.println(" ");
        }
    }


    private void listarAutores() {
        List<Libro> libros = repository.findAll();
        Set<String> autores = new HashSet<>();
        for (Libro libro : libros) {
            autores.add(libro.getAutor());
        }
        for (String autor : autores) {
            System.out.println("------- AUTOR -----");
            System.out.println(autor);
            System.out.println("-------------------");
        }
    }

    private void listarLibros() {
        List<Libro> libros = repository.findAll();
        for (Libro libro : libros) {
            System.out.println("------- LIBRO -----");
            System.out.println(libro);
            System.out.println("-------------------");
            System.out.println(" ");
        }
    }

    private List<DatosLibro> getDatosLibros() {
        System.out.println("Introduce el titulo del libro que desea buscar: ");
        var titulo = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + titulo.replace(" ", "+"));
        ApiResponse apiResponse = conversor.obtenerDatos(json, ApiResponse.class);
        return apiResponse.results();
    }

    private void buscarLibros() {
        List<DatosLibro> datosLibros = getDatosLibros();
        for (DatosLibro datosLibro : datosLibros) {
            Libro libro = new Libro(datosLibro);
            repository.save(libro);
            System.out.println("------- LIBRO -----");
            System.out.println(libro);
            System.out.println("-------------------");
            System.out.println(" ");
        }
    }
}
