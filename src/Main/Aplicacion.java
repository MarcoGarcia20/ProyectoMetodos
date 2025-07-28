package src.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;

import src.Entidades.Cliente;
import src.Entidades.Reclamo;
import src.Estructuras.ArbolB;
import src.Persistencia.IndiceClienteArchivo;

public class Aplicacion {

    public Aplicacion() {
        // --- PASO 1: Configuración de la prueba ---
        int ordenDelArbol = 16;
        int numeroDeRegistros = 10000;
        ArbolB miArbol = new ArbolB(ordenDelArbol);

        System.out.println("Iniciando prueba masiva para un Árbol B de orden " + ordenDelArbol);
        System.out.println("Número de registros a insertar: " + numeroDeRegistros);
        System.out.println("-------------------------------------------------");

        // --- PASO 2: Generar claves en orden aleatorio ---
        System.out.println("Generando y mezclando claves...");
        List<String> clavesParaInsertar = new ArrayList<>();
        for (int i = 0; i < numeroDeRegistros; i++) {
            // Usamos formato para que todas las claves tengan la misma longitud (ej: "00000", "00001")
            String clave = String.format("%05d", i);
            clavesParaInsertar.add(clave);
        }
        Collections.shuffle(clavesParaInsertar); // ¡Mezclamos para una inserción más realista!
        System.out.println("Claves mezcladas. Listo para insertar.");

        // --- PASO 3: Insertar los registros y medir el tiempo ---
        System.out.println("\nIniciando inserción masiva...");
        long tiempoInicioInsercion = System.nanoTime();

        for (String clave : clavesParaInsertar) {
            miArbol.insertar(clave, "Dato para " + clave);
        }

        long tiempoFinInsercion = System.nanoTime();
        long duracionInsercionMs = TimeUnit.NANOSECONDS.toMillis(tiempoFinInsercion - tiempoInicioInsercion);
        System.out.println("¡Inserción completada!");
        System.out.println("Tiempo total de inserción: " + duracionInsercionMs + " ms");
        System.out.println("-------------------------------------------------");

        // --- PASO 4: Verificación y pruebas ---
        System.out.println("\nIniciando verificaciones...");

        // 4.1. Verificar el tamaño del árbol
        List<String> listado = miArbol.listarInOrden();
        System.out.println("Verificación de tamaño: Se insertaron " + numeroDeRegistros + " registros, el listado contiene " + listado.size() + " registros.");
        if (listado.size() == numeroDeRegistros) {
            System.out.println("=> PRUEBA DE TAMAÑO: CORRECTA");
        } else {
            System.out.println("=> PRUEBA DE TAMAÑO: FALLIDA");
        }

        // 4.2. Buscar claves específicas que sabemos que existen
        String claveExistente1 = "00001";
        String claveExistente2 = "05000";
        String claveExistente3 = "09999";
        
        System.out.println("\nBuscando clave '" + claveExistente1 + "': " + (miArbol.buscar(claveExistente1) != null ? "Encontrada" : "No encontrada"));
        System.out.println("Buscando clave '" + claveExistente2 + "': " + (miArbol.buscar(claveExistente2) != null ? "Encontrada" : "No encontrada"));
        System.out.println("Buscando clave '" + claveExistente3 + "': " + (miArbol.buscar(claveExistente3) != null ? "Encontrada" : "No encontrada"));

        // 4.3. Buscar una clave que no existe
        String claveInexistente = "10000";
        System.out.println("Buscando clave '" + claveInexistente + "': " + (miArbol.buscar(claveInexistente) != null ? "Encontrada" : "No encontrada"));
        System.out.println("=> PRUEBA DE BÚSQUEDA: Se espera que las 3 primeras se encuentren y la última no.");

        // 4.4. Medir el tiempo de una búsqueda
        long tiempoInicioBusqueda = System.nanoTime();
        miArbol.buscar(claveExistente2);
        long tiempoFinBusqueda = System.nanoTime();
        long duracionBusquedaNs = tiempoFinBusqueda - tiempoInicioBusqueda;
        System.out.println("\nTiempo de una búsqueda individual: " + duracionBusquedaNs + " nanosegundos. (¡Muy rápido!)");

        System.out.println("\n-------------------------------------------------");
        System.out.println("Prueba finalizada.");
    }
}
