package main.java;

import java.util.Scanner;
import java.util.Random;

public class TutorAlgebra {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static int preguntasCorrectas = 0;
    private static int preguntasIntentadas = 0;

    public static void main(String[] args) {
        mostrarBienvenida();

        boolean continuar = true;
        while (continuar) {
            int modo = seleccionarModo();

            if (modo == 4) {
                continuar = false;
                mostrarPuntuacionFinal();
            } else {
                ejecutarModo(modo);
            }
        }

        scanner.close();
    }

    /**
     * Muestra el mensaje de bienvenida y explicación del programa
     */
    public static void mostrarBienvenida() {
        System.out.println("=============================================");
        System.out.println("    TUTOR DE ÁLGEBRA - ECUACIONES LINEALES   ");
        System.out.println("=============================================");
        System.out.println("¡Bienvenido! Este programa te ayudará a practicar");
        System.out.println("ecuaciones lineales de la forma: y = mx + b");
        System.out.println();
    }

    /**
     * Permite al usuario seleccionar el modo de práctica
     * @return el número del modo seleccionado
     */
    public static int seleccionarModo() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("Selecciona un modo de práctica:");
        System.out.println("1. Resolver para y (dados m, x, b)");
        System.out.println("2. Resolver para m (dados y, x, b)");
        System.out.println("3. Resolver para b (dados y, m, x)");
        System.out.println("4. Salir del programa");
        System.out.print("Tu elección (1-4): ");

