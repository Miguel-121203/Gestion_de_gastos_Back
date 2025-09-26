-- Crear usuario de aplicación
CREATE USER expense_user WITH PASSWORD '123456';

-- Darle permisos sobre la base
GRANT ALL PRIVILEGES ON DATABASE expense_db TO expense_user;

-- Asegurar permisos sobre schema público
\connect expense_db
GRANT ALL PRIVILEGES ON SCHEMA public TO expense_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO expense_user;
