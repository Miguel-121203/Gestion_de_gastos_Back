-- Crear usuario de aplicación para STAGING
CREATE USER expense_user_staging WITH PASSWORD 'staging123456';

-- Darle permisos sobre la base de staging
GRANT ALL PRIVILEGES ON DATABASE expense_db_staging TO expense_user_staging;

-- Asegurar permisos sobre schema público
\connect expense_db_staging
GRANT ALL PRIVILEGES ON SCHEMA public TO expense_user_staging;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO expense_user_staging;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO expense_user_staging;

-- Datos de prueba más realistas para staging
INSERT INTO expenses (description, amount, category, created_date) VALUES 
('Prueba integración sistema facturación', 50000, 'INTEGRACION', NOW()),
('Validación procesos contables', 75000, 'CONTABILIDAD', NOW())
ON CONFLICT DO NOTHING;