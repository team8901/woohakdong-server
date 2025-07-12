#!/bin/bash

REPO_DIR="/home/ubuntu/workspace"
BRANCH="main"
IMAGE_NAME="woohakdong-dev:latest"
CONTAINER_NAME="woohakdong"
PORT="8080"

cd "$REPO_DIR" || exit

echo "Starting auto-deploy loop..."

while true; do
  echo "[INFO] Checking for updates..."
  git fetch origin "$BRANCH"

  LOCAL_HASH=$(git rev-parse "$BRANCH")
  REMOTE_HASH=$(git rev-parse "origin/$BRANCH")

  if [ "$LOCAL_HASH" != "$REMOTE_HASH" ]; then
    echo "[INFO] Change detected. Pulling latest code and redeploying..."

    git pull origin "$BRANCH"

    echo "[INFO] Building Docker image..."
    docker build -t "$IMAGE_NAME" .

    echo "[INFO] Stopping existing container..."
    docker stop "$CONTAINER_NAME"
    docker rm "$CONTAINER_NAME"

    echo "[INFO] Starting new container..."
    docker run -d -p 80:"$PORT" --name "$CONTAINER_NAME" "$IMAGE_NAME"
  else
    echo "[INFO] No changes."
  fi

  sleep 30
done