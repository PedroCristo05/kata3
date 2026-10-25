# Kata 3: Transformación y Serialización de Datos a JSON

El objetivo consiste en procesar un conjunto de datos tabulares externo (`vgsales.csv`), transformar los registros en objetos de dominio inmutables y serializarlos hacia un documento JSON estructurado con metadatos utilizando la biblioteca **Google Gson**, desacoplando los destinos de salida mediante flujos abstractos (`OutputStream`).

---

## 1. Demostración en Vídeo

* **Enlace al vídeo:** [kata3_pcpc](https://youtu.be/OPliYRn2gLs)

---

## 2. Decisiones de Diseño y Arquitectura

La solución sigue una arquitectura basada en **Puertos y Adaptadores**, separando la lectura, el parseo, el modelo de datos y la serialización.

```text
software.ulpgc.katas/
├── model/
│   ├── Platform.java           -> Enumerado defensivo para tipar plataformas
│   ├── Game.java               -> Record inmutable que modela la entidad de dominio
│   └── GameReport.java         -> Record contenedor de salida con Metadata anidada
├── io/
│   ├── LoadResult.java         -> Record que empaqueta datos válidos y recuento de descartes
│   ├── GameReader.java         -> Puerto de entrada para fuentes de datos
│   ├── CsvGameParser.java      -> Transformador puro (CSV -> Objetos de dominio)
│   ├── FileGameReader.java     -> Adaptador de entrada que gestiona el stream de fichero
│   ├── GameSerializer.java     -> Puerto de salida abstracto basado en OutputStream
│   └── GsonGameSerializer.java -> Adaptador de serialización JSON con Google Gson
└── Main.java                   -> Orquestador del flujo y pruebas de robustez
```

### Separación de Responsabilidades

* **Transformación pura vs. Entrada física:** La clase `CsvGameParser` centraliza la validación, limpieza de cadenas y conversión de tipos (`String` a `Platform`, `int`, `double`), retornando `null` ante anomalías sin lanzar excepciones. `FileGameReader` se limita a gestionar el `InputStream` y coordinar el conteo de descartes.
* **Modelo inmutable con `records`:** Se emplean `Game`, `LoadResult` y `GameReport` para eliminar código repetitivo, garantizar inmutabilidad y ofrecer una semántica clara de datos.
* **Estructura jerárquica de salida:** `GameReport` incluye un `record` anidado `Metadata` (`generatedAt`, `totalRecords`, `discardedRecords`, `totalGlobalSales`) para generar un documento JSON enriquecido y estructurado, evitando arrays planos anónimos.

---

## 3. Justificación Técnica de Gson

Se ha incorporado **Google Gson** frente a la concatenación manual de cadenas por los siguientes motivos:

1. **Robustez y escape de caracteres:** Gestiona de forma automática comillas, barras invertidas y caracteres de control presentes en los títulos de videojuegos, evitando generar JSONs malformados.
2. **Serialización jerárquica:** Mapea directamente grafos de objetos y colecciones a pares clave-valor y arrays JSON mediante reflexión.
3. **Formateo legible (*Pretty Printing*):** Se configura `GsonBuilder.setPrettyPrinting()` para producir sangrías claras aptas para inspección visual.
4. **Manejo de fechas modernas:** Se implementa un `JsonSerializer<Instant>` para estandarizar la estampa temporal (`generatedAt`) en formato ISO-8601 legible (`"2026-10-24T11:33:05Z"`).

---

## 4. Desacoplamiento de Salida (`OutputStream`)

La interfaz `GameSerializer` define su contrato con un `OutputStream` abstracto:

```java
public interface GameSerializer {
    void serialize(GameReport report, OutputStream outputStream);
}

```

Esto permite reutilizar exactamente el mismo método `serialize` para:

* Emitir el resultado a la consola estándar mediante `System.out`.
* Persistir el archivo físico `output.json` mediante un `FileOutputStream`.
* Escribir en memoria (`ByteArrayOutputStream`) o en sockets de red sin modificar la lógica de serialización.

---

## 5. Salida del Programa

```json
{
  "metadata": {
    "generatedAt": "2026-10-25T11:33:05.524796600Z",
    "totalRecords": 20,
    "discardedRecords": 0,
    "totalGlobalSales": 590.25
  },
  "games": [
    {
      "name": "Wii Sports",
      "platform": "Wii",
      "genre": "Sports",
      "year": 2006,
      "globalSales": 82.74
    },
    ...
  ]
}

```

---

## 6. Historial de Ramas

El repositorio documenta el ciclo de aprendizaje mediante repetición deliberada en tres ramas de desarrollo integradas finalmente en `master`:

* **`develop1`:** Primera iteración del diseño.
* **`develop2`:** Segunda repetición.
* **`develop3`:** Tercera repetición limpia utilizada para la grabación del vídeo explicativo.
* **`master`:** Rama principal estable que consolida la versión definitiva y la documentación.
