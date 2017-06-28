#!/bin/bash

# Find all page ids of pages containing a link to Category "français"
time cat frwiktionary-20170601-categorylinks.sql | grep -Po "\(\K\d+,'français','[\p{L}-]+','[^']+','','uppercase','page'\)" | cut -d "," -f 1 -


# Output a mapping id,page_title (one per line)
time cat frwiktionary-20170601-page.sql | grep -Po "\(\K\d+,0,'[\p{L}-]+'" | cut -d "," -f 1,3 - > wiktionnary_fr_words_id_to_title.txt
