# Módulo: Asignación Docente

---

## 📌 Descripción General

El módulo **Asignación Docente** permite gestionar la carga académica de los docentes dentro del sistema **Baldwin ERP**. Aquí se asocia un docente a una clase (curso + sección + grado) con un tipo de actividad específico (Pedagógica, Administrativa, etc.).

---

## 🏗️ Arquitectura del Módulo

```
asignacion-docente/
├── asignacion-docente/
│   ├── asignacion-docente.ts          # Componente contenedor principal
│   ├── asignacion-docente.html        # Template del contenedor
│   └── asignacion-docente.css         # Estilos del contenedor
├── components/
│   ├── asignacion-list/
│   │   ├── asignacion-list.ts         # Lógica de la tabla (CRUD + Signals)
│   │   ├── asignacion-list.html       # Template de la tabla (con modal)
│   │   ├── asignacion-list.css        # Estilos de la lista
│   │   └── asignacion-list.spec.ts    # Tests unitarios
│   └── asignacion-form-modal/
│       ├── asignacion-form-modal.ts   # Lógica del formulario (Create/Edit)
│       ├── asignacion-form-modal.html # Template del modal (glassmorphism)
│       └── asignacion-form-modal.css  # Estilos del modal
```

---

## 🔗 Modelos de Datos

### `AsignacionDocenteRequest` (Frontend → Backend)

| Campo             | Tipo     | Requerido | Descripción                         |
|-------------------|----------|-----------|-------------------------------------|
| `estado`          | `Estado` | ✅        | ACTIVO, INACTIVO, NUEVO, SUSPENDIDO |
| `observaciones`   | `string` | ❌        | Notas opcionales                    |
| `docenteId`       | `number` | ✅        | ID del docente                      |
| `claseId`         | `number` | ✅        | ID de la clase académica            |
| `tipoActividadId` | `number` | ✅        | ID del tipo de actividad            |

### `AsignacionDocenteResponse` (Backend → Frontend)

| Campo                 | Tipo     | Descripción                              |
|-----------------------|----------|------------------------------------------|
| `id`                  | `number` | Identificador único                      |
| `estado`              | `Estado` | Estado de la asignación                  |
| `observaciones`       | `string` | Notas                                    |
| `docenteId`           | `number` | ID del docente                           |
| `docenteNombre`       | `string` | Nombre completo del docente              |
| `claseId`             | `number` | ID de la clase                           |
| `cursoNombre`         | `string` | Nombre del curso (ej: Matemáticas)       |
| `gradoNombre`         | `string` | Nombre del grado (ej: 1er Grado)        |
| `seccionNombre`       | `string` | Nombre de la sección (ej: Sección A)    |
| `tipoActividadId`     | `number` | ID del tipo de actividad                 |
| `tipoActividadNombre` | `string` | Nombre del tipo (ej: Pedagógica)        |

---

## 🌐 Endpoints del Backend

| Método   | Ruta                              | Descripción                  | Acceso    |
|----------|-----------------------------------|------------------------------|-----------|
| `GET`    | `/api/asignacion-docente`         | Listar todas las asignaciones | `@IsStaff`|
| `GET`    | `/api/asignacion-docente/{id}`    | Obtener por ID               | `@IsStaff`|
| `POST`   | `/api/asignacion-docente`         | Crear nueva asignación       | `@IsStaff`|
| `PUT`    | `/api/asignacion-docente/{id}`    | Actualizar asignación        | `@IsStaff`|
| `DELETE` | `/api/asignacion-docente/{id}`    | Eliminar asignación          | `@IsAdmin`|

### Endpoints auxiliares usados por el formulario

| Método | Ruta                  | Descripción                        |
|--------|-----------------------|------------------------------------|
| `GET`  | `/api/Docentes`       | Lista de docentes (para selector)  |
| `GET`  | `/api/clases`         | Lista de clases (para selector)    |
| `GET`  | `/api/tipo-actividad` | Lista de tipos de actividad        |

---

## ⚙️ Patrones Técnicos

### Estado Reactivo con Signals (Angular 17+)

El componente `AsignacionListComponent` utiliza **Angular Signals** para gestionar el estado local:

