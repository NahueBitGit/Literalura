# Literalura

Literalura es una aplicación de consola para gestionar una biblioteca virtual, permitiendo la búsqueda y registro de libros desde la API de Gutendex, la gestión de autores, y el filtrado de libros por diferentes criterios.

## Funcionalidades

1. **Buscar libro por título:**
   - Realiza una consulta a la API de Gutendex y registra el libro si no existe en la base de datos.
   
2. **Listar libros registrados:**
   - Muestra una lista de todos los libros registrados en la base de datos, ordenados alfabéticamente por título.

3. **Listar autores registrados:**
   - Muestra una lista de autores registrados en la base de datos, asegurando que no se repitan nombres duplicados.

4. **Listar autores vivos en un determinado año:**
   - Filtra y muestra autores que estaban vivos en un año específico ingresado por el usuario.

5. **Listar libros por idioma:**
   - Permite filtrar libros por idioma (es, en, fr).

6. **Salir:**
   - Finaliza la ejecución de la aplicación.

## Requisitos previos

- **Java Development Kit (JDK)** 17 o superior.
- **Maven** para gestionar dependencias.
- **Base de datos PostgreSQL** configurada y accesible.
- Conexión a Internet para interactuar con la API de Gutendex.
- Jackson mapping de JSON.

## Uso

Al iniciar la aplicación, se mostrará un menú interactivo:
```
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Listar libros por idioma
0 - Salir
```
- Seleccione una opción ingresando el número correspondiente.
- Siga las instrucciones para ingresar datos adicionales cuando sea necesario.

## Estructura del Proyecto

- **`Principal`**: Clase principal que gestiona la lógica del menú y coordina las acciones.
- **`model`**: Contiene las clases de dominio, como `Autor`, `Libro` y `DatosLibro`.
- **`repository`**: Contiene las interfaces para interactuar con la base de datos.
- **`service`**: Incluye clases para consumir la API de Gutendex y convertir datos JSON.

## Ejemplo de Uso

### Buscar un libro:
1. Seleccione la opción `1` del menú.
2. Ingrese el título del libro (por ejemplo, "Romeo").
3. La aplicación buscará el libro en la API y lo registrará en la base de datos si no existe.

### Listar autores vivos:
1. Seleccione la opción `4` del menú.
2. Ingrese el año deseado.
3. La aplicación mostrará los autores que estaban vivos en ese año.

## Notas importantes

- El proyecto asume que solo se trabajará con los idiomas `es`, `en` y `fr`.
- La aplicación es sensible a errores de entrada y manejará valores no válidos con mensajes adecuados.
- Se utiliza `Optional` y manejo de excepciones para robustecer las operaciones.
- Se requiere IntelliJ IDEA para el Ambiente de Desarrollo y Control de versiones.
- Base de datos: Nombre: alura_literalura. Usuario: nahueltem.

## Contribución

¡Las contribuciones son bienvenidas! Por favor, crea un fork del repositorio y envía un pull request con tus mejoras.

## Licencia

Este proyecto está bajo la Licencia MIT.

