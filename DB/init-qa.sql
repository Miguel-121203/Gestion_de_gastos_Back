-- Crear usuario de aplicación para QA
CREATE USER gestion_gastos_user_qa WITH PASSWORD 'qa123456';

-- Darle permisos sobre la base de QA
GRANT ALL PRIVILEGES ON DATABASE gestion_gastos_qa TO gestion_gastos_user_qa;

-- Asegurar permisos sobre schema público
\connect gestion_gastos_qa
GRANT ALL PRIVILEGES ON SCHEMA public TO gestion_gastos_user_qa;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gestion_gastos_user_qa;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gestion_gastos_user_qa;

-- Datos de prueba para QA
-- Los datos se insertarán automáticamente cuando las tablas se creen mediante JPA
