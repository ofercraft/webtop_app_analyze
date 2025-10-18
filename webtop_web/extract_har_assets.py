#!/usr/bin/env python3
"""
extract_har_assets.py

Usage:
    python extract_har_assets.py capture.har out_dir [--types html,js,css] [--index index.csv]

Extracts assets from HAR `log.entries[*].response.content.text`.
By default extracts text/html, text/css, application/javascript, application/x-javascript, and common JS mime types.
"""

import sys
import os
import json
import base64
import csv
import argparse
import pathlib
from urllib.parse import urlparse, unquote
import mimetypes
import re
from datetime import datetime
import hashlib

# Default MIME patterns to extract
DEFAULT_MIME_KEYWORDS = [
    "text/html",
    "text/css",
    "javascript",               # catches application/javascript, text/javascript
    "application/javascript",
    "application/x-javascript",
    "application/ecmascript",
    "text/javascript",
]

def parse_args():
    p = argparse.ArgumentParser(description="Extract HTML/JS/CSS (or other) files from a HAR file.")
    p.add_argument("har", help="Path to HAR file (JSON).")
    p.add_argument("outdir", help="Output directory where extracted files will be written.")
    p.add_argument("--types", default="html,js,css",
                   help="Comma-separated friendly types for naming index and basic filter hint (not used for mime matching).")
    p.add_argument("--index", default="index.csv", help="CSV index filename (written into outdir).")
    p.add_argument("--mime-filter", default=None,
                   help="Optional custom comma-separated MIME substrings to match (overrides default). Example: 'text/html,text/css,application/json'")
    return p.parse_args()

def safe_path_component(s: str) -> str:
    # remove characters unsafe for filenames, keep unicode letters/digits, dash, underscore, dot
    s = re.sub(r"[<>:\"/\\|?*\x00-\x1F]", "_", s)
    s = re.sub(r"\s+", "_", s)
    # collapse multiple underscores
    s = re.sub(r"_+", "_", s)
    return s.strip("._") or "_"

def guess_extension_from_mime(mime: str):
    if not mime:
        return ""
    mime = mime.split(";")[0].strip().lower()
    # try std lib
    ext = mimetypes.guess_extension(mime) or ""
    if ext:
        return ext
    # fallback mapping
    if "javascript" in mime:
        return ".js"
    if "css" in mime:
        return ".css"
    if "html" in mime:
        return ".html"
    if "json" in mime:
        return ".json"
    return ""

def make_output_path(outdir: pathlib.Path, url: str, mime: str):
    """
    Create an output file path under outdir following:
      outdir/hostname/<path_with_dirs_or_root>/filename.ext
    If the URL path ends with '/', file will be named 'index.<ext>' or 'root.<ext>'
    Ensures directories exist but does NOT create the file yet.
    """
    p = urlparse(url)
    hostname = safe_path_component(p.hostname or "host")
    path = unquote(p.path or "/")
    # treat query hash in filename if path ends with file, else put query into filename
    path_components = [c for c in path.split("/") if c and c != "."]
    # If last component looks like a filename with extension, use it; otherwise use index.ext
    if path.endswith("/") or not path_components:
        file_name = "index"
        dir_parts = path_components
    else:
        file_name = path_components[-1]
        dir_parts = path_components[:-1]

    file_name = safe_path_component(file_name)
    # if file has no extension, try to add one from mime
    ext = pathlib.Path(file_name).suffix
    if not ext:
        ext = guess_extension_from_mime(mime) or ""
        file_name = file_name + ext

    # If query present and filename would collide or be too generic, add a hash of query
    if p.query:
        qhash = hashlib.sha1(p.query.encode("utf-8")).hexdigest()[:10]
        name_noext = pathlib.Path(file_name).stem
        ext = pathlib.Path(file_name).suffix
        file_name = f"{name_noext}__q-{qhash}{ext}"

    # Build path: outdir/hostname/dir_parts.../file_name
    target_dir = outdir / hostname
    for comp in dir_parts:
        target_dir = target_dir / safe_path_component(comp)
    target_dir.mkdir(parents=True, exist_ok=True)

    # ensure uniqueness
    candidate = target_dir / file_name
    i = 1
    stem = candidate.stem
    suffix = candidate.suffix
    while candidate.exists():
        candidate = target_dir / f"{stem}_{i}{suffix}"
        i += 1
    return candidate

