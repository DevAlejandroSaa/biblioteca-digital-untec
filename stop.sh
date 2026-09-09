#!/bin/bash

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"

BASE_COMPOSE="$ROOT/docker/docker-compose.yml"
SERVICE_DIR="$ROOT/docker/service"

# Construir lista de archivos -f
COMPOSE_FILES=(-f "$BASE_COMPOSE")

for file in "$SERVICE_DIR"/*.yml; do
  [ -e "$file" ] || continue
  echo "📦 Incluyendo $(basename "$file")"
  COMPOSE_FILES+=(-f "$file")
done

echo "🛑 Deteniendo sistema completo..."

docker compose --project-directory "$ROOT/docker" "${COMPOSE_FILES[@]}" down

echo "✅ Sistema detenido"