# i caved and wrote it in python :sob:
# we need a JIT compiler for rust

from typing import Tuple
import bs4
from pathlib import Path

INPUT_DIR = "build/docs/javadoc"
OUTPUT_DIR = "build/docs/javadoc"
IGNORED_SUPERCLASSES = ["java.lang.Object", "java.lang.Enum"]

# Return the method and field sections of a superclass, loading it if necessary
def process_doc_section(superclass_cache: dict, i: Path, j: bs4.element.Tag, id: str):
    id = j["id"].split(id)[1]
    if id in IGNORED_SUPERCLASSES:
        return None

    tmp = j.parent.select("h3 > a")
    if len(tmp) == 0:
        return None

    superclass_def = tmp.pop()["href"]
    if superclass_def not in superclass_cache:
        superclass_doc_path = i.parent / superclass_def
        superclass_defs = load_superclass_methods(
            superclass_doc_path)
        superclass_cache[superclass_def] = superclass_defs

    return superclass_cache[superclass_def]


# Load a superclass and return its methods and field sections
def load_superclass_methods(path: Path) -> Tuple[bs4.element.Tag, bs4.element.Tag]:
    print(f" │ Loading {path}")
    raw = open(path, "r", encoding="utf-8").read()
    soup = bs4.BeautifulSoup(raw, "html.parser")

    sections = soup.select(".memberSummary")
    methods = sections[-1]
    for i in methods.select("caption"):
        i.decompose()

    fields = None
    if len(sections) >= 2:
        fields = sections[0]
        for i in fields.select("caption"):
            i.decompose()

    return (methods, fields)


def main():
    superclass_cache = {}

    for i in Path(INPUT_DIR).rglob("*.html"):
        print(f"[*] Processing {i}")
        raw = open(i, "r", encoding="utf-8").read()
        soup = bs4.BeautifulSoup(raw, "html.parser")

        # Patch the Inherited Methods section
        for j in soup.select("[id*='methods.inherited.from.class.']"):
            superclass_def = process_doc_section(
                superclass_cache, i, j, "methods.inherited.from.class.")
            if superclass_def is None:
                continue
            j.parent.select("code")[0].replace_with(superclass_def[0])

        # Patch the Inherited Fields section
        for j in soup.select("[id*='fields.inherited.from.class.']"):
            superclass_def = process_doc_section(
                superclass_cache, i, j, "fields.inherited.from.class.")
            if superclass_def is None:
                continue
            j.parent.select("code")[0].replace_with(superclass_def[1])

        # Write the patched file
        output_path = OUTPUT_DIR + i.as_posix().split(INPUT_DIR)[1]
        print(f" └ Writing {output_path}")
        Path(output_path).parent.mkdir(parents=True, exist_ok=True)
        open(output_path, "w", encoding="utf-8").write(str(soup))


# yeah, i guess you could say im a master of python
if __name__ == '__main__':
    main()
