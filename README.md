Sistema de gestión de gastos con alertas y metas desarrollado en Java con Spring Boot.
📋 Descripción del Proyecto
Aplicación backend para la gestión integral de gastos personales que incluye registro de transacciones, establecimiento de metas de ahorro, límites de gasto por categorías y sistema de notificaciones por email.
🏗️ Arquitectura del Sistema
Stack Tecnológico

Java 17+ - Lenguaje de programación principal
Spring Boot 3.x - Framework principal
Spring Data JPA - Persistencia de datos
Spring Security - Autenticación y autorización
Spring Mail - Sistema de notificaciones por email
PostgreSQL - Base de datos principal
Maven - Gestión de dependencias
Docker - Containerización

📊 Épicas de Desarrollo
Épica 1: Gestión de Gastos y Categorías
Duración estimada: 3-4 semanas
Funcionalidades Principales:

✅ Registro de gastos (monto, categoría, fecha, descripción)
✅ Gestión de categorías predefinidas y personalizadas
✅ Historial de transacciones con filtros
✅ Cálculo de totales por período
✅ API RESTful para operaciones CRUD

Entregables:

Modelo de datos para gastos y categorías
Controladores REST para gestión de gastos
Servicios de negocio para cálculos y validaciones
Repositorios JPA para persistencia
Tests unitarios e integración

Épica 2: Sistema de Metas y Presupuestos
Duración estimada: 4-5 semanas
Funcionalidades Principales:

✅ Creación y gestión de metas de ahorro
✅ Establecimiento de límites de gasto por categoría
✅ Presupuesto mensual configurable
✅ Cálculo automático de progreso
✅ Indicadores visuales de cumplimiento

Entregables:

Modelo de datos para metas y presupuestos
Servicios de cálculo de progreso
APIs para gestión de metas
Scheduler para evaluaciones periódicas
Dashboard de métricas

Épica 3: Sistema de Notificaciones y Alertas
Duración estimada: 3-4 semanas
Funcionalidades Principales:

✅ Alertas por límites excedidos
✅ Notificaciones de progreso de metas
✅ Recordatorios de gastos recurrentes
✅ Resúmenes semanales y mensuales
✅ Configuración personalizable de notificaciones

Entregables:

Sistema de templates de email
Scheduler para notificaciones automáticas
Configuración de alertas personalizables
Sistema de logs de notificaciones
Panel de configuración de usuario

🗄️ Épica de Base de Datos
Fase 1: Diseño e Implementación del Esquema
Duración estimada: 2 semanas
Entregables:

Diagrama entidad-relación completo
Scripts de creación de tablas



👥 Equipo de Desarrollo

Backend Lead: Miguel Angel Beltran Bohorquez
Database Egineer: Nicolas Rivera Aroca
FullStack Engineer: Jesus David Candelo Gonzalez 
