-- =====================================================
-- ROLES
-- =====================================================
INSERT INTO rol (nombre, esadministrador)
SELECT 'ADMINISTRADOR', true
    WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre='ADMINISTRADOR');

INSERT INTO rol (nombre, esadministrador)
SELECT 'CLIENTE', false
    WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre='CLIENTE');

-- =====================================================
-- TIPO OBJETO
-- =====================================================
INSERT INTO tipo_objeto (id, descripcion, clase_name)
SELECT 1, 'Entidad', 'SistemadeGestiondeOrdenes.entity.entities'
    WHERE NOT EXISTS (SELECT 1 FROM tipo_objeto WHERE id=1);

INSERT INTO tipo_objeto (id, descripcion, clase_name)
SELECT 2, 'OpcionMenu', 'SistemadeGestiondeOrdenes.seguridad.persistence.entities.OpcionMenu'
    WHERE NOT EXISTS (SELECT 1 FROM tipo_objeto WHERE id=2);

-- =====================================================
-- OBJETOS (ENTIDADES)
-- =====================================================
INSERT INTO objeto (nombre_objeto, tipo_objeto_id)
SELECT 'Ordenlaboratorio', 1
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Ordenlaboratorio');

INSERT INTO objeto (nombre_objeto, tipo_objeto_id)
SELECT 'ConfiguracionApi', 1
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='ConfiguracionApi');

-- =====================================================
-- USUARIO ADMIN
-- =====================================================
INSERT INTO usuario (activo, correo, name, password, username)
SELECT true, 'admin@admin.com', 'admin', 'admin1234', 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username='admin');

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuario u
         JOIN rol r ON r.nombre='ADMINISTRADOR'
WHERE u.username='admin'
  AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur
    WHERE ur.usuario_id=u.id AND ur.rol_id=r.id
);

-- =====================================================
-- ACCIONES
-- =====================================================
INSERT INTO accion (nombre, descripcion)
SELECT 'save', 'Permite crear un nuevo registro'
    WHERE NOT EXISTS (SELECT 1 FROM accion WHERE nombre='save');

INSERT INTO accion (nombre, descripcion)
SELECT 'findAll', 'Permite leer registros'
    WHERE NOT EXISTS (SELECT 1 FROM accion WHERE nombre='findAll');

INSERT INTO accion (nombre, descripcion)
SELECT 'update', 'Permite modificar registros'
    WHERE NOT EXISTS (SELECT 1 FROM accion WHERE nombre='update');

INSERT INTO accion (nombre, descripcion)
SELECT 'deleteById', 'Permite eliminar registros'
    WHERE NOT EXISTS (SELECT 1 FROM accion WHERE nombre='deleteById');

INSERT INTO accion (nombre, descripcion)
SELECT 'ver', 'Permite ver opcion de menu'
    WHERE NOT EXISTS (SELECT 1 FROM accion WHERE nombre='ver');

-- =====================================================
-- OBJETOS OPCION MENU
-- =====================================================
INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Home'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Home');

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Inicio'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Inicio');

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Ordenlaboratorio'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Ordenlaboratorio' AND tipo_objeto_id=2);

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Ver Ordenlaboratorio'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Ver Ordenlaboratorio');

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Gestionar Ordenlaboratorio'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Gestionar Ordenlaboratorio');

-- =====================================================
-- ACCION_OBJETO (RELACIÓN)
-- =====================================================
INSERT INTO accion_objeto (accion_id, objeto_id)
SELECT a.id, o.id
FROM accion a
         JOIN objeto o ON o.nombre_objeto='Ordenlaboratorio'
WHERE a.nombre IN ('save','findAll','update','deleteById')
  AND NOT EXISTS (
    SELECT 1 FROM accion_objeto ao
    WHERE ao.accion_id=a.id AND ao.objeto_id=o.id
);

-- =====================================================
-- PRIVILEGIOS ADMIN
-- =====================================================
INSERT INTO privilegio (autorizado, accion_objeto_id, rol_id, usuario_id)
SELECT true, ao.id, r.id, null
FROM accion_objeto ao
         JOIN rol r ON r.nombre='ADMINISTRADOR'
