This branch contains the scripts used to fetch the HTML files from top
Alexa sites.

The method used is the following:

* The top 1 million Alexa sites
[csv](http://s3.amazonaws.com/alexa-static/top-1m.csv.zip) is
downloaded, unzipped, and the URLs are extracted from it
* The URLs are then fed to a Python script that downloads the HTML files
using a thread pool (to minimize waiting) and their headers.
* The resulting directory structure is a root directory named with
current date & time. Sub directories are 256bit hashes of the URLs below
them, verifying there aren't too many files per directory (to avoid
slowing down the file system)
