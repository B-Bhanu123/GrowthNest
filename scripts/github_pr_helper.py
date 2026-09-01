import sys
import json
import urllib.request
import subprocess

def get_github_token():
    try:
        proc = subprocess.run(
            ["git", "credential", "fill"],
            input="url=https://github.com\n",
            capture_output=True,
            text=True,
            check=True
        )
        for line in proc.stdout.splitlines():
            if line.startswith("password="):
                return line.split("=", 1)[1].strip()
    except Exception as e:
        print(f"Could not fetch credential from git: {e}")
    return ""

REPO = "B-Bhanu123/GrowthNest"

def make_request(url, method="GET", data=None):
    token = get_github_token()
    headers = {
        "Authorization": f"token {token}",
        "User-Agent": "Python-Git-PR-Helper",
        "Accept": "application/vnd.github.v3+json",
        "Content-Type": "application/json"
    }
    encoded_data = json.dumps(data).encode("utf-8") if data else None
    req = urllib.request.Request(url, data=encoded_data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req) as resp:
            body = resp.read().decode("utf-8")
            return json.loads(body) if body else {}
    except urllib.error.HTTPError as e:
        err_body = e.read().decode("utf-8")
        print(f"HTTP Error {e.code}: {err_body}")
        raise e

def create_and_merge_pr(head_branch, title, body="Phase completion PR"):
    pr_url = f"https://api.github.com/repos/{REPO}/pulls"
    pr_payload = {
        "title": title,
        "body": body,
        "head": head_branch,
        "base": "main"
    }
    pr_res = make_request(pr_url, method="POST", data=pr_payload)
    pr_num = pr_res.get("number")
    print(f"[SUCCESS] Created PR #{pr_num}: {pr_res.get('html_url')}")
    
    # Merge PR
    merge_url = f"https://api.github.com/repos/{REPO}/pulls/{pr_num}/merge"
    merge_payload = {
        "commit_title": f"Merge pull request #{pr_num} from {head_branch}",
        "commit_message": f"Merged phase feature branch {head_branch} into main",
        "merge_method": "squash"
    }
    merge_res = make_request(merge_url, method="PUT", data=merge_payload)
    print(f"[SUCCESS] Merged PR #{pr_num}: {merge_res.get('merged')}")

if __name__ == "__main__":
    if len(sys.argv) > 2:
        head_branch = sys.argv[1]
        title = sys.argv[2]
        body = sys.argv[3] if len(sys.argv) > 3 else "Phase PR"
        create_and_merge_pr(head_branch, title, body)
