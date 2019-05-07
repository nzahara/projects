import csv
import json
import io


def json_dump(inp):
    reader = csv.DictReader(io.StringIO(inp), fieldnames=("state", "color"))
    # Parse the CSV into JSON
    out = json.dumps([row for row in reader])
    return out

