# Guía de Desarrollo: Asignación Docente

Este documento describe la arquitectura, el diseño y las soluciones técnicas aplicadas al módulo de **Asignación Docente**.

---

## 🛠️ Soluciones Técnicas Importantes

### 1. Corregir el "Lag" en Selectores y Renderizado Inicial
**Problema:** Los selectores (`select`) a veces aparecían vacíos o requerían interacción manual para mostrar datos.
**Solución:** 
- **Angular Signals + `forkJoin`**: Se sincronizan todas las peticiones asíncronas antes de habilitar el formulario.
- **`ChangeDetectorRef.detectChanges()`**: Se añadió una llamada explícita tras cargar los datos maestros. Esto garantiza que Angular refresque la vista en el mismo ciclo en que llegan los datos, eliminando la necesidad de hacer "clic en otra parte".

### 2. Centrado y Posicionamiento de Modales
**Problema:** El modal aparecía desplazado hacia arriba o se cortaba en pantallas pequeñas.
**Solución:** 
- Se implementó un contenedor `fixed inset-0` con `grid place-items-center`.
- Se añadió un `max-h-[95vh]` y `overflow-y-auto` interno para asegurar que el encabezado y el pie de página sean siempre accesibles.

---

## 🎨 Sistema de Diseño (Baldwin Premium)

### Fondos y Transparencias
Para mantener la coherencia visual en modo claro y oscuro:
- **Overlay (Fondo)**: Se utiliza `bg-slate-950/85` con `backdrop-blur-md`. Esto crea un contraste fuerte y profesional, ocultando distracciones del fondo.
- **Contenedor**: Usa `rounded-[2.5rem]` y sombras profundas (`shadow-[0_20px_50px_rgba(0,0,0,0.5)]`).

### Estilizado de Inputs (Dark Mode)
- **Modo Claro**: `bg-slate-50 border-slate-200 text-slate-700`.
- **Modo Oscuro**: `dark:bg-slate-800/50 dark:border-slate-700/50 dark:text-slate-200`.
- **Foco**: Se utiliza un anillo de color marca `focus:ring-baldwin-verde/10` para una respuesta visual suave.

---

## 🏗️ Estructura del Módulo

| Componente | Función |
|------------|---------|
| `AsignacionListComponent` | Tabla principal con gestión de estado mediante Signals. |
| `AsignacionFormModal` | Formulario reactivo para creación y edición. |
| `AsignacionDocenteService` | Consumo de API REST (CRUD + Selectores). |

---

## 🔧 Consejos para Futuros Cambios

1. **Z-Index**: Los modales deben usar `z-[100]` o superior para estar por encima de la barra lateral y cabecera del sistema.
2. **Atomicidad**: Si añades un nuevo campo al formulario que dependa de una lista externa, añádela al `forkJoin` en `loadInitialData()` para evitar renders parciales.
3. **Validación**: No olvides el estado `isLoadingData()`; el botón de guardado debe estar deshabilitado mientras se sincronizan los datos para evitar errores de referencia nula.

---

> [!IMPORTANT]
> Mantener siempre el uso de `lucide-icons` para la iconografía, respetando el tamaño `w-5 h-5` en inputs para consistencia visual.
