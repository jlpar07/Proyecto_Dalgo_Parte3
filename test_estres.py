import random, subprocess, time

n = 10000
m = 1000000

# 1. Generar grafo aleatorio grande
edges = set()
while len(edges) < m:
    u = random.randint(0, n - 1)
    v = random.randint(0, n - 1)
    if u != v:
        edges.add((min(u, v), max(u, v)))

with open("estres.txt", "w") as f:
    f.write(f"1\n{n} {m}\n")
    f.write(" ".join(f"{u} {v}" for u, v in edges) + "\n")

# 2. Ejecutar ProblemaP3.java y medir tiempo
inicio = time.time()
with open("estres.txt", "r") as entrada:
    res = subprocess.run(
        ["java", "ProblemaP3"],
        stdin=entrada,
        capture_output=True,
        text=True,
        check=True
    )
fin = time.time()

# 3. Verificar que todos los nodos queden cubiertos
seleccionados = list(map(int, res.stdout.strip().split()))
adj = [set() for _ in range(n)]
for u, v in edges:
    adj[u].add(v)
    adj[v].add(u)

cubiertos = set(seleccionados)
for s in seleccionados:
    cubiertos.update(adj[s])

print(f"Tiempo de ejecución: {fin - inicio:.3f} s")
print(f"Estudiantes seleccionados: {len(seleccionados)} de {n}")
print(f"¿Cobertura 100% válida?: {len(cubiertos) == n}")