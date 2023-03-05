# i caved and wrote it in python :sob:
# we need a JIT compiler for rust

import bs4
from pathlib import Path

INPUT_DIR = ".doc"
OUTPUT_DIR = ".doc"
IGNORED_SUPERCLASSES = ["java.lang.Object", "java.lang.Enum"]
MODULE = "paintingcanvas"


def load_superclass_methods(path: Path) -> bs4.element.Tag:
    print(f" │ Loading {path}")
    raw = open(path, "r", encoding="utf-8").read()
    soup = bs4.BeautifulSoup(raw, "html.parser")

    sections = soup.select(".memberSummary")
    methods = sections[-1]
    for i in methods.select("caption"):
        i.decompose()

    return methods


def main():
    superclass_cache = {}

    for i in Path(INPUT_DIR).rglob("*.html"):
        print(f"[*] Processing {i}")
        raw = open(i, "r", encoding="utf-8").read()
        soup = bs4.BeautifulSoup(raw, "html.parser")

        for j in soup.select("[id*='methods.inherited.from.class.']"):
            id = j["id"].split("methods.inherited.from.class.")[1]
            if id in IGNORED_SUPERCLASSES:
                continue

            tmp = j.parent.select("h3 > a")
            if len(tmp) == 0:
                continue

            superclass_def = tmp.pop()["href"]
            if superclass_def not in superclass_cache:
                superclass_doc_path = i.parent / superclass_def
                superclass_methods = load_superclass_methods(
                    superclass_doc_path)
                superclass_cache[superclass_def] = superclass_methods

            j.parent.select("code")[0].replace_with(
                superclass_cache[superclass_def])

        output_path = OUTPUT_DIR + i.as_posix().split(INPUT_DIR)[1]
        print(f" └ Writing {output_path}")
        Path(output_path).parent.mkdir(parents=True, exist_ok=True)
        open(output_path, "w", encoding="utf-8").write(str(soup))


# yeah, i guess you could say im a master of python
if __name__ == '__main__':
    main()
