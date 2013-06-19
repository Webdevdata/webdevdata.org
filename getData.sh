#!/bin/sh

URLS=/tmp/urls.txt
if [ ! -f $URLS ] 
then
    curl http://s3.amazonaws.com/alexa-static/top-1m.csv.zip | zcat | awk -F',' '{print $2}' > $URLS
fi
DIR=`./downloadr.py create`
cat $URLS | xargs -I{} -n 1 -P64 ./downloadr.py download {} $DIR
