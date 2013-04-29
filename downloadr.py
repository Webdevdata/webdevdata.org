#!/usr/bin/python

import os
from time import gmtime, strftime
import sys
from urllib2 import HTTPError, URLError, urlopen
from multiprocessing.pool import ThreadPool
import hashlib

curdate = strftime("%Y%m%d%H%M%S", gmtime())
os.mkdir(curdate)
os.chdir(curdate)
pool = ThreadPool(processes = 64)

def downloadFile(url):
    url = url.strip()
    try: 
        print "Downloading: ",  "http://" + url
        f = urlopen("http://" + url)
        hash = hashlib.md5()
        hash.update(url)
        dir = hash.hexdigest()[:2]
        print "dir", dir
        if not os.path.exists(dir):
            os.mkdir(dir)
        with open(dir + "/" + url + ".html.txt", "wb") as local_file:
            local_file.write(f.read())
            local_file.close()
        with open(dir + "/" + url + ".html.hdr.txt", "wb") as local_file:
            local_file.write(str(f.getcode()) + "\n" + str(f.info()))
            local_file.close()
    except HTTPError, e:
        print "HTTPError:", e.code, url
    except URLError, e:
        print "URLError:", e.reason, url

for url in sys.stdin.xreadlines():
    pool.map_async(downloadFile, [url, ])

pool.close()
pool.join()
