import os

GOOGLE = {
    'version': 'v1',
    'cx': '005962531539652225874:yezvmoey-wk',
    'key': os.getenv('GOOGLE_KEY', ''),
}


LANGUAGE_TOOL_URL = 'http://localhost:8081/v2/check'

DATABASE = {
    'NAME': 'languagetool',
    'USERNAME': '',
    'PASSWORD': '',
    'HOST': 'localhost',
}


try:
    from local_settings import *  # pylint: disable=wildcard-import,wrong-import-position
except ImportError:
    pass
