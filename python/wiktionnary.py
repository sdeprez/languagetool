import sys


def get_word_set(lang):
    print('Getting wiktionnary %s words' % lang)
    file_name = '%s/wiktionnary_%s_words.txt' % (lang, lang)
    with open(file_name, 'r') as word_file:
        return [l.strip() for l in word_file]


def is_in_wiktionnary(word, lang):
    func = is_in_wiktionnary
    if not hasattr(func, 'words'):
        func.words = get_word_set(lang)

    return word in func.words


def read_ids(lang):
    file_name = '%s/wiktionnary_%s_words_page_id.txt' % (lang, lang)
    with open(file_name, 'r') as id_file:
        for line in id_file:
            yield line.strip()


def get_id_title_mapping(lang):
    mapping = {}

    file_name = '%s/wiktionnary_%s_words_id_to_title.txt' % (lang, lang)
    with open(file_name, 'r') as mapping_file:
        for line in mapping_file:
            page_id, title = line.split(',')
            mapping[page_id] = title.strip().strip('\'')

    return mapping


def get_words(lang):
    from new_word_adder import validate_word
    words = []

    mapping = get_id_title_mapping(lang)

    for page_id in list(read_ids(lang)):
        if page_id not in mapping:
            continue

        word = validate_word(mapping[page_id])
        if word:
            words.append(word)

    file_name = '%s/wiktionnary_%s_words.txt' % (lang, lang)
    with open(file_name, 'w') as words_file:
        for word in words:
            words_file.write(word + '\n')


if __name__ == '__main__':
    get_words(sys.argv[1])