```typescript
asignaciones = signal<AsignacionDocenteResponse[]>([]);
isLoading = signal<boolean>(false);
showModal = signal<boolean>(false);
selectedAsignacion = signal<AsignacionDocenteResponse | undefined>(undefined);
```

### Comunicación Padre → Modal

El modal se comunica con el listado mediante `@Input()` y `@Output()`:

```html
@if (showModal()) {
    <app-asignacion-form-modal
        [asignacion]="selectedAsignacion()"   <!-- Datos para editar (undefined = crear) -->
        (onClose)="closeModal()"               <!-- Cerrar el modal -->
        (onSave)="cargarAsignaciones()">        <!-- Refrescar la tabla -->
    </app-asignacion-form-modal>
}
```

### Formulario Reactivo

El modal usa `ReactiveFormsModule` con validación:

```typescript
this.asignacionForm = this.fb.group({
    docenteId:       [null, Validators.required],
    claseId:         [null, Validators.required],
    tipoActividadId: [null, Validators.required],
    estado:          ['NUEVO', Validators.required],
    observaciones:   ['']
});
```

---

## 🎨 Diseño UI / UX

### Principios Aplicados

| Principio              | Implementación                                                              |
|------------------------|-----------------------------------------------------------------------------|
| **Tema Adaptativo**    | Todos los elementos usan `bg-white dark:bg-slate-900` para light/dark mode |
| **Glassmorphism**      | `backdrop-blur-sm`, `bg-white/40`, bordes semitransparentes                |
| **Marca Baldwin**      | Degradado `bg-btn-gradient` (azul → verde), colores `baldwin-verde/cobalto`|
| **Micro-animaciones**  | `transition-all duration-300`, `active:scale-95`, hover con slide           |
| **Tipografía Premium** | `font-black`, `uppercase`, `tracking-widest` para encabezados              |

### Estados Visuales en la Tabla

| Estado       | Color              | Badge                                   |
|--------------|--------------------|-----------------------------------------|
| `ACTIVO`     | Emerald (verde)    | `bg-emerald-500/10 text-emerald-500`    |
| `NUEVO`      | Blue (azul)        | `bg-blue-500/10 text-blue-500`          |
| `INACTIVO`   | Rose (rosado)      | `bg-rose-500/10 text-rose-500`          |
| `SUSPENDIDO` | Amber (amarillo)   | `bg-amber-500/10 text-amber-500`        |

---

## 🔧 Servicio Principal

**Ubicación:** `core/services/asignaciondocente/asignaciondocente_services.ts`

```typescript
@Injectable({ providedIn: 'root' })
export class AsignacionDocenteService {
    private readonly API_URL = 'http://localhost:8080/api';

    // CRUD
    listar():                 Observable<AsignacionDocenteResponse[]>
    obtenerPorId(id):         Observable<AsignacionDocenteResponse>
    registrar(data):          Observable<AsignacionDocenteResponse>
    actualizar(id, data):     Observable<AsignacionDocenteResponse>
    eliminar(id):             Observable<void>

    // Para los selectores del formulario
    listarDocentes():         Observable<DocenteResponse[]>
    listarClases():           Observable<ClaseResponse[]>
    listarTiposActividad():   Observable<TipoActividadResponse[]>
}
```

---

## 📝 Notas Importantes

1. **El endpoint de docentes usa `D` mayúscula**: `/api/Docentes` (según el controlador del backend).
2. **El modelo `DocenteResponse` es plano**: Los campos `nombres` y `apellidos` están en el nivel raíz, NO anidados en `usuario.persona`.
3. **El `select` de `<option [value]>` envía strings**: Angular convierte `[value]` a string. Si el backend espera `Long`, esto funciona correctamente con Spring Boot ya que el `@RequestBody` deserializa correctamente.
4. **Lucide Icons**: El módulo requiere `LucideAngularModule` importado y los iconos registrados en `app.config.ts`.

---

## 🚀 Ruta de Navegación

```
/admin/asignacion-docente
```

Configurado en:
- `app.routes.ts` → `loadComponent` con lazy loading
- `nav-config.ts` → Sección "Configuración Académica"
