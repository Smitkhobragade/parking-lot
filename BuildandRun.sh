#!/bin/bash
echo "Compiling Java files..."
javac -d bin -cp "lib/*" src/main.java src/config/*.java src/service/*.java

echo "Creating JAR file..."
jar cvfm parking-lot-system.jar MANIFEST.MF -C bin .

echo "Running JAR file..."
java -jar parking-lot-system.jar