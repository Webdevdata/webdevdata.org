This branch contains the scripts used to fetch the HTML files from top
Alexa sites.

The method used is the following:

* The top 1 million Alexa sites
[csv](http://s3.amazonaws.com/alexa-static/top-1m.csv.zip) is
downloaded, unzipped, and the URLs are extracted from it
* The URLs are then fed to a Python script that downloads the HTML files
and their HTTP headers using a thread pool (to minimize waiting). 

The resulting directory structure is:
 
* A root directory of the pattern "webdevdata.org-YYYY-MM-DD-HHMMSS"
* Sub-directories are 16 bit hashes of the URLs below them. Used to
verify there are not toom many files in a single directory. 

The resulting files have an ".html.txt" extension for the data files and
".html.hdr.txt" extension for the header files.
