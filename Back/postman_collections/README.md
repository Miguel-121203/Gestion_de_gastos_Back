# 📮 Colecciones Postman - Sistema de Gestión de Gastos

Este directorio contiene 4 colecciones de Postman para probar todos los microservicios del sistema.

## 📁 Archivos Incluidos

1. **ms_user_collection.json** - Autenticación y Gestión de Usuarios (Port 8110)
2. **ms_categories_collection.json** - Gestión de Categorías (Port 8090)
3. **ms_expense_collection.json** - Gestión de Gastos (Port 8080)
4. **ms_income_collection.json** - Gestión de Ingresos (Port 8100)

---

## 🚀 Cómo Importar en Postman

### Paso 1: Importar las Colecciones

1. Abre Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastra los 4 archivos JSON o selecciónalos usando **Upload Files**
4. Click en **Import**

### Paso 2: Crear el Environment

1. Click en **Environments** en el panel izquierdo
2. Click en el botón **+** para crear un nuevo environment
3. Nombra el environment: `Gestion Gastos Local`
4. Agrega las siguientes variables:

| Variable Name | Initial Value | Current Value |
|---------------|---------------|---------------|
| ms_user_url | http://localhost:8110 | http://localhost:8110 |
| ms_categories_url | http://localhost:8090 | http://localhost:8090 |
| ms_expense_url | http://localhost:8080 | http://localhost:8080 |
| ms_income_url | http://localhost:8100 | http://localhost:8100 |
| token | | |
| userId | | |
| token2 | | |
| userId2 | | |
| expenseCategoryId1 | | |
| expenseCategoryId2 | | |
| expenseCategoryId3 | | |
| incomeCategoryId1 | | |
| incomeCategoryId2 | | |
| expenseId1 | | |
| expenseId2 | | |
| expenseId3 | | |
| incomeId1 | | |
| incomeId2 | | |
| incomeId3 | | |

5. Click en **Save**
6. Selecciona el environment en el dropdown de la esquina superior derecha

---

## 🎯 Orden de Ejecución Recomendado

### ✅ Antes de Empezar: Iniciar Microservicios

Asegúrate de que todos los microservicios estén corriendo:

```bash
# Terminal 1 - ms_user
cd ms_user
./mvnw spring-boot:run

# Terminal 2 - ms_categories
cd ms_categories
./mvnw spring-boot:run

# Terminal 3 - ms_expense
cd ms_expense/ms_expense
./mvnw spring-boot:run

# Terminal 4 - ms_income
cd ms_income
./mvnw spring-boot:run
```

### 📝 Configurar Variable de Entorno JWT

**IMPORTANTE:** Los 4 microservicios deben usar el mismo JWT secret:

```bash
export JWT_SECRET="mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890"
```

Agrega esta línea a tu `.bash_profile` o `.zshrc` para hacerlo permanente.

---

## 🧪 Secuencia de Pruebas

### 1️⃣ MS_USER (Colección: MS_USER - Gestión de Usuarios)

**Orden de ejecución:**

1. **Register User** ✅
   - Registra el primer usuario (usuario@test.com)
   - ⚡ Auto-guarda: `token` y `userId` en el environment

2. **Login** ✅
   - Autentica al usuario
   - ⚡ Auto-guarda: `token` y `userId` en el environment

3. **Validate Token** ✅
   - Verifica que el token es válido

4. **Register User 2** ✅ (Opcional)
   - Registra un segundo usuario para probar aislamiento
   - ⚡ Auto-guarda: `token2` y `userId2` en el environment

---

### 2️⃣ MS_CATEGORIES (Colección: MS_CATEGORIES - Gestión de Categorías)

**Orden de ejecución:**

1. **Create Expense Category - Alimentación** ✅
   - ⚡ Auto-guarda: `expenseCategoryId1`

2. **Create Expense Category - Transporte** ✅
   - ⚡ Auto-guarda: `expenseCategoryId2`

3. **Create Expense Category - Entretenimiento** ✅
   - ⚡ Auto-guarda: `expenseCategoryId3`

4. **Create Income Category - Salario** ✅
   - ⚡ Auto-guarda: `incomeCategoryId1`

5. **Create Income Category - Freelance** ✅
   - ⚡ Auto-guarda: `incomeCategoryId2`

6. **Get All Categories** ✅
   - Verifica que se crearon todas las categorías

7. **Get Categories by Type - EXPENSE** ✅
8. **Get Categories by Type - INCOME** ✅
9. **Get Category by ID** ✅
10. **Update Category** ✅
11. **Delete Category (Soft Delete)** ✅

---

### 3️⃣ MS_EXPENSE (Colección: MS_EXPENSE - Gestión de Gastos)

**⚠️ REQUIERE:** Token JWT del login (se auto-incluye si seguiste el orden)

**Orden de ejecución:**

1. **Create Expense - Almuerzo** ✅
   - ⚡ Auto-guarda: `expenseId1`
   - Verifica que `userId` en la respuesta coincide con el usuario autenticado

2. **Create Expense - Transporte** ✅
   - ⚡ Auto-guarda: `expenseId2`

3. **Create Expense - Supermercado** ✅
   - ⚡ Auto-guarda: `expenseId3`

4. **Get All User Expenses** ✅
   - Verifica que solo se devuelven los gastos del usuario autenticado

