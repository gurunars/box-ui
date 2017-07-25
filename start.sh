#!/bin/bash

google-chrome http://localhost:8000 &
cd html-docs && python -m SimpleHTTPServer
