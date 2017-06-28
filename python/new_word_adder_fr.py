# Corpus taken from http://wortschatz.uni-leipzig.de/en/download

import random
import re

import requests

from settings import LANGUAGE_TOOL_URL
from wiktionnary import is_in_wiktionnary


def is_spelling_mistake(word):
    params = {'text': word, 'language': 'fr'}
    response = requests.get(LANGUAGE_TOOL_URL, params=params)
    matches = response.json()['matches']

    return _has_spelling_match(matches)


def _has_spelling_match(matches):
    return any(
        r in ('HUNSPELL_NO_SUGGEST_RULE', 'HUNSPELL_RULE') or r.startswith('MORFOLOGIK_RULE_')
        for r in [match['rule']['id'] for match in matches]
    )


def validate_word(word):
    if word.lower() != word:
        return

    if len(word) <= 2:
        return

    if not re.match(r'^[a-zàâçéèêëîïôûùüÿæœ-]+$', word):
        return

    if not word.replace('-', ''):
        return

    if word.startswith('-') or word.endswith('-'):
        return

    return word


def _handle_line(line):
    word = line.split('\t')[1]
    word = validate_word(word)
    if word and is_spelling_mistake(word):
        if is_in_wiktionnary(word):
            print(word)
            return word


def add_new_words(file_path):
    with open(file_path, 'r') as word_file:
        for line in word_file:
            _handle_line(line)


def pick_words(file_path, min_count, sample_size=10):
    lines = []
    with open(file_path, 'r') as word_file:
        lines = [l for l in word_file if int(l.split('\t')[2]) >= min_count]
        #  lines = [l for l in word_file if int(l.split('\t')[2]) == min_count]

    print('Got %i lines of occurences >= %i' % (len(lines), min_count))
    #  print('Got %i lines of occurences = %i' % (len(lines), min_count))

    words = []
    random.shuffle(lines)
    for line in lines:
        word = _handle_line(line)
        if word and word.lower() == word:
            words.append(word)

        if sample_size and len(words) >= sample_size:
            break

    if sample_size:
        print(words)
    else:
        print(len(words))

    return words


if __name__ == '__main__':
    #  add_new_words('./fra_wikipedia_2010_10K-words.txt')
    #  add_new_words('./fra_wikipedia_2010_1M-words.txt')
    #  pick_words('./fra_wikipedia_2010_1M-words.txt', 10, 15)
    pick_words('./fra_wikipedia_2010_1M-words.txt', 1, None)
