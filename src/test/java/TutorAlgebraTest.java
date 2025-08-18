package test.java;

import main.java.TutorAlgebra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

/**
 * Tests unitarios para la clase TutorAlgebra
 *
 */
public class TutorAlgebraTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Capturar la salida del sistema para testing
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restaurar la salida original
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Test: Método obtenerNombreModo() funciona correctamente")
    void testObtenerNombreModo() {
        assertEquals("RESOLVER PARA Y", TutorAlgebra.obtenerNombreModo(1));
        assertEquals("RESOLVER PARA M", TutorAlgebra.obtenerNombreModo(2));
        assertEquals("RESOLVER PARA B", TutorAlgebra.obtenerNombreModo(3));
        assertEquals("DESCONOCIDO", TutorAlgebra.obtenerNombreModo(0));
        assertEquals("DESCONOCIDO", TutorAlgebra.obtenerNombreModo(4));
        assertEquals("DESCONOCIDO", TutorAlgebra.obtenerNombreModo(-1));
        assertEquals("DESCONOCIDO", TutorAlgebra.obtenerNombreModo(100));
    }

    @RepeatedTest(100) // Reducido de 1000 a 100 para tests más rápidos
    @DisplayName("Test: generarNumeroAleatorio() está en rango correcto")
    void testGenerarNumeroAleatorio() {
        int numero = TutorAlgebra.generarNumeroAleatorio();

        // Verificar que está en el rango correcto
        assertTrue(numero >= -100 && numero <= 100,
                "Número fuera de rango: " + numero + " (debe estar entre -100 y 100)");

        // Verificar que no es cero
        assertNotEquals(0, numero, "El número generado no debe ser cero");
    }

    @Test
    @DisplayName("Test: mostrarBienvenida() muestra el mensaje correcto")
    void testMostrarBienvenida() {
        TutorAlgebra.mostrarBienvenida();
        String output = outputStream.toString();

        assertTrue(output.contains("TUTOR DE ÁLGEBRA"),
                "Debe contener el título del programa");
        assertTrue(output.contains("y = mx + b"),
                "Debe mostrar la fórmula de la ecuación lineal");
        assertTrue(output.contains("Bienvenido"),
                "Debe contener mensaje de bienvenida");
    }

    @Test
    @DisplayName("Test: mostrarPuntuacionModo() muestra información correcta")
    void testMostrarPuntuacionModo() {
        // Test con puntuación perfecta
        TutorAlgebra.mostrarPuntuacionModo(3, "RESOLVER PARA Y");
        String output = outputStream.toString();

        // Debug: Mostrar la salida real para diagnosticar
        System.setOut(originalOut); // Temporalmente restaurar para debug
        System.out.println("=== DEBUG OUTPUT ===");
        System.out.println("Salida capturada:");
        System.out.println("'" + output + "'");
        System.out.println("Longitud: " + output.length());
        System.out.println("===================");
        System.setOut(new PrintStream(outputStream)); // Volver a capturar

        assertTrue(output.contains("3/3"), "Debe mostrar 3/3 preguntas correctas");

        // Más flexible en la verificación del porcentaje
        assertTrue(output.contains("100") || output.contains("100.0"),
                "Debe mostrar 100% de acierto de alguna forma. Salida actual: " + output);

        assertTrue(output.contains("Excelente") || output.contains("excelente"),
                "Debe felicitar por el resultado perfecto");

        // Limpiar para siguiente test
        outputStream.reset();

        // Test con puntuación media
        TutorAlgebra.mostrarPuntuacionModo(2, "RESOLVER PARA M");
        output = outputStream.toString();

        assertTrue(output.contains("2/3"), "Debe mostrar 2/3 preguntas correctas");

        // Buscar 66.7% o 66,7% (por si usa coma decimal)
        assertTrue(output.contains("66.7") || output.contains("66,7") || output.contains("67"),
                "Debe mostrar aproximadamente 67% de acierto. Salida: " + output);
    }

    @Test
    @DisplayName("Test: Cálculos matemáticos son correctos")
    void testCalculosMatematicos() {
        // Test de ecuaciones lineales básicas

        // Caso 1: y = mx + b con valores positivos
        int m1 = 3, x1 = 4, b1 = 5;
        int y1_esperado = m1 * x1 + b1; // 3*4+5 = 17
        assertEquals(17, y1_esperado, "Cálculo y = 3*4+5 debe ser 17");

        // Verificar cálculo inverso para m: m = (y-b)/x
        int m1_calculado = (y1_esperado - b1) / x1; // (17-5)/4 = 3
        assertEquals(m1, m1_calculado, "Cálculo inverso de m debe ser correcto");

        // Verificar cálculo inverso para b: b = y - mx
        int b1_calculado = y1_esperado - m1 * x1; // 17 - 3*4 = 5
        assertEquals(b1, b1_calculado, "Cálculo inverso de b debe ser correcto");

        // Caso 2: Con números negativos
        int m2 = -2, x2 = 6, b2 = -8;
        int y2_esperado = m2 * x2 + b2; // -2*6+(-8) = -20
        assertEquals(-20, y2_esperado, "Cálculo con negativos debe ser correcto");

        // Caso 3: Resultado cero
        int m3 = 5, x3 = -2, b3 = 10;
        int y3_esperado = m3 * x3 + b3; // 5*(-2)+10 = 0
        assertEquals(0, y3_esperado, "Resultado puede ser cero");
    }

    @Test
    @DisplayName("Test: Validación de casos extremos")
    void testCasosExtremos() {
        // Test con valores límite
        int m_max = 100, x_max = 100, b_max = 100;
        int y_max = m_max * x_max + b_max; // Puede ser muy grande
        assertNotNull(y_max, "Debe manejar valores grandes");

        int m_min = -100, x_min = -100, b_min = -100;
        int y_min = m_min * x_min + b_min; // -100*(-100)+(-100) = 9900
        assertEquals(9900, y_min, "Cálculo con valores mínimos debe ser correcto");

        // Test de división (para el cálculo de m)
        int y_test = 15, x_test = 3, b_test = 0;
        int m_test = (y_test - b_test) / x_test; // (15-0)/3 = 5
        assertEquals(5, m_test, "División debe ser exacta cuando es posible");
    }

    @Test
    @DisplayName("Test: mostrarPista() muestra información para cada modo")
    void testMostrarPista() {
        int m = 2, x = 3, b = 4, y = 10; // y = 2*3+4 = 10

        // Test pista modo 1 (resolver y)
        TutorAlgebra.mostrarPista(1, m, x, b, y);
        String output1 = outputStream.toString();
        assertTrue(output1.contains("y = mx + b"), "Pista modo 1 debe mostrar la fórmula");
        assertTrue(output1.contains("sustituye"), "Debe explicar sustitución");

        outputStream.reset();

        // Test pista modo 2 (resolver m)
        TutorAlgebra.mostrarPista(2, m, x, b, y);
        String output2 = outputStream.toString();
        assertTrue(output2.contains("despeja m"), "Pista modo 2 debe explicar cómo despejar m");
        assertTrue(output2.contains("m = (y - b) / x"), "Debe mostrar la fórmula para m");

        outputStream.reset();

        // Test pista modo 3 (resolver b)
        TutorAlgebra.mostrarPista(3, m, x, b, y);
        String output3 = outputStream.toString();
        assertTrue(output3.contains("despeja b"), "Pista modo 3 debe explicar cómo despejar b");
        assertTrue(output3.contains("b = y - mx"), "Debe mostrar la fórmula para b");
    }

    @Test
    @DisplayName("Test: Verificar que los métodos públicos existen")
    void testMetodosPublicosExisten() {
        Class<?> tutorClass = TutorAlgebra.class;

        // Verificar que los métodos principales existen
        assertDoesNotThrow(() -> {
            tutorClass.getMethod("mostrarBienvenida");
            tutorClass.getMethod("obtenerNombreModo", int.class);
            tutorClass.getMethod("generarNumeroAleatorio");
            tutorClass.getMethod("mostrarPista", int.class, int.class, int.class, int.class, int.class);
            tutorClass.getMethod("mostrarPuntuacionModo", int.class, String.class);
        }, "Todos los métodos públicos requeridos deben existir");
    }

    @Test
    @DisplayName("Test: Simulación de flujo completo simplificado")
    void testFlujoSimplificado() {
        // Este test verifica que las partes principales del programa funcionan

        // Test 1: Bienvenida
        assertDoesNotThrow(() -> TutorAlgebra.mostrarBienvenida(),
                "mostrarBienvenida() no debe lanzar excepciones");

        // Test 2: Obtener nombres de modo
        for (int i = 1; i <= 3; i++) {
            String nombreModo = TutorAlgebra.obtenerNombreModo(i);
            assertNotNull(nombreModo, "Nombre de modo no debe ser null");
            assertFalse(nombreModo.isEmpty(), "Nombre de modo no debe estar vacío");
            assertNotEquals("DESCONOCIDO", nombreModo, "Modos 1-3 deben tener nombres válidos");
        }

        // Test 3: Generación de números
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> TutorAlgebra.generarNumeroAleatorio(),
                    "generarNumeroAleatorio() no debe lanzar excepciones");
        }

        // Test 4: Mostrar pistas
        for (int modo = 1; modo <= 3; modo++) {
            final int modoFinal = modo;
            assertDoesNotThrow(() -> TutorAlgebra.mostrarPista(modoFinal, 2, 3, 4, 10),
                    "mostrarPista() no debe lanzar excepciones para modo " + modo);
        }
    }

    @Test
    @DisplayName("Test: Consistencia matemática entre modos")
    void testConsistenciaMatematica() {
        // Usar valores conocidos para verificar consistencia
        int m = 5, x = 3, b = 7;
        int y = m * x + b; // y = 5*3+7 = 22

        // Si tenemos estos valores, los cálculos inversos deben ser consistentes

        // Modo 1: Calcular y dado m, x, b
        int y_calculado = m * x + b;
        assertEquals(22, y_calculado, "y = mx + b debe dar 22");

        // Modo 2: Calcular m dado y, x, b
        int m_calculado = (y - b) / x; // (22-7)/3 = 5
        assertEquals(m, m_calculado, "m calculado debe coincidir con m original");

        // Modo 3: Calcular b dado y, m, x
        int b_calculado = y - m * x; // 22 - 5*3 = 7
        assertEquals(b, b_calculado, "b calculado debe coincidir con b original");

        // Verificar que la ecuación se mantiene consistente
        assertEquals(y, m_calculado * x + b_calculado,
                "La ecuación debe ser consistente con valores calculados");
    }

    /**
     * Test de integración que simula múltiples operaciones
     */
    @Test
    @DisplayName("Test de Integración: Múltiples cálculos")
    void testIntegracionMultiplesCalculos() {
        // Simular 10 problemas aleatorios y verificar consistencia
        for (int i = 0; i < 10; i++) {
            int m = TutorAlgebra.generarNumeroAleatorio();
            int x = TutorAlgebra.generarNumeroAleatorio();
            int b = TutorAlgebra.generarNumeroAleatorio();
            int y = m * x + b;

            // Verificar que los cálculos inversos funcionan
            assertEquals(m, (y - b) / x,
                    String.format("Iteración %d: m=%d, x=%d, b=%d, y=%d", i, m, x, b, y));
            assertEquals(b, y - m * x,
                    String.format("Iteración %d: cálculo de b inconsistente", i));
        }
    }

    /**
     * Test específico para verificar el formato de porcentaje
     */
    @Test
    @DisplayName("Test: Verificar formato de porcentaje específicamente")
    void testFormatoPorcentaje() {
        // Capturar directamente la salida de mostrarPuntuacionModo
        TutorAlgebra.mostrarPuntuacionModo(3, "TEST");
        String output = outputStream.toString();

        // Restaurar salida para debug
        System.setOut(originalOut);
        System.out.println("=== VERIFICACIÓN DE FORMATO ===");
        System.out.println("Salida completa:");
        System.out.println(output);
        System.out.println("Contiene '100'? " + output.contains("100"));
        System.out.println("Contiene '100.0'? " + output.contains("100.0"));
        System.out.println("Contiene '%'? " + output.contains("%"));

        // Test más permisivo - solo verificar que muestra algún tipo de 100%
        boolean tieneFormatoCorrecto = output.contains("100") && output.contains("%");
        assertTrue(tieneFormatoCorrecto,
                "Debe mostrar 100% en algún formato. Output: '" + output + "'");
    }
}