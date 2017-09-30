#!/bin/bash

# Find all page ids of pages containing a link to categories "English_*"
time grep -Po "\(\K\d+,'English_[^']+','[^']*','[^']*','[^']*','uppercase','page'\)" enwiktionary-20170920-categorylinks.sql | cut -d "," -f 1 - | uniq > wiktionnary_en_words_page_id.txt && say "done"


# Output a mapping id,page_title (one per line)
time cat enwiktionary-20170920-page.sql | grep -Po "\(\K\d+,0,'[\p{L}-]+'" | cut -d "," -f 1,3 - > wiktionnary_en_words_id_to_title.txt
