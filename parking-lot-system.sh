#!/bin/bash

# Get the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed. Please install Java 17+."
    exit 1
fi

# Run the application
java -jar "$SCRIPT_DIR/parking-lot-system.jar" "$@"
