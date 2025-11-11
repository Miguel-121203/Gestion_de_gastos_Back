-- Crear usuario de aplicación para PRODUCCIÓN
CREATE USER gestion_gastos_user_pdn WITH PASSWORD '${POSTGRES_PDN_USER_PASSWORD:-pdn123456}';

-- Darle permisos sobre la base de producción
GRANT ALL PRIVILEGES ON DATABASE gestion_gastos_pdn TO gestion_gastos_user_pdn;

-- Asegurar permisos sobre schema público
\connect gestion_gastos_pdn
GRANT ALL PRIVILEGES ON SCHEMA public TO gestion_gastos_user_pdn;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gestion_gastos_user_pdn;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gestion_gastos_user_pdn;

-- Configuraciones adicionales para producción
-- Los índices y restricciones se crearán después de que JPA genere las tablas
