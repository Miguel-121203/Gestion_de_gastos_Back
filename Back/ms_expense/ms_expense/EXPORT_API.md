# API de Exportación de Gastos

Endpoint para exportar gastos de un usuario por mes en formatos PDF o Excel.

## Endpoint

### Exportar Gastos Mensuales

**Endpoint:** `GET /api/v1/expenses/export/user/{userId}`

**Descripción:** Exporta todos los gastos de un usuario para un mes específico en formato PDF o Excel.

**Parámetros de ruta:**
- `userId` (requerido): ID del usuario

**Parámetros de consulta:**
- `year` (requerido): Año (ejemplo: 2024)
- `month` (requerido): Mes del 1 al 12, donde:
  - 1 = enero
  - 2 = febrero
  - 3 = marzo
  - 4 = abril
  - 5 = mayo
  - 6 = junio
  - 7 = julio
  - 8 = agosto
  - 9 = septiembre
  - 10 = octubre
  - 11 = noviembre
  - 12 = diciembre
- `format` (opcional): Formato de exportación. Valores: `pdf` (default) o `excel`/`xlsx`

---

## Ejemplos de Uso

### Exportar en PDF

```bash
# Exportar gastos de enero 2024 del usuario 1 en PDF
curl -X GET "http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=1&format=pdf" -o gastos_enero_2024.pdf

# Exportar gastos de diciembre 2024 del usuario 5 en PDF
curl -X GET "http://localhost:8080/api/v1/expenses/export/user/5?year=2024&month=12&format=pdf" -o gastos_diciembre_2024.pdf
```

### Exportar en Excel

```bash
# Exportar gastos de marzo 2024 del usuario 1 en Excel
curl -X GET "http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=3&format=excel" -o gastos_marzo_2024.xlsx

# Exportar gastos de junio 2024 del usuario 3 en Excel
curl -X GET "http://localhost:8080/api/v1/expenses/export/user/3?year=2024&month=6&format=xlsx" -o gastos_junio_2024.xlsx
```

### Exportar sin especificar formato (usa PDF por defecto)

```bash
# Exportar gastos de mayo 2024 del usuario 2 (PDF por defecto)
curl -X GET "http://localhost:8080/api/v1/expenses/export/user/2?year=2024&month=5" -o gastos_mayo_2024.pdf
```

---

## Respuestas

### Éxito (200 OK)

El archivo se descarga automáticamente con el nombre generado que incluye:
- ID del usuario
- Nombre del mes en español
- Año
- Timestamp de generación
- Extensión del archivo (.pdf o .xlsx)

**Ejemplo de nombre de archivo:**
- `gastos_usuario_1_enero_2024_20241010_180530.pdf`
- `gastos_usuario_5_diciembre_2024_20241010_181245.xlsx`

**Headers de respuesta:**
```
Content-Type: application/pdf   (o application/vnd.openxmlformats-officedocument.spreadsheetml.sheet para Excel)
Content-Disposition: attachment; filename="gastos_usuario_1_enero_2024_20241010_180530.pdf"
Cache-Control: must-revalidate, post-check=0, pre-check=0
```

### Sin Contenido (204 No Content)

Se retorna cuando no hay gastos para el usuario en el mes especificado.

### Solicitud Incorrecta (400 Bad Request)

Se retorna cuando:
- El mes es menor a 1 o mayor a 12
- Faltan parámetros requeridos

### Error Interno (500 Internal Server Error)

Se retorna cuando ocurre un error durante la generación del archivo.

---

## Formatos de Exportación

### PDF
- **Content-Type:** `application/pdf`
- **Características:**
  - Tabla con encabezados estilizados (fondo gris)
  - Título del reporte: "Reporte de Gastos"
  - Información del reporte (total de gastos y fecha de generación)
  - Columnas: ID, Monto, Categoría, Fecha, Descripción, Usuario, Estado
  - Fila de total con fondo amarillo mostrando la suma total
  - Formato de montos: `$XXX.XX`
  - Alineación de montos a la derecha

### Excel (XLSX)
- **Content-Type:** `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Características:**
  - Hoja de cálculo con nombre "Gastos"
  - Encabezados con formato profesional (fondo azul oscuro, texto blanco, centrado)
  - Columnas: ID, Monto, Categoría ID, Fecha de Gasto, Descripción, Usuario ID, Estado, Fecha Creación, Última Actualización
  - Formato de moneda para montos: `$#,##0.00` (alineado a la derecha)
  - Formato de fechas centrado
  - Fila de total con fondo amarillo y texto en negrita
  - Columnas auto-ajustadas al contenido

