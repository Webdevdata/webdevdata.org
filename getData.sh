#!/bin/sh

curl http://s3.amazonaws.com/alexa-static/top-1m.csv.zip | zcat | awk -F',' '{print $2}' > /tmp/urls.txt
cat /tmp/urls.txt | ./downloadr.py
