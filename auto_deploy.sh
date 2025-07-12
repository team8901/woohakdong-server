#!/bin/bash

REPO_DIR="/home/ubuntu/workspace"
BRANCH="main"
IMAGE_NAME="woohakdong-dev:latest"
CONTAINER_NAME="woohakdong"
PORT="8080"

# 슬랙 웹훅 환경변수 불러오기
SLACK_WEBHOOK_URL=${SLACK_WEBHOOK_URL}

# 슬랙 메시지 함수 정의 (먼저 선언되어야 함)
send_slack_message() {
  local MESSAGE=$1
  if [[ -n "$SLACK_WEBHOOK_URL" ]]; then
    curl -X POST -H 'Content-type: application/json' \
      --data "{\"text\":\"${MESSAGE}\"}" \
      "$SLACK_WEBHOOK_URL"
  fi
}

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
    if ! docker build -t "$IMAGE_NAME" .; then
      send_slack_message "❌ Docker 이미지 빌드 실패"
      exit 1
    fi

    echo "[INFO] Stopping existing container..."
    docker stop "$CONTAINER_NAME"
    docker rm "$CONTAINER_NAME"

    echo "[INFO] Starting new container..."
    docker run -d -p 80:"$PORT" --name "$CONTAINER_NAME" "$IMAGE_NAME"
    send_slack_message "🚀 배포 완료! 최신 코드로 서비스 중."
  else
    echo "[INFO] No changes."
  fi

  sleep 30
done