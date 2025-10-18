# Kalma

Aplicación Android para registrar el estado de ánimo y recibir recordatorios diarios.

## Requisitos previos

- **JDK 17** (puede usarse la distribución de Temurin o Zulu).
- **Android SDK** con las plataformas y herramientas para API 35.
- Al menos 8 GB de RAM libres para compilar el proyecto cómodamente.

## Preparar el JDK

1. Descarga el JDK 17 apropiado para tu sistema operativo.
2. Instala el paquete y añade el directorio `bin` del JDK a tu variable `PATH`.
3. Comprueba la instalación ejecutando `java -version` y `javac -version`.

## Instalar el Android SDK

En este repositorio no es posible instalar el SDK directamente porque el entorno aislado no tiene acceso a los servidores de Google. Ejecuta estos pasos en tu equipo local:

1. Descarga el paquete "Command line tools" desde [developer.android.com](https://developer.android.com/studio#cmdline-tools). Coloca el archivo `commandlinetools-*.zip` en una carpeta temporal.
2. (Opcional) Copia el script `scripts/setup-android-sdk.sh` a tu máquina y dale permisos de ejecución: `chmod +x scripts/setup-android-sdk.sh`.
3. Lanza el script indicando la ruta al ZIP descargado y el destino donde quieras instalar el SDK. Por ejemplo:

   ```bash
   ./scripts/setup-android-sdk.sh ~/Android/Sdk ~/Downloads/commandlinetools-linux-10406996_latest.zip
   ```

   El script creará la estructura de `cmdline-tools`, instalará `platform-tools`, `platforms;android-35` y `build-tools;35.0.0`, y aceptará las licencias necesarias. Si prefieres hacerlo manualmente, puedes ejecutar `sdkmanager` con los mismos paquetes.

4. Define la variable de entorno `ANDROID_SDK_ROOT` apuntando al directorio donde quedó el SDK.
5. Crea un archivo `local.properties` en la raíz del proyecto con este contenido (ajusta la ruta según corresponda):

   ```
   sdk.dir=/home/usuario/Android/Sdk
   ```

## Construir la aplicación

Una vez configuradas las herramientas:

```bash
./gradlew assembleDebug
```

Si trabajas desde Android Studio, importa el proyecto desde esta carpeta raíz; el IDE detectará automáticamente la configuración de Gradle y el SDK.

## Solución de problemas

- Si `./gradlew` se queda esperando en "Downloading https://services.gradle.org/..." verifica tu conexión y que el firewall permita la descarga de distribuciones de Gradle.
- Si el ensamblado falla por no encontrar el SDK, revisa que `local.properties` apunte correctamente al directorio instalado.
- En dispositivos con Android 13+ asegúrate de conceder el permiso de notificaciones para que el recordatorio diario funcione correctamente.

