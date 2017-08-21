# pylint: disable=no-member

from googleapiclient.discovery import build

from models import GoogleQuery
from settings import GOOGLE


def has_google_spelling_correction(word):
    result = _get_result(word)
    correction = result.get('spelling', {}).get('correctedQuery')
    if correction:
        print('Correction for "%s": "%s"' % (word, correction))
        return True

    #  for item in result['items']:
        #  if item['link'].startswith('https://fr.wiktionary.org/wiki/'):

    return False


def _get_result(word):
    cx_id = GOOGLE['cx']
    try:
        query = GoogleQuery.objects.get(query=word, cx=cx_id)
    except GoogleQuery.DoesNotExist:
        query = GoogleQuery(query=word, cx=cx_id)
        query.result = _make_query(word, cx_id)
        query.save()

    return query.result


def _clean_dot_in_key(result):
    def fix_key(k):
        return k.replace('.', '-') if isinstance(k, str) else k

    if isinstance(result, dict):
        return {fix_key(k): _clean_dot_in_key(v) for k, v in result.items()}

    if isinstance(result, list):
        return [_clean_dot_in_key(k) for k in result]

    return result


def _make_query(word, cx_id):
    service = build('customsearch', GOOGLE['version'], developerKey=GOOGLE['key'])
    result = service.cse().list(q=word, cx=cx_id).execute()
    return _clean_dot_in_key(result)