def decode_content(text: str, encoding: str):
    if text is None:
        return None
    if encoding and encoding.lower() == "base64":
        try:
            return base64.b64decode(text)
        except Exception:
            # some HAR exporters may include whitespace/newlines etc
            try:
                return base64.b64decode(text.encode("utf-8", errors="ignore"))
            except Exception:
                return None
    # If not base64, HAR stores the text (possibly already unicode) — return bytes encoded as utf-8
    try:
        return text.encode("utf-8")
    except Exception:
        return text if isinstance(text, (bytes, bytearray)) else str(text).encode("utf-8", errors="ignore")

def main():
    args = parse_args()
    har_path = pathlib.Path(args.har)
    outdir = pathlib.Path(args.outdir)
    outdir.mkdir(parents=True, exist_ok=True)
    index_path = outdir / args.index

    if args.mime_filter:
        mime_keywords = [m.strip().lower() for m in args.mime_filter.split(",") if m.strip()]
    else:
        mime_keywords = DEFAULT_MIME_KEYWORDS

    # Load HAR
    with open(har_path, "r", encoding="utf-8") as f:
        har = json.load(f)

    entries = har.get("log", {}).get("entries", [])
    if not entries:
        print("No entries found in HAR 'log.entries'. Exiting.")
        return

    rows = []
    saved_count = 0
    for idx, e in enumerate(entries):
        try:
            req = e.get("request", {})
            resp = e.get("response", {})
            url = req.get("url") or e.get("request", {}).get("url") or resp.get("url") or ""
            status = resp.get("status")
            startedDateTime = e.get("startedDateTime")
            content = resp.get("content", {}) or {}
            mimeType = (content.get("mimeType") or "").lower()
            text = content.get("text")
            encoding = content.get("encoding")  # e.g. "base64"
            # skip if no text content recorded
            if text is None:
                continue

            # decide whether to save based on mime keywords
            if not any(mk in mimeType for mk in mime_keywords):
                # sometimes mimeType may be empty: fallback to file extension from URL
                ext_guess = pathlib.Path(urlparse(url).path).suffix.lower()
                # if ext_guess not in (".js", ".css", ".html", ".htm"):
                #     continue

            data = decode_content(text, encoding)
            if data is None:
                continue

            out_path = make_output_path(outdir, url, mimeType)
            # write bytes
            try:
                with open(out_path, "wb") as of:
                    of.write(data)
            except Exception as ex:
                print(f"Failed to write {out_path}: {ex}")
                continue

            saved_count += 1
            rows.append({
                "index": idx,
                "url": url,
                "status": status,
                "mimeType": mimeType,
                "out_path": str(out_path.relative_to(outdir)),
                "startedDateTime": startedDateTime or "",
                "bodySize": content.get("size") or "",
            })
            print(f"[{saved_count}] Saved {url} -> {out_path}  (mime: {mimeType})")
        except Exception as exc:
            print("Error processing entry:", exc)
            continue

    # write CSV index
    if rows:
        with open(index_path, "w", newline="", encoding="utf-8") as csvf:
            fieldnames = ["index", "url", "status", "mimeType", "out_path", "startedDateTime", "bodySize"]
            writer = csv.DictWriter(csvf, fieldnames=fieldnames)
            writer.writeheader()
            for r in rows:
                writer.writerow(r)

    print(f"Done. Extracted {saved_count} files to {outdir}")
    if rows:
        print(f"Index written to {index_path}")

if __name__ == "__main__":
    main()
