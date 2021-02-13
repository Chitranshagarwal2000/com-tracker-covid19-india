#!/bin/bash
set -e
echo "Trying to kill application, checking port 5000"
#pid=`lsof -i:5000`
#if [[ -n $pid ]]; then
#  echo "$pid"
#  kill $pid
#fi