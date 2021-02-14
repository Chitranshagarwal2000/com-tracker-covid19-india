#!/bin/bash

echo "Killing process on port 8080"
kill -9 $(lsof -t -i:5000)
echo "Done killing the process on port 8080"