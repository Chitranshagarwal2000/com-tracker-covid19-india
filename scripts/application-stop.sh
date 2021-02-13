#!/bin/bash
set -e
echo "Trying to kill application, checking port 5000"
echo $(lsof -i:5000)
kill $(lsof -i:5000 -t)