#!/usr/bin/env bash
set -e

# Create secrets directory if it doesn't exist
mkdir -p secrets

# Helper to create secret file with default value if not present
create_secret() {
  local file=$1
  local default=$2
  if [ ! -f "secrets/$file" ]; then
    echo "$default" > "secrets/$file"
    echo "Created secrets/$file"
  fi
}

create_secret db_password "postgres"
create_secret jwt_secret "secret"
create_secret admin_user "admin"
create_secret admin_password "admin"

# Start services using docker-compose

docker-compose up -d --build