5. **Update Expense** ✅
   - Actualiza el gasto del almuerzo

6. **Get Expenses by Date Range** ✅
   - Filtra por fechas

7. **Get Expenses by Category** ✅
   - Filtra por categoría de alimentación

8. **Get Expenses by Amount Range** ✅
   - Filtra por rango de montos

9. **Delete Expense** ✅
   - Elimina lógicamente un gasto

10. **Try to Update Another User's Expense (FORBIDDEN)** 🔒
    - **Prueba de seguridad:** Usa `token2` para intentar modificar un gasto del primer usuario
    - **Resultado esperado:** 403 FORBIDDEN

---

### 4️⃣ MS_INCOME (Colección: MS_INCOME - Gestión de Ingresos)

**⚠️ REQUIERE:** Token JWT del login (se auto-incluye si seguiste el orden)

**Orden de ejecución:**

1. **Create Income - Salario** ✅
   - ⚡ Auto-guarda: `incomeId1`
   - Verifica que `userId` en la respuesta coincide con el usuario autenticado

2. **Create Income - Freelance** ✅
   - ⚡ Auto-guarda: `incomeId2`

3. **Create Income - Bono** ✅
   - ⚡ Auto-guarda: `incomeId3`

4. **Get All User Incomes** ✅
   - Verifica que solo se devuelven los ingresos del usuario autenticado

5. **Update Income** ✅
   - Actualiza el salario

6. **Get Incomes by Date Range** ✅
   - Filtra por fechas

7. **Get Incomes by Category** ✅
   - Filtra por categoría de salario

8. **Get Incomes by Amount Range** ✅
   - Filtra por rango de montos

9. **Delete Income** ✅
   - Elimina lógicamente un ingreso

10. **Try to Delete Another User's Income (FORBIDDEN)** 🔒
    - **Prueba de seguridad:** Usa `token2` para intentar eliminar un ingreso del primer usuario
    - **Resultado esperado:** 403 FORBIDDEN

---

## 🔐 Pruebas de Seguridad Incluidas

Las colecciones incluyen pruebas de seguridad para verificar el aislamiento de datos entre usuarios:

1. **MS_EXPENSE:**
   - `Try to Update Another User's Expense (FORBIDDEN)`
   - Usa el token del segundo usuario para intentar modificar gastos del primero
   - ✅ Debe devolver 403 FORBIDDEN

2. **MS_INCOME:**
   - `Try to Delete Another User's Income (FORBIDDEN)`
   - Usa el token del segundo usuario para intentar eliminar ingresos del primero
   - ✅ Debe devolver 403 FORBIDDEN

---

## 📊 Scripts Automáticos

Las colecciones incluyen scripts que automatizan:

### 🎯 Auto-guardar Variables
- `token` y `userId` después del registro/login
- IDs de categorías después de crearlas
- IDs de gastos e ingresos después de crearlos

### ✅ Validaciones Automáticas
- Verificación de códigos HTTP exitosos
- Verificación de que userId en respuestas coincide con el usuario autenticado
- Validación de tokens generados

---

## 🐛 Solución de Problemas

### Error: "Could not get any response"
- ✅ Verifica que el microservicio esté corriendo
- ✅ Verifica el puerto correcto

### Error: 401 Unauthorized
- ✅ Verifica que ejecutaste el **Login** primero
- ✅ Verifica que el environment tiene guardada la variable `token`
- ✅ Verifica que JWT_SECRET es el mismo en todos los microservicios

### Error: 403 Forbidden en PUT/DELETE
- ✅ Esto es correcto si estás intentando modificar datos de otro usuario
- ✅ Si es tu propio dato, verifica que el token es correcto

### Error: 500 Internal Server Error
- ✅ Verifica los logs del microservicio en la consola
- ✅ Verifica que la base de datos PostgreSQL esté corriendo
- ✅ Verifica las credenciales de base de datos en `application-dev.properties`

---

## 📈 Estructura de las Respuestas

### ExpenseResponse
```json
{
  "expenseId": 1,
  "amount": 45.50,
  "expenseCategoryId": 1,
  "category": {
    "name": "Alimentación",
    "description": "Gastos en comida y restaurantes",
    "type": "EXPENSE"
  },
  "expenseDate": "2025-11-10T00:00:00",
  "description": "Almuerzo en restaurante",
  "userId": 1,
  "active": true,
  "createdAt": "2025-11-10T14:00:00",
  "updatedAt": "2025-11-10T14:00:00"
}
```

### IncomeResponse
```json
{
  "incomeId": 1,
  "amount": 3000.00,
  "incomeCategoryId": 2,
  "category": {
    "name": "Salario",
    "description": "Ingreso mensual por trabajo",
    "type": "INCOME"
  },
  "incomeDate": "2025-11-01T00:00:00",
  "description": "Salario mensual noviembre",
  "userId": 1,
  "active": true,
  "createdAt": "2025-11-10T14:00:00",
  "updatedAt": "2025-11-10T14:00:00"
}
```

---

## 🎉 ¡Listo para Probar!

Ahora tienes todo configurado para probar el sistema completo. Sigue el orden recomendado y observa cómo las variables se guardan automáticamente, facilitando las pruebas.

**Happy Testing! 🚀**