WHERE NOT EXISTS (
    SELECT 1 FROM privilegio p
    WHERE p.accion_objeto_id=ao.id AND p.rol_id=r.id
);

-- =====================================================
-- PRIVILEGIOS CLIENTE (NEGADOS)
-- =====================================================
INSERT INTO privilegio (autorizado, accion_objeto_id, rol_id, usuario_id)
SELECT false, ao.id, r.id, null
FROM accion_objeto ao
         JOIN rol r ON r.nombre='CLIENTE'
WHERE NOT EXISTS (
    SELECT 1 FROM privilegio p
    WHERE p.accion_objeto_id=ao.id AND p.rol_id=r.id
);

-- =====================================================
-- EMAIL CONFIG
-- =====================================================
INSERT INTO email_configuration (host, port, username, password, auth, starttls_enable)
SELECT 'smtp.office365.com', 587, 'admin@dtl360.com', 'mgdvxfrdjhwwwlfd', true, true
    WHERE NOT EXISTS (SELECT 1 FROM email_configuration WHERE host='smtp.office365.com');

-- =====================================================
-- CATEGORÍAS ECOMMERCE
-- =====================================================
INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
SELECT 'Electrónica', 'Productos electrónicos y tecnología', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre='Electrónica');

INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
SELECT 'Ropa', 'Ropa y accesorios de moda', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre='Ropa');

INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
SELECT 'Hogar', 'Artículos para el hogar', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre='Hogar');

INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
SELECT 'Deportes', 'Artículos deportivos y fitness', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre='Deportes');

INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion, fecha_actualizacion)
SELECT 'Libros', 'Libros y material educativo', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre='Libros');

-- =====================================================
-- PRODUCTOS ECOMMERCE (EJEMPLOS)
-- =====================================================
INSERT INTO producto (nombre, descripcion, precio, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Laptop HP Pavilion', 'Laptop HP con procesador Intel Core i5, 8GB RAM, 256GB SSD', 799.99, 15, 'LAP-HP-001', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Electrónica'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='LAP-HP-001');

INSERT INTO producto (nombre, descripcion, precio, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Smartphone Samsung Galaxy', 'Smartphone con pantalla de 6.5", 128GB almacenamiento', 599.99, 25, 'PHONE-SAM-001', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Electrónica'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='PHONE-SAM-001');

INSERT INTO producto (nombre, descripcion, precio, precio_oferta, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Auriculares Bluetooth', 'Auriculares inalámbricos con cancelación de ruido', 149.99, 99.99, 50, 'AUR-BT-001', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Electrónica'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='AUR-BT-001');

INSERT INTO producto (nombre, descripcion, precio, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Camiseta Deportiva Nike', 'Camiseta de entrenamiento para hombre', 29.99, 100, 'CAM-NIKE-001', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Ropa'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='CAM-NIKE-001');

INSERT INTO producto (nombre, descripcion, precio, precio_oferta, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Zapatillas Running Adidas', 'Zapatillas para running con tecnología Boost', 119.99, 89.99, 40, 'ZAP-ADI-001', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Deportes'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='ZAP-ADI-001');

INSERT INTO producto (nombre, descripcion, precio, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Cafetera Programable', 'Cafetera con temporizador y filtro permanente', 79.99, 30, 'CAF-PRO-001', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Hogar'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='CAF-PRO-001');

INSERT INTO producto (nombre, descripcion, precio, stock, sku, activo, destacado, fecha_creacion, fecha_actualizacion, categoria_id)
SELECT 'Java Programming Book', 'Guía completa de programación Java para principiantes', 45.99, 60, 'BOOK-JAVA-001', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, c.categoria_id
FROM categoria c
WHERE c.nombre='Libros'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE sku='BOOK-JAVA-001');

-- =====================================================
-- OBJETOS MENU ECOMMERCE
-- =====================================================
INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'E-commerce'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='E-commerce' AND tipo_objeto_id=2);

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Tienda'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Tienda' AND tipo_objeto_id=2);

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Mi Carrito'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Mi Carrito' AND tipo_objeto_id=2);