---

## Cálculo del Rango de Fechas

El endpoint calcula automáticamente el rango completo del mes:

- **Fecha inicial:** Día 1 del mes a las 00:00:00
- **Fecha final:** Último día del mes a las 23:59:59

**Ejemplos:**
- `month=1, year=2024` → Del 2024-01-01 00:00:00 al 2024-01-31 23:59:59
- `month=2, year=2024` → Del 2024-02-01 00:00:00 al 2024-02-29 23:59:59 (año bisiesto)
- `month=6, year=2024` → Del 2024-06-01 00:00:00 al 2024-06-30 23:59:59

---

## Ejemplos con Postman

### Configuración de Request

**URL:**
```
GET http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=1&format=pdf
```

**Headers:**
- Ninguno requerido (el endpoint maneja CORS)

**Params:**
| Key    | Value | Description          |
|--------|-------|----------------------|
| year   | 2024  | Año a exportar       |
| month  | 1     | Mes a exportar (1-12)|
| format | pdf   | pdf o excel          |

### Colección de Postman

```json
{
  "info": {
    "name": "Expense Export API - Monthly",
    "description": "API para exportar gastos mensuales en PDF o Excel"
  },
  "item": [
    {
      "name": "Export January PDF",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=1&format=pdf",
          "query": [
            {"key": "year", "value": "2024"},
            {"key": "month", "value": "1"},
            {"key": "format", "value": "pdf"}
          ]
        }
      }
    },
    {
      "name": "Export March Excel",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=3&format=excel",
          "query": [
            {"key": "year", "value": "2024"},
            {"key": "month", "value": "3"},
            {"key": "format", "value": "excel"}
          ]
        }
      }
    },
    {
      "name": "Export December Default (PDF)",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/v1/expenses/export/user/1?year=2024&month=12",
          "query": [
            {"key": "year", "value": "2024"},
            {"key": "month", "value": "12"}
          ]
        }
      }
    }
  ]
}
```

---

## Validaciones

El endpoint realiza las siguientes validaciones:

1. **Validación del mes:**
   - El valor de `month` debe estar entre 1 y 12
   - Si es inválido, retorna `400 Bad Request`

2. **Validación de gastos:**
   - Si no hay gastos en el mes especificado, retorna `204 No Content`

3. **Manejo de errores:**
   - Cualquier error durante la generación retorna `500 Internal Server Error`
   - Todos los errores se registran en los logs

---

## Logging

El endpoint registra la siguiente información:

- **INFO:** Solicitudes recibidas con parámetros (userId, year, month, format)
- **INFO:** Rango de fechas calculado
- **INFO:** Tipo de archivo generado y cantidad de gastos
- **INFO:** Nombre del archivo exportado y cantidad de gastos
- **WARN:** Cuando no se encuentran gastos para exportar
- **ERROR:** Mes inválido fuera del rango 1-12
- **ERROR:** Errores durante la generación del archivo

---

## Notas Técnicas

1. **CORS:** El endpoint está configurado con `@CrossOrigin(origins = "*")` para desarrollo. Ajustar en producción.

2. **Formato de fechas:** Las fechas en los reportes usan el formato `yyyy-MM-dd HH:mm:ss`.

3. **Nombres de meses:** Los archivos generados incluyen el nombre del mes en español.

4. **Totales:** Ambos formatos calculan y muestran el total de los gastos del mes.

5. **Cálculo automático:** El endpoint calcula automáticamente el primer y último día del mes, incluyendo meses de diferentes duraciones (28, 29, 30, 31 días).

---

## Dependencias

```xml
<!-- Apache POI for Excel generation -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>

<!-- iText for PDF generation -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
    <type>pom</type>
</dependency>
```

---

## Arquitectura

La funcionalidad sigue la arquitectura hexagonal del proyecto:

- **Puerto (Interface):** `ExportServicePort` en `domain.ports`
- **Servicio:** `ExportService` en `application.services`
- **Controlador REST:** `ExportController` en `infrastructure.adapters.input.rest`
- **Configuración:** Bean en `BeanConfiguration`
