// Hecho por: Yeiner Ibarguen, Jose Luis Parra
// ISIS 2112 - Diseño de Algoritmos | Proyecto Parte 3

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class ProblemaP3 {

    // Selecciona repetidamente el estudiante que cubre más estudiantes aún pendientes.
    private static List<Integer> resolverCaso(int n, List<List<Integer>> grafo) {
        List<Integer> respuesta = new ArrayList<>();
        boolean[] cubiertos = new boolean[n];
        int[] ganancias = new int[n];
        int faltantes = n;
        // Cola de prioridad para seleccionar el nodo con mayor ganancia actual.
        PriorityQueue<Entrada> cola = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            // Inicialmente, un estudiante cubre su propio nodo y sus vecinos.
            ganancias[i] = grafo.get(i).size() + 1;
            cola.add(new Entrada(i, ganancias[i]));
        }

        while (faltantes > 0) {
            //Se selecciona el nodo con mayor ganancia actual.
            Entrada entrada = cola.poll();
            int nodo = entrada.nodo;

            // Solo se usa la entrada si su ganancia sigue siendo la actual del nodo.
            // Si no, es una entrada vieja y se descarta.
            if (entrada.ganancia == ganancias[nodo]) {
                respuesta.add(nodo);
                // ArrayList para almacenar los nodos que se acaban de cubrir en esta iteración.
                List<Integer> nuevosCubiertos = new ArrayList<>();
                // El nodo elegido puede estar ya cubierto; solo cuenta como nuevo si no lo estaba.
                if (!cubiertos[nodo]) {
                    cubiertos[nodo] = true;
                    nuevosCubiertos.add(nodo);
                    faltantes--;
                }

                for (int vecino : grafo.get(nodo)) {
                    if (!cubiertos[vecino]) {
                        cubiertos[vecino] = true;
                        nuevosCubiertos.add(vecino);
                        faltantes--;
                    }
                }

                // Cada nuevo nodo cubierto deja de aportar ganancia a sí mismo y a sus vecinos.
                // Se insertan nuevas entradas para que la cola refleje esas ganancias.
                for (int cubierto : nuevosCubiertos) {
                    ganancias[cubierto]--;
                    cola.add(new Entrada(cubierto, ganancias[cubierto]));
                    for (int candidato : grafo.get(cubierto)) {
                        ganancias[candidato]--;
                        cola.add(new Entrada(candidato, ganancias[candidato]));
                    }
                }
            }
        }

        Collections.sort(respuesta);
        return respuesta;
    }

    private static class Entrada implements Comparable<Entrada> {
        // Representa una fotografía de la ganancia de un nodo en un momento dado.
        private final int nodo;
        private final int ganancia;

        private Entrada(int nodo, int ganancia) {
            this.nodo = nodo;
            this.ganancia = ganancia;
        }

        // Sale primero la entrada con mayor ganancia; si empatan, la de menor código.
        public int compareTo(Entrada otra) {
            if (this.ganancia != otra.ganancia) {
                return Integer.compare(otra.ganancia, this.ganancia);
            }
            return Integer.compare(this.nodo, otra.nodo);
        }
    }

    // Método principal para manejar la entrada y salida
    // En general, se construye un grafo no dirigido a partir de la entrada y se llama a resolverCaso para obtener la solución
    public static void main(String[] args) throws IOException {
        FastScanner sc = new FastScanner();
        String firstToken = sc.next();
        if (firstToken == null) return;

        int numCasos = Integer.parseInt(firstToken);
        StringBuilder out = new StringBuilder();

        for (int c = 0; c < numCasos; c++) {
            int n = sc.nextInt();
            int m = sc.nextInt();

            List<List<Integer>> adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }

            for (int i = 0; i < m; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                adj.get(u).add(v);
                adj.get(v).add(u);
            }

            List<Integer> seleccionados = resolverCaso(n, adj);

            // Formatear salida: códigos separados por exactamente un espacio y terminar con '*'
            for (int i = 0; i < seleccionados.size(); i++) {
                if (i > 0) out.append(' ');
                out.append(seleccionados.get(i));
            }
            out.append('\n');
        }

        System.out.print(out.toString());
    }

    // Lector rápido por tokens para manejar hasta m = 10^6 sin cuellos de botella
    private static class FastScanner {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");

        String next() throws IOException {
            while (!st.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null) return null;
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}