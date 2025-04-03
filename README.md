# Proyecto Banco Pichincha- Prueba Técnica

Este repositorio contiene una solución completa para un sistema bancario basado en arquitectura de microservicios, utilizando Java Spring Boot para el backend y React para el frontend.

---

## 🧱 Estructura del Proyecto

```
proyecto-banco/
├── backend-clientes/           # API REST con Spring Boot
├── mi-proyecto-react/          # Frontend con React (Vite)
├── docker/                     # Archivos Docker centralizados
│   ├── Dockerfile.backend
│   ├── Dockerfile.frontend
│   └── docker-compose.yml
├── postman/
│   └── BancoAPI.postman_collection.json
├── BaseDatos.sql               # Script de creación de tablas
└── README.md
```

---

## ⚙️ Tecnologías

- **Backend:** Java + Spring Boot + JPA + H2
- **Frontend:** React + Vite + Fetch API
- **Base de Datos:** H2 en memoria
- **Contenedores:** Docker, Docker Compose
- **Testing:** Postman

---

## 🚀 Despliegue con Docker Compose

### 📦 Requisitos
- Docker instalado
- Docker Compose instalado

### ▶️ Levantar el proyecto completo

```bash
cd docker
docker-compose up --build
```

### 🌐 Servicios disponibles
- Frontend: http://localhost:5173
- Backend: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

---

## 🧪 Pruebas con Postman

Importa el archivo:
```
postman/BancoAPI.postman_collection.json
```

Usá los endpoints para validar:
- CRUD de Clientes `/api/clientes`
- CRUD de Cuentas `/api/cuentas`
- CRUD de Movimientos `/api/movimientos`
- Reportes `/api/reportes/estado-cuenta`

---

## 🗃 Script de Base de Datos

El archivo `BaseDatos.sql` contiene solo la estructura de las tablas necesarias:

- `persona`
- `cliente`
- `cuenta`
- `movimiento`

---

## 📤 Entrega

- ✅ Subido a repositorio público GitHub
- ✅ Incluye script SQL, Postman, README y Docker
- ✅ Entregable listo para evaluación técnica

---

## ✉️ Correo de entrega

_Asunto:_ Entrega Prueba Técnica Microservicios - Cesar Santacruz

_Cuerpo:_
```
Buenas tardes,

Adjunto la solución solicitada para la prueba técnica.

🔗 Repositorio Global: https://github.com/usuario/backend-banco
📁 Archivos incluidos: BaseDatos.sql, colección Postman, README, Dockerfiles.

Quedo atento a cualquier observación o entrevista técnica.

Saludos cordiales,
Cesar Santacruz
