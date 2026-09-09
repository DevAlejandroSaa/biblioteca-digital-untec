# Biblioteca Digital UNTEC

Proyecto web desarrollado con **Java EE**, utilizando **Java 8** y **Apache Tomcat 9**.

La aplicación corresponde a un sistema de gestión para una biblioteca digital, con funcionalidades diferenciadas para estudiantes y personal de biblioteca.

## Usuarios

El sistema contempla dos tipos de usuario.

### Estudiante

**Usuario:** `estudiante`  
**Contraseña:** `1234`

El estudiante puede:

- Buscar libros.
- Solicitar libros.
- Consultar cuántos libros ha solicitado.
- Consultar los libros que tiene en préstamo.
- Consultar la fecha de devolución de cada libro.

### Personal de biblioteca

**Usuario:** `personal`  
**Contraseña:** `1234`

El personal de biblioteca puede:

- Buscar libros.
- Registrar nuevos libros.
- Modificar información de los libros.
- Consultar el total de libros.
- Consultar cuántos libros han sido prestados.
- Entregar libros a los estudiantes.
- Recepcionar libros devueltos.
- Consultar qué estudiantes presentan morosidad en la entrega de libros.
- Consultar qué libros han sido entregados.

## Acceso al sistema

La aplicación dispone de dos cuentas de prueba:

| Usuario | Contraseña | Tipo |
|---|---|---|
| `estudiante` | `1234` | Estudiante |
| `personal` | `1234` | Personal de biblioteca |

Las funcionalidades disponibles dependen del tipo de usuario que inicia sesión.

## Arquitectura

El proyecto utiliza el patrón **MVC (Model-View-Controller)** para separar las responsabilidades de la aplicación.

```text
Vista (JSP)
     │
     ▼
Controlador (Servlet)
     │
     ▼
Modelo / DAO
````

La aplicación utiliza **JSP, Servlets y JSTL** para implementar la interfaz y el flujo de navegación.