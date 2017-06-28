def get_word_set():
    print('Getting wiktionnary fr words')
    with open('wiktionnary_fr_words.txt', 'r') as word_file:
        return [l.strip() for l in word_file]


def is_in_wiktionnary(word):
    func = is_in_wiktionnary
    if not hasattr(func, 'words'):
        func.words = get_word_set()

    return word in func.words


def read_ids():
    with open('wiktionnary_fr_words_page_id.txt', 'r') as id_file:
        for line in id_file:
            yield line.strip()


def get_id_title_mapping():
    mapping = {}

    with open('wiktionnary_fr_words_id_to_title.txt', 'r') as mapping_file:
        for line in mapping_file:
            page_id, title = line.split(',')
            mapping[page_id] = title.strip().strip('\'')

    return mapping


def get_titles():
    from new_word_adder_fr import validate_word

    mapping = get_id_title_mapping()
    for page_id in read_ids():
        if page_id not in mapping:
            return

        word = mapping[page_id]
        word = validate_word(word)
        if word:
            print(word)

if __name__ == '__main__':
    get_titles()
