#!/bin/bash

### Some examples ### 

# Query on tag <nav> :
# 	- total instances
#   	- number of pages where the instances are present
java -cp .:lib/* org.opens.webdevdataextractor.HtmlCounter /path/to/webdevdata/webdevdata.org-2013-06-18-124603/ nav 

# Query on tags <nav> <header> <footer> <article> <section>, for each tag :
# 	- total instances
#	- number of pages where the instances are present
#java -cp .:lib/* org.opens.webdevdataextractor.HtmlCounter /path/to/webdevdata/webdevdata.org-2013-06-18-124603/ nav header footer article section

# Query on tags <h1> <h2> with URIs they appear in, for each tag :
#	- total instances
#	- number of pages where the instances are present
# 	- URI list with number of instances per each (descending order)
#java -cp .:lib/* org.opens.webdevdataextractor.HtmlCounter --withURIs /path/to/webdevdata/webdevdata.org-2013-06-18-124603/ h1 h2

# Query on tags <img> without alt attribute with URIs they appear in
#	- total instances
#	- number of pages where the instances are present
#	- URI list with number of instances per each (descending order)
#java -cp .:lib/* org.opens.webdevdataextractor.HtmlCounter --withURIs /path/to/webdevdata/webdevdata.org-2013-06-18-124603/ img:not([alt])

