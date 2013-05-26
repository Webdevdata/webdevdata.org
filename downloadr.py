#!/usr/bin/python

import os
from time import gmtime, strftime
import sys
from urllib2 import HTTPError, URLError, urlopen
from multiprocessing.pool import ThreadPool
import hashlib
import magic

curdate = "webdevdata.org-" + strftime("%Y-%m-%d-%H%M%S", gmtime())
os.mkdir(curdate)
os.chdir(curdate)
pool = ThreadPool(processes = 64)

def downloadFile(url):
    url = url.strip()
    try: 
        print "Downloading: ", url
        if url.startswith("http://"):
            url = url[7:]
        urlhost = url.split("/")[0]
        urlpath = "/".join(url.split("/")[1:])
        f = urlopen("http://" + url)
        hash = hashlib.md5()
        hash.update(url)
        dir = hash.hexdigest()[:2]
        if not os.path.exists(dir):
            os.mkdir(dir)
        buffer = f.read()
        ext = magic.from_buffer(buffer).split()[0].lower()
        if "html" in ext:
            ext = "html.txt"
        filename = dir + "/" + urlhost + "_" + hash.hexdigest() + "." + ext
        with open(filename, "wb") as local_file:
            local_file.write(buffer)
            local_file.close()
        with open(filename + ".hdr.txt", "wb") as local_file:
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
