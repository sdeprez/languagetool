from datetime import datetime

import mongoengine
from settings import DATABASE


def connect_to_database():
    mongoengine.connect(
        db=DATABASE['NAME'],
        username=DATABASE['USERNAME'],
        password=DATABASE['PASSWORD'],
        host=DATABASE['HOST'],
    )


class BaseDocument(mongoengine.Document):

    meta = {
        'abstract': True,
    }

    created_at = mongoengine.DateTimeField()
    updated_at = mongoengine.DateTimeField(default=datetime.now)

    def save(self, *args, **kwargs):
        now = datetime.now()
        if not self.created_at:
            self.created_at = now
        self.updated_at = now

        super().save(*args, **kwargs)


class GoogleQuery(BaseDocument):

    query = mongoengine.StringField(required=True, unique_with='cx')
    cx = mongoengine.StringField(required=True)
    result = mongoengine.DictField()


connect_to_database()
