# 🎓 SantoTrack

SantoTrack es una aplicación nativa para Android diseñada para ayudar a los estudiantes a llevar un control absoluto de su rendimiento académico. Permite registrar asignaturas, visualizar el progreso semestral, y cuenta con un simulador predictivo para calcular exactamente qué nota necesitas en tus próximos exámenes para aprobar.

> **Estado actual del proyecto:** En etapa de desarrollo inicial (Esqueleto visual UI y navegación base implementada mediante empty states).

---

## ✨ Características (Roadmap)

- **📚 Gestión de Asignaturas:** Crea y personaliza tus ramos con colores identificadores.
- **📊 Simulador Predictivo:** Calcula qué nota mínima requieres en tu Examen o Certamen final para aprobar el ramo (ej. *"Necesitas un 4.5 en el Certamen 3 para aprobar"*).
- **📈 Control de Notas (Calculadora):** Registra tus pruebas, trabajos con sus respectivas ponderaciones.
- **✅ Control de Asistencia:** Visualización de porcentajes de asistencia y cálculo del margen de inasistencias permitidas. (Próximamente)
- **🔔 Alertas Tempranas:** Alertas visuales para asignaturas en riesgo de reprobación.

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Java
- **UI:** Android XML Layouts (Vistas clásicas)
- **Diseño:** Material Design 3 (con colores y componentes customizados)
- **Build System:** Gradle (Kotlin DSL)
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 37

---

## 🚀 Requisitos Previos

Para poder abrir, editar y compilar este proyecto en tu entorno local, necesitas tener instalado lo siguiente:

1. [Android Studio](https://developer.android.com/studio) (Versión Ladybug, Koala o superior recomendada).
2. **Java Development Kit (JDK):** Versión 11 o superior (viene integrado normalmente con Android Studio).
3. **Android SDK:** API 37 (Se descargará automáticamente al sincronizar el proyecto en Android Studio).

---

## ⚙️ Instalación y Ejecución

Sigue estos pasos para correr la aplicación en tu celular físico o en un emulador:

1. **Clona este repositorio** en tu máquina local:
   ```bash
   git clone https://github.com/TU_USUARIO/SantoTrack.git
   ```

2. **Abre el proyecto en Android Studio:**
   - Abre Android Studio y selecciona `File > Open`.
   - Navega hasta la carpeta `SantoTrack` que acabas de clonar y presiona `OK`.

3. **Sincroniza Gradle:**
   - Android Studio empezará a descargar las dependencias necesarias. Si te aparece un banner en la parte superior derecha diciendo "Sync Now", hazle clic.

4. **Ejecuta la App:**
   - Conecta tu teléfono Android mediante USB (con la Depuración USB activada) o inicia un Emulador (Android Virtual Device).
   - Presiona el botón verde de **Play (Run 'app')** en la barra superior o usa el atajo `Shift + F10`.

---

## 📂 Arquitectura Básica del Proyecto

- `app/src/main/java/.../ui`: Contiene la lógica de las pantallas y fragmentos (`AsignaturasFragment`, `CalculadoraFragment`, `NuevaMateriaFragment`).
- `app/src/main/res/layout`: Contiene todo el diseño visual (maquetación) en XML.
- `app/src/main/res/values`: Contiene la base de nuestro sistema de diseño: `colors.xml`, `strings.xml` y `themes.xml`.
- `app/build.gradle.kts`: Archivo de dependencias y configuración del módulo de la app.
