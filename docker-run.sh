#!/bin/bash
set -euo pipefail

# docker-run.sh - builda a imagem e sobe o container (bind local ./data)
# Requisitos: docker instalado e .env presente na raiz do projeto.

echo "Iniciando build e deploy via Docker..."

if [ -f .env ]; then
  export $(grep -v '^\s*#' .env | xargs)
else
  echo " Arquivo .env não encontrado!"
  exit 1
fi

if [ -z "${APP_PORT:-}" ]; then
  echo "APP_PORT não definido no .env"
  exit 1
fi

if [ -z "${SQLITE_PATH:-}" ]; then
  echo " SQLITE_PATH não definido no .env"
  exit 1
fi

if [ -z "${SQLITE_FILENAME:-}" ]; then
  echo " SQLITE_FILENAME não definido no .env"
  exit 1
fi

echo " APP_PORT       = ${APP_PORT}"
echo " SQLITE_PATH    = ${SQLITE_PATH}"
echo " SQLITE_FILENAME= ${SQLITE_FILENAME}"

HOST_DATA_DIR="./data"
if [ ! -d "${HOST_DATA_DIR}" ]; then
  echo " Pasta ${HOST_DATA_DIR} não existe — criando..."
  mkdir -p "${HOST_DATA_DIR}"
fi

echo " Ajustando permissões da pasta ${HOST_DATA_DIR} (chmod 777)..."
chmod -R 777 "${HOST_DATA_DIR}"

IMAGE_NAME="teleconsulta-app"
CONTAINER_NAME="teleconsulta-app-container"

echo "  Buildando a imagem Docker (${IMAGE_NAME})..."
docker build -t "${IMAGE_NAME}" .

echo " Removendo container antigo (se existir)..."
docker rm -f "${CONTAINER_NAME}" 2>/dev/null || true

echo " Iniciando container ${CONTAINER_NAME}..."
docker run -d \
  --name "${CONTAINER_NAME}" \
  -p "${APP_PORT}:8080" \
  -e APP_ENV=docker \
  -e SQLITE_PATH="${SQLITE_PATH}" \
  -e SQLITE_FILENAME="${SQLITE_FILENAME}" \
  -v "$(pwd)/data:${SQLITE_PATH}" \
  --restart unless-stopped \
  "${IMAGE_NAME}"

echo " Container iniciado com sucesso!"
echo " Acesse: http://localhost:${APP_PORT}"

