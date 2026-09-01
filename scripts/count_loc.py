import os

EXCLUDED_DIRS = {".git", "node_modules", "dist", "build", "target", ".idea", ".vscode"}
VALID_EXTS = {".java", ".ts", ".tsx", ".js", ".jsx", ".sql", ".json", ".md", ".css", ".html", ".py", ".sh", ".yml", ".yaml"}

def count_lines(filepath):
    try:
        with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
            return sum(1 for _ in f)
    except Exception:
        return 0

def main():
    total_lines = 0
    file_counts = {}

    for root, dirs, files in os.walk(os.getcwd()):
        dirs[:] = [d for d in dirs if d not in EXCLUDED_DIRS]
        for file in files:
            ext = os.path.splitext(file)[1]
            if ext in VALID_EXTS:
                filepath = os.path.join(root, file)
                lines = count_lines(filepath)
                total_lines += lines
                file_counts[ext] = file_counts.get(ext, 0) + lines

    print("=== LOC Summary ===")
    for ext, lines in sorted(file_counts.items(), key=lambda x: x[1], reverse=True):
        print(f"  {ext}: {lines:,} lines")
    print(f"Total Lines of Code: {total_lines:,}")

if __name__ == "__main__":
    main()
