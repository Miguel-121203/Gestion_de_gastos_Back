-- Crear usuario de aplicación para PRODUCCIÓN
CREATE USER expense_user_prod WITH PASSWORD '${POSTGRES_PROD_USER_PASSWORD}';

-- Darle permisos sobre la base de producción
GRANT ALL PRIVILEGES ON DATABASE expense_db_prod TO expense_user_prod;

-- Asegurar permisos sobre schema público
\connect expense_db_prod
GRANT ALL PRIVILEGES ON SCHEMA public TO expense_user_prod;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO expense_user_prod;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO expense_user_prod;

-- Configuraciones adicionales para producción
-- Índices para optimización
CREATE INDEX IF NOT EXISTS idx_expenses_created_date ON expenses(created_date);
CREATE INDEX IF NOT EXISTS idx_expenses_category ON expenses(category);
CREATE INDEX IF NOT EXISTS idx_expenses_amount ON expenses(amount);

-- Configurar límites y restricciones
ALTER TABLE expenses ADD CONSTRAINT check_amount_positive CHECK (amount > 0);
ALTER TABLE expenses ADD CONSTRAINT check_description_length CHECK (LENGTH(description) >= 3);