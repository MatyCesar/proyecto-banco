-- BaseDatos.sql - Solo estructura (sin datos)

-- Eliminar si existen
DROP TABLE IF EXISTS movimiento;
DROP TABLE IF EXISTS cuenta;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS persona;

-- Tabla Persona (base abstracta)
CREATE TABLE persona (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  genero VARCHAR(10),
  edad INT,
  identificacion VARCHAR(50),
  direccion VARCHAR(255),
  telefono VARCHAR(50)
);

-- Tabla Cliente
CREATE TABLE cliente (
  id BIGINT PRIMARY KEY,
  contrasena VARCHAR(100),
  estado BOOLEAN,
  FOREIGN KEY (id) REFERENCES persona(id)
);

-- Tabla Cuenta
CREATE TABLE cuenta (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  numero VARCHAR(20),
  tipo VARCHAR(20),
  saldo_inicial DOUBLE,
  estado BOOLEAN,
  cliente_id BIGINT,
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Tabla Movimiento
CREATE TABLE movimiento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  fecha DATE,
  tipo VARCHAR(20),
  valor DOUBLE,
  saldo DOUBLE,
  cuenta_id BIGINT,
  FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);
