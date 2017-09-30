# Corpus taken from http://wortschatz.uni-leipzig.de/en/download

import random
import re
import sys

import requests

from settings import LANGUAGE_TOOL_URL
from wiktionnary import is_in_wiktionnary


def is_spelling_mistake(word, lang):
    languages = {
        'fr': 'fr',
        'en': 'en-US',
    }
    params = {'text': word, 'language': languages[lang]}
    response = requests.get(LANGUAGE_TOOL_URL, params=params)
    matches = response.json()['matches']

    return _has_spelling_match(matches)


def _has_spelling_match(matches):
    return any(
        r in ('HUNSPELL_NO_SUGGEST_RULE', 'HUNSPELL_RULE') or r.startswith('MORFOLOGIK_RULE_')
        for r in [match['rule']['id'] for match in matches]
    )


def validate_word(word):
    if not word or len(word) <= 2:
        return

    if word.upper() == word:
        word = word.lower()

    if not re.match(r'^[a-zàâçéèêëîïôûùüÿæœ-]+$', word):
        return

    if not word.replace('-', ''):
        return

    if word.startswith('-') or word.endswith('-'):
        return

    return word


def _handle_line(line, lang):
    word = line.split('\t')[1]
    word = validate_word(word)
    if word and is_spelling_mistake(word, lang):
        if is_in_wiktionnary(word, lang):
            print(word)
            return word


def add_new_words(lang):
    file_path = './%s/%s_wikipedia_2016_1M-words.txt' % (lang, lang)
    with open(file_path, 'r') as word_file:
        for line in word_file:
            _handle_line(line, lang)


if __name__ == '__main__':
    add_new_words(sys.argv[1])
