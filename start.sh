#!/bin/bash

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"

BASE_COMPOSE="$ROOT/docker/docker-compose.yml"
SERVICE_DIR="$ROOT/docker/service"

# volumes para mysql
mkdir -p "$ROOT/docker/volumes/mysql/data"

# Construir lista de archivos -f
COMPOSE_FILES=(-f "$BASE_COMPOSE")

for file in "$SERVICE_DIR"/*.yml; do
  [ -e "$file" ] || continue
  echo "📦 Incluyendo $(basename "$file")"
  COMPOSE_FILES+=(-f "$file")
done

echo "🔨 Construyendo imágenes..."
docker compose --project-directory "$ROOT/docker" "${COMPOSE_FILES[@]}" build --no-cache

echo "🚀 Levantando sistema completo..."
docker compose --project-directory "$ROOT/docker" "${COMPOSE_FILES[@]}" up -d --force-recreate

echo "✅ Sistema levantado"