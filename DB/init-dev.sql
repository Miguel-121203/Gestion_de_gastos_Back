-- Crear usuario de aplicación para DEV
CREATE USER expense_user_dev WITH PASSWORD 'dev123456';

-- Darle permisos sobre la base de desarrollo
GRANT ALL PRIVILEGES ON DATABASE expense_db_dev TO expense_user_dev;

-- Asegurar permisos sobre schema público
\connect expense_db_dev
GRANT ALL PRIVILEGES ON SCHEMA public TO expense_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO expense_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO expense_user_dev;

-- Insertar datos de prueba para desarrollo
INSERT INTO expenses (description, amount, category, created_date) VALUES 
('Almuerzo equipo desarrollo', 25000, 'COMIDA', NOW()),
('Licencias software dev', 150000, 'SOFTWARE', NOW()),
('Transporte reunión cliente', 12000, 'TRANSPORTE', NOW())
ON CONFLICT DO NOTHING;
