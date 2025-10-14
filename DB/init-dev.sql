-- Crear usuario de aplicación para DEV
CREATE USER gestion_gastos_user_dev WITH PASSWORD 'dev123456';

-- Darle permisos sobre la base de desarrollo
GRANT ALL PRIVILEGES ON DATABASE gestion_gastos_dev TO gestion_gastos_user_dev;

-- Asegurar permisos sobre schema público
\connect gestion_gastos_dev
GRANT ALL PRIVILEGES ON SCHEMA public TO gestion_gastos_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gestion_gastos_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gestion_gastos_user_dev;

-- Insertar datos de prueba para desarrollo
-- Los datos se insertarán automáticamente cuando las tablas se creen mediante JPA
