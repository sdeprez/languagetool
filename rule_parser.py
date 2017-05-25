import xml.etree.ElementTree as ET


def get_rules(language):
    tree = ET.parse(
        './languagetool-language-modules/%s/src/main/resources/'
        'org/languagetool/rules/%s/grammar.xml' % (language, language)
    )

    rules = []

    def add_rule(rule, rule_id, category_id):
        str_message = ET.tostring(rule.find('./message'), method='text', encoding='utf-8')
        message = str_message.decode('utf-8').strip()
        rules.append({
            'id': rule_id,
            'message': message,
            'category_id': category_id,
        })

    for category in tree.findall('category'):
        category_id = category.attrib['id']

        for rule in category.findall('.//rule[@id]'):
            add_rule(rule, rule.attrib['id'], category_id)

        for rule_group in category.findall('.//rulegroup'):
            rule_id = rule_group.attrib['id']
            for rule in rule_group.findall('./rule'):
                add_rule(rule, rule_id, category_id)

    return rules
