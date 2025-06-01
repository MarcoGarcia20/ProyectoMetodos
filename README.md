Proyecto de Métodos de Acceso  

Aplicación con Interfaz Gráfica en Java
----------------------------------------------------------
Descripción
Este es un proyecto académico desarrollado en Java con enfoque en la implementación y visualización de métodos numéricos, utilizando una interfaz gráfica amigable para facilitar la interacción del usuario. Está pensado como herramienta educativa, permitiendo experimentar con algoritmos clásicos desde un entorno visual.
----------------------------------------------------------
Funcionalidades
Gestión de datos a través de formularios gráficos.

Módulos para ingresar, validar, eliminar y consultar información de clientes.

Almacenamiento persistente de datos en archivos .dat.

Organización modular del código usando paquetes:

Entidades: lógica de negocio (Cliente, Reclamo, Servicio, etc.).

Ventanas: interfaz gráfica (formularios y validaciones).

Archivos: manejo de archivos binarios para persistencia de datos.

-----------------------------------------------------------

Objetivo académico
El propósito principal es demostrar el uso práctico de estructuras de datos, principios de POO y técnicas de persistencia, todo esto aplicado a un contexto de métodos numéricos. La interfaz gráfica refuerza el enfoque didáctico, facilitando pruebas y visualización de resultados.

Requisitos
Java Development Kit (JDK) 8+

Un IDE recomendado (como IntelliJ IDEA, NetBeans o Eclipse)

------------------------------------------------------------

Cómo ejecutar el proyecto
Clonar o descargar el repositorio ZIP y extraerlo.

Abrir el proyecto en tu IDE favorito.

Compilar todos los archivos (src si tu IDE lo requiere).

Ejecutar desde Principal.java o Aplicacion.java (dependiendo del punto de entrada real).

-------------------------------------------------------------

Estructura del proyecto
css
Copiar
Editar
ProyectoMetodos-main/
├── Aplicacion.java
├── Principal.java
├── README.md
├── Prueba1.txt
├── Archivos/
│   ├── Archivo.dat
│   ├── Clientes.dat
│   └── Sistema.dat
├── Entidades/
│   ├── Acceso.java
│   ├── Cliente.java
│   ├── Reclamo.java
│   ├── Servicio.java
│   └── Solucion.java
├── Ventanas/
│   ├── Datos.java
│   ├── Validador.java
│   ├── ValidarCliente.java
│   └── Ventanas relacionadas a gestión de cliente
