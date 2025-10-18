#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 || $# -gt 2 ]]; then
  cat <<USAGE
Uso: $0 <ruta-sdk> [zip-commandlinetools]

- <ruta-sdk>: directorio donde se instalará/encuentra el SDK (p. ej. ~/Android/Sdk)
- [zip-commandlinetools]: ruta al archivo ZIP de "Command line tools" descargado manualmente.
  Si el directorio ya contiene cmdline-tools no es necesario especificarlo.
USAGE
  exit 1
fi

SDK_ROOT="$1"
CMDLINE_ZIP="${2:-}"
mkdir -p "$SDK_ROOT"

if [[ ! -d "$SDK_ROOT/cmdline-tools/latest" ]]; then
  if [[ -z "$CMDLINE_ZIP" ]]; then
    echo "No se encontró cmdline-tools. Proporciona la ruta al ZIP descargado como segundo argumento." >&2
    exit 1
  fi

  TMP_DIR=$(mktemp -d)
  trap 'rm -rf "$TMP_DIR"' EXIT

  unzip -q "$CMDLINE_ZIP" -d "$TMP_DIR"
  mkdir -p "$SDK_ROOT/cmdline-tools"
  if [[ -d "$TMP_DIR/cmdline-tools" ]]; then
    mv "$TMP_DIR/cmdline-tools" "$SDK_ROOT/cmdline-tools/latest"
  else
    mv "$TMP_DIR"/tools "$SDK_ROOT/cmdline-tools/latest"
  fi
fi

SDKMANAGER="$SDK_ROOT/cmdline-tools/latest/bin/sdkmanager"
if [[ ! -x "$SDKMANAGER" ]]; then
  echo "sdkmanager no encontrado en $SDKMANAGER" >&2
  exit 1
fi

export ANDROID_SDK_ROOT="$SDK_ROOT"
yes | "$SDKMANAGER" --sdk_root="$SDK_ROOT" --licenses >/dev/null
"$SDKMANAGER" --sdk_root="$SDK_ROOT" \
  "platform-tools" \
  "platforms;android-35" \
  "build-tools;35.0.0" \
  "cmdline-tools;latest"

echo "SDK instalado en $SDK_ROOT"
echo "Recuerda añadir $SDK_ROOT/platform-tools al PATH y crear local.properties con sdk.dir=$SDK_ROOT"