        int modo = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            if (scanner.hasNextInt()) {
                modo = scanner.nextInt();
                if (modo >= 1 && modo <= 4) {
                    entradaValida = true;
                } else {
                    System.out.print("Por favor, ingresa un número entre 1 y 4: ");
                }
            } else {
                System.out.print("Entrada inválida. Ingresa un número entre 1 y 4: ");
                scanner.next(); // Limpiar entrada inválida
            }
        }

        return modo;
    }

    /**
     * Ejecuta el modo seleccionado con 3 preguntas
     * @param modo el modo de práctica seleccionado
     */
    public static void ejecutarModo(int modo) {
        String tipoEcuacion = obtenerNombreModo(modo);
        System.out.println("\n--- MODO: " + tipoEcuacion + " ---");
        System.out.println("Responderás 3 preguntas en este modo.\n");

        int preguntasCorrectasModo = 0;

        for (int i = 1; i <= 3; i++) {
            System.out.println("Pregunta " + i + " de 3:");
            boolean respuestaCorrecta = hacerPregunta(modo);

            if (respuestaCorrecta) {
                preguntasCorrectasModo++;
                preguntasCorrectas++;
            }
            preguntasIntentadas++;
            System.out.println();
        }

        // Mostrar puntuación del modo
        mostrarPuntuacionModo(preguntasCorrectasModo, tipoEcuacion);
    }

    /**
     * Genera y hace una pregunta según el modo seleccionado
     * @param modo el tipo de pregunta a generar
     * @return true si la respuesta fue correcta, false en caso contrario
     */
    public static boolean hacerPregunta(int modo) {
        // Generar valores aleatorios entre -100 y 100
        int m = generarNumeroAleatorio();
        int x = generarNumeroAleatorio();
        int b = generarNumeroAleatorio();
        int y = m * x + b; // Calcular y para tener valores consistentes

        int respuestaCorrecta = 0;
        String pregunta = "";

        // Determinar la pregunta y respuesta correcta según el modo
        switch (modo) {
            case 1: // Resolver para y
                respuestaCorrecta = y;
                pregunta = String.format("Si m = %d, x = %d, b = %d, ¿cuál es el valor de y?", m, x, b);
                break;
            case 2: // Resolver para m
                respuestaCorrecta = m;
                y = m * x + b; // Asegurar consistencia
                pregunta = String.format("Si y = %d, x = %d, b = %d, ¿cuál es el valor de m?", y, x, b);
                break;
            case 3: // Resolver para b
                respuestaCorrecta = b;
                y = m * x + b; // Asegurar consistencia
                pregunta = String.format("Si y = %d, m = %d, x = %d, ¿cuál es el valor de b?", y, m, x);
                break;
        }

        System.out.println("Ecuación: y = mx + b");
        System.out.println(pregunta);

        return verificarRespuesta(respuestaCorrecta, modo, m, x, b, y);
    }

    /**
     * Verifica la respuesta del usuario y maneja intentos múltiples
     * @param respuestaCorrecta la respuesta correcta
     * @param modo el modo actual para generar pistas
     * @param m, x, b, y los valores de la ecuación para las pistas
     * @return true si respondió correctamente, false en caso contrario
     */
    public static boolean verificarRespuesta(int respuestaCorrecta, int modo, int m, int x, int b, int y) {
        int intentos = 0;
        int maxIntentos = 3;

        while (intentos < maxIntentos) {
            System.out.print("Tu respuesta: ");

            if (scanner.hasNextInt()) {
                int respuestaUsuario = scanner.nextInt();

                if (respuestaUsuario == respuestaCorrecta) {
                    System.out.println("¡Correcto! 🎉");
                    return true;
                } else {
                    intentos++;
                    if (intentos < maxIntentos) {
                        System.out.println("Incorrecto. Intento " + intentos + " de " + maxIntentos);
                        System.out.println("Inténtalo de nuevo.");
                    }
                }
            } else {
                System.out.println("Por favor, ingresa un número entero.");
                scanner.next(); // Limpiar entrada inválida
                intentos++;
            }
        }

        // Después de 3 intentos incorrectos, mostrar pista
        System.out.println("\n--- PISTA ---");
        mostrarPista(modo, m, x, b, y);
        System.out.println("La respuesta correcta era: " + respuestaCorrecta);

        return false;
    }

    /**
     * Muestra una pista específica según el modo
     * @param modo el tipo de problema
     * @param m, x, b, y los valores de la ecuación
     */
    public static void mostrarPista(int modo, int m, int x, int b, int y) {
        switch (modo) {
            case 1: // Resolver para y
                System.out.println("Para encontrar y, sustituye los valores en la ecuación:");
                System.out.println("y = mx + b");
                System.out.printf("y = (%d)(%d) + (%d)\n", m, x, b);
                System.out.printf("y = %d + %d = %d\n", m*x, b, m*x+b);
                break;
            case 2: // Resolver para m
                System.out.println("Para encontrar m, despeja m de la ecuación:");
                System.out.println("y = mx + b");
                System.out.println("y - b = mx");
                System.out.println("m = (y - b) / x");
                System.out.printf("m = (%d - %d) / %d = %d / %d = %d\n",
                        y, b, x, y-b, x, (y-b)/x);
                break;
            case 3: // Resolver para b
                System.out.println("Para encontrar b, despeja b de la ecuación:");
                System.out.println("y = mx + b");
                System.out.println("b = y - mx");
                System.out.printf("b = %d - (%d)(%d) = %d - %d = %d\n",
                        y, m, x, y, m*x, y-m*x);
                break;
        }
    }

    /**
     * Genera un número aleatorio entre -100 y 100 (excluyendo 0 para x y m)
     * @return número entero aleatorio
     */
    public static int generarNumeroAleatorio() {
        int numero;
        do {
            numero = random.nextInt(201) - 100; // Rango de -100 a 100
        } while (numero == 0); // Evitar división por cero

        return numero;
    }

    /**
     * Obtiene el nombre descriptivo del modo
     * @param modo el número del modo
     * @return descripción del modo
     */
    public static String obtenerNombreModo(int modo) {
        switch (modo) {
            case 1: return "RESOLVER PARA Y";
            case 2: return "RESOLVER PARA M";
            case 3: return "RESOLVER PARA B";
            default: return "DESCONOCIDO";
        }
    }

    /**
     * Muestra la puntuación del modo actual
     * @param correctas preguntas correctas en este modo
     * @param tipoModo nombre del modo
     */
    public static void mostrarPuntuacionModo(int correctas, String tipoModo) {
        System.out.println("--- RESULTADO DEL MODO: " + tipoModo + " ---");
        System.out.println("Preguntas correctas: " + correctas + "/3");
        double porcentaje = (correctas / 3.0) * 100;
        System.out.printf("Porcentaje de acierto: %.1f%%\n", porcentaje);

        if (correctas == 3) {
            System.out.println("¡Excelente trabajo! 🌟");
        } else if (correctas == 2) {
            System.out.println("¡Buen trabajo! 👍");
        } else if (correctas == 1) {
            System.out.println("Sigue practicando 📚");
        } else {
            System.out.println("¡No te rindas! La práctica hace al maestro 💪");
        }
    }

    /**
     * Muestra la puntuación final del programa
     */
    public static void mostrarPuntuacionFinal() {
        System.out.println("\n===========================================");
        System.out.println("           PUNTUACIÓN FINAL                  ");
        System.out.println("=============================================");

        if (preguntasIntentadas > 0) {
            double porcentajeGeneral = (double) preguntasCorrectas / preguntasIntentadas * 100;
            System.out.println("Preguntas respondidas correctamente: " + preguntasCorrectas);
            System.out.println("Total de preguntas intentadas: " + preguntasIntentadas);
            System.out.printf("Porcentaje general de acierto: %.1f%%\n", porcentajeGeneral);

            if (porcentajeGeneral >= 90) {
                System.out.println("¡Eres un experto en álgebra! 🏆");
            } else if (porcentajeGeneral >= 70) {
                System.out.println("¡Muy buen rendimiento! 🎉");
            } else if (porcentajeGeneral >= 50) {
                System.out.println("¡Buen esfuerzo! Sigue practicando 📈");
            } else {
                System.out.println("¡La práctica te llevará al éxito! 🚀");
            }
        } else {
            System.out.println("No se respondieron preguntas en esta sesión.");
        }

        System.out.println("\n¡Gracias por usar el Tutor de Álgebra!");
        System.out.println("¡Sigue practicando para mejorar tus habilidades!");
        System.out.println("================================================");
    }
}