# Data Entry Application

## Configuración del Frontend

### Requisitos Previos

- Node.js (versión 20.x o superior)
- npm (versión 10.x o superior)

### Pasos de Instalación

1. **Instalar Angular CLI globalmente:**

```bash
npm install -g @angular/cli@18
```

2. **Navegar a la carpeta del proyecto angular:**
cd frontend

cd template

cd package

3. **Instalar dependencias adicionales:**

npm install
### Estructura del Proyecto

El proyecto sigue una estructura modular con los siguientes componentes:

- **Módulos**: Cada entidad tiene su propio módulo
- **Componentes**: Componentes CRUD para cada entidad
- **Servicios**: Servicios HTTP para comunicación con el backend
- **Modelos**: Interfaces TypeScript basadas en las entidades
- **Rutas**: Configuración de rutas para navegación

### Ejecución del Proyecto Angular

**Navega hasta la carpeta principal del proyecto angular:**
cd src
# Iniciar el servidor de desarrollo
ng serve
```

La aplicación estará disponible en `http://localhost:4200`

### Ejecución del proyecto Spring boot

# Ejecuta la clase 'DataEntryAppication'

El proyecto estara disponible en 'http://localhost:8080'

### Características Principales

- Interfaz de usuario moderna con Angular Material
- Formularios dinámicos con NGX-Formly
- Operaciones CRUD completas para cada entidad