INSERT INTO objeto (tipo_objeto_id, nombre_objeto)
SELECT 2, 'Mis Órdenes'
    WHERE NOT EXISTS (SELECT 1 FROM objeto WHERE nombre_objeto='Mis Órdenes' AND tipo_objeto_id=2);

-- =====================================================
-- ACCION_OBJETO PARA OPCIONES MENU ECOMMERCE
-- =====================================================
INSERT INTO accion_objeto (accion_id, objeto_id)
SELECT a.id, o.id
FROM accion a
         JOIN objeto o ON o.nombre_objeto IN (
                                               'E-commerce',
                                               'Tienda',
                                               'Mi Carrito',
                                               'Mis Órdenes'
    ) AND o.tipo_objeto_id=2
WHERE a.nombre='ver'
  AND NOT EXISTS (
    SELECT 1 FROM accion_objeto ao
    WHERE ao.accion_id=a.id AND ao.objeto_id=o.id
);

-- =====================================================
-- OPCIONES MENÚ ECOMMERCE (Solo secciones públicas)
-- =====================================================
-- Sección E-commerce
INSERT INTO opcion_menu (nav_cap, display_name, icon_name, route, nombreobjeto, orden)
SELECT 'E-commerce', NULL, NULL, NULL, 'E-commerce', 20
    WHERE NOT EXISTS (SELECT 1 FROM opcion_menu WHERE nombreobjeto='E-commerce');

INSERT INTO opcion_menu (nav_cap, display_name, icon_name, route, nombreobjeto, orden)
SELECT NULL, 'Tienda', 'shopping_cart', '/tienda', 'Tienda', 21
    WHERE NOT EXISTS (SELECT 1 FROM opcion_menu WHERE nombreobjeto='Tienda');

INSERT INTO opcion_menu (nav_cap, display_name, icon_name, route, nombreobjeto, orden)
SELECT NULL, 'Mi Carrito', 'shopping_bag', '/carrito', 'Mi Carrito', 22
    WHERE NOT EXISTS (SELECT 1 FROM opcion_menu WHERE nombreobjeto='Mi Carrito');

INSERT INTO opcion_menu (nav_cap, display_name, icon_name, route, nombreobjeto, orden)
SELECT NULL, 'Mis Órdenes', 'receipt_long', '/mis-ordenes', 'Mis Órdenes', 23
    WHERE NOT EXISTS (SELECT 1 FROM opcion_menu WHERE nombreobjeto='Mis Órdenes');

-- =====================================================
-- PRIVILEGIOS PARA OPCIONES ECOMMERCE
-- =====================================================
-- Privilegios para ADMINISTRADOR
INSERT INTO privilegio (autorizado, accion_objeto_id, rol_id, usuario_id)
SELECT true, ao.id, r.id, null
FROM accion_objeto ao
         JOIN objeto o ON o.id = ao.objeto_id
         JOIN accion a ON a.id = ao.accion_id
         JOIN rol r ON r.nombre='ADMINISTRADOR'
WHERE o.nombre_objeto IN (
                          'E-commerce',
                          'Tienda',
                          'Mi Carrito',
                          'Mis Órdenes'
    )
  AND a.nombre='ver'
  AND NOT EXISTS (
    SELECT 1 FROM privilegio p
    WHERE p.accion_objeto_id=ao.id AND p.rol_id=r.id
);

-- Privilegios para CLIENTE (acceso completo al ecommerce)
INSERT INTO privilegio (autorizado, accion_objeto_id, rol_id, usuario_id)
SELECT true, ao.id, r.id, null
FROM accion_objeto ao
         JOIN objeto o ON o.id = ao.objeto_id
         JOIN accion a ON a.id = ao.accion_id
         JOIN rol r ON r.nombre='CLIENTE'
WHERE o.nombre_objeto IN (
                          'E-commerce',
                          'Tienda',
                          'Mi Carrito',
                          'Mis Órdenes'
    )
  AND a.nombre='ver'
  AND NOT EXISTS (
    SELECT 1 FROM privilegio p
    WHERE p.accion_objeto_id=ao.id AND p.rol_id=r.id
);
