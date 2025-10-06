# Módulo de Gestión de Usuarios y Persistencia

Este documento detalla la implementación de la capa de persistencia y la gestión de la entidad `Usuario` para el Sistema de Tickets. El módulo establece la conexión a la base de datos, define el acceso a los datos a través del patrón DAO y proporciona una interfaz de usuario interactiva para las operaciones básicas de CRUD (Crear, Leer).

## Responsable
* **Samuel Zapata:** Responsable del Backend persistente y DAOs (Usuario + Infraestructura de BD).

***

## Funcionalidades Implementadas

Se ha desarrollado un sistema de gestión de usuarios autocontenido con las siguientes características:

### 1. Sistema de Gestión de Usuarios (CRUD Básico)
La aplicación permite realizar las siguientes operaciones sobre los usuarios en la base de datos:

* **Crear Usuario:** A través de un formulario interactivo, se solicita un nombre de usuario y se permite seleccionar un rol (`REPORTER` o `ASSIGNEE`) desde una lista desplegable. El nuevo usuario es persistido en la base de datos.
* **Listar Todos los Usuarios:** Muestra una lista completa de todos los usuarios registrados en la base de datos, presentando su ID, nombre de usuario y rol en una ventana con barra de scroll para facilitar la visualización.
* **Buscar Usuario por ID:** Permite al usuario introducir un ID numérico para buscar a un usuario específico. Si el usuario es encontrado, se muestran todos sus detalles; de lo contrario, se informa que no existe.

### 2. Interfaz de Usuario Interactiva
* Se utiliza la librería **`javax.swing.JOptionPane`** para crear una interfaz de usuario gráfica y sencilla.
* La aplicación presenta un **menú principal en bucle** que permite al usuario seleccionar la operación que desea realizar.
* Se incluyen validaciones de entrada y se muestran mensajes de confirmación, error o advertencia para guiar al usuario.

### 3. Conectividad a Base de Datos PostgreSQL
* La conexión a la base de datos se gestiona de forma centralizada a través de la clase `config.ConnectionDb.java`.
* Se utiliza el driver **JDBC** para PostgreSQL para establecer la comunicación y ejecutar consultas SQL.

***

## Arquitectura y Diseño

El proyecto sigue una arquitectura por capas bien definida para separar responsabilidades, lo que facilita su mantenimiento y escalabilidad.

1.  **Capa de Presentación (UI):**
    * `Main.java`: Punto de entrada de la aplicación. Su única responsabilidad es instanciar y lanzar el menú.
    * `app.MenuUser.java`: Gestiona el bucle del menú principal y la interacción directa con el usuario a través de `JOptionPane`. Delega todas las acciones al controlador.

2.  **Capa de Controlador:**
    * `controller.MenuController.java`: Actúa como el cerebro de la aplicación. Recibe las peticiones del `app.MenuUser`, procesa la lógica de negocio (como pedir datos al usuario) y se comunica con la capa de datos (DAO) para persistir o recuperar información.

3.  **Capa de Acceso a Datos (DAO):**
    * `dao.UsuarioDao.java`: Es la **interfaz** que define el contrato de las operaciones de base de datos para los usuarios. Esto desacopla el controlador de la implementación específica.
    * `dao.UsuarioDaoJDBC.java`: Es la **implementación** concreta que utiliza JDBC y SQL para realizar las operaciones definidas en la interfaz `UsuarioDao`.

4.  **Capa de Dominio (Modelo):**
    * `domain.Usuario.java`: Es la clase (POJO) que modela la entidad `users` de la base de datos.
    * `domain.Role.java`: Es un `enum` para garantizar que los roles de usuario sean siempre válidos (`REPORTER` o `ASSIGNEE`).

***

## Cómo Ejecutar el Proyecto

Sigue estos pasos para poner en marcha el módulo de gestión de usuarios:

### 1. Requisitos Previos
* Java JDK 11 o superior.
* Una base de datos PostgreSQL activa (local o en la nube como Supabase).
* Haber ejecutado el script SQL para crear la tabla `users`.

### 2. Configurar el Driver JDBC
1.  Descarga el **driver JDBC de PostgreSQL** desde su sitio oficial.
2.  Crea una carpeta `lib` en la raíz del proyecto.
3.  Copia el archivo `.jar` del driver en la carpeta `lib`.
4.  Asegúrate de que tu IDE (IntelliJ, Eclipse) haya añadido este `.jar` al **classpath** del proyecto.

### 3. Configurar la Conexión
**Este es el paso más importante.** Abre el archivo `src/config/ConnectionDb.java` y rellena las constantes con tus credenciales de la base de datos:

```java
// src/config/ConnectionDb.java
public class ConnectionDb {
    private static final String HOST = "tu_host_aqui";
    private static final String PORT = "5432";
    private static final String DATABASE = "postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tu_contraseña_aqui";
    // ...
}