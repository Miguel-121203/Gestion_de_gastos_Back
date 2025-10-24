-- Script SQL con ejemplos de categorías iniciales
-- Este script puede ser ejecutado manualmente en la base de datos

-- Categorías de INGRESOS
INSERT INTO categories (name, description, type, active, created_at, updated_at) VALUES
('Salario', 'Ingresos por salario mensual', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Freelance', 'Ingresos por trabajos freelance', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Inversiones', 'Dividendos y ganancias de inversiones', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bonificaciones', 'Bonos y premios laborales', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ventas', 'Ingresos por ventas de productos o servicios', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alquileres', 'Ingresos por alquiler de propiedades', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Otros Ingresos', 'Otros tipos de ingresos', 'INCOME', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Categorías de GASTOS
INSERT INTO categories (name, description, type, active, created_at, updated_at) VALUES
('Alimentación', 'Gastos en supermercado y comida', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Transporte', 'Gasolina, transporte público, uber', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vivienda', 'Alquiler, hipoteca, servicios públicos', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Salud', 'Médicos, medicinas, seguros de salud', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Educación', 'Colegiaturas, cursos, libros', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Entretenimiento', 'Cine, streaming, hobbies', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ropa', 'Vestuario y calzado', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tecnología', 'Dispositivos, software, internet', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurantes', 'Comidas fuera de casa', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Servicios', 'Servicios profesionales y mantenimiento', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mascotas', 'Cuidado y alimentación de mascotas', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Seguros', 'Seguros varios (auto, vida, hogar)', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Impuestos', 'Impuestos y obligaciones fiscales', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Otros Gastos', 'Otros gastos varios', 'EXPENSE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
