// Autor(es): Yeiner Ibarguen, Jose Luis Parra
// ISIS 2112 - Diseño de Algoritmos | Proyecto Parte 3

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class ProblemaP3 {

    // Primera solución
    private static List<Integer> resolverCaso(int n, List<List<Integer>> grafo) {
        List<Integer> respuesta = new ArrayList<>();
        boolean [] visitados = new boolean[n];
        int [] ganancias = new int[n];
        int faltantes = n;
        PriorityQueue<Integer> cola = new PriorityQueue<>((a, b) -> Integer.compare(ganancias[b], ganancias[a])); // Cola de prioridad basada en las ganancias

        for (int i = 0; i < n; i++) {
            ganancias[i] = grafo.get(i).size() + 1; // Inicializamos las ganancias con el número de vecinos del nodo + el mismo
            cola.add(i); // Agregamos todos los nodos a la cola de prioridad, se prioriza el nodo con mayor ganancia
        }

    
        while (faltantes > 0 && !cola.isEmpty()){
            
            int nodo = cola.poll(); // Obtenemos el nodo con la mayor ganancia
            if (ganancias[nodo] == 0) continue; // Si la ganancia es 0 o negativa, lo ignoramos

            // Marcamos el nodo como visitado y lo agregamos a la respuesta
            visitados[nodo] = true;
            respuesta.add(nodo);
            // Disminuimos el contador de nodos faltantes
            faltantes--;

            // Actualizamos las ganancias de los vecinos del nodo seleccionado
            for (int vecino : grafo.get(nodo)) {
                if (!visitados[vecino]) {
                    visitados[vecino] = true; // Marcamos el vecino como visitado
                    faltantes--; // Disminuimos el contador de nodos faltantes
                    ganancias[vecino]--; // Reducimos la ganancia del vecino
                    cola.add(vecino); // Reinsertamos el vecino en la cola para actualizar su posición según la nueva ganancia
            }
            }
        }



        return respuesta;
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

            // Formatear salida: códigos separados por exactamente un espacio
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
