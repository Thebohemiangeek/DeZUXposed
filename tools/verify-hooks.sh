#!/usr/bin/env bash
# Check that every class/method this module hooks exists in a firmware's APKs
# and jars. Usage: tools/verify-hooks.sh DIR_WITH_APKS_AND_JARS
# Pull the files from a device with adb (see docs/ROADMAP.md) or extract them
# from a firmware package. Prints one line per hook target: ok / MISSING.
set -uo pipefail

SRC_DIR=$(cd "$(dirname "$0")/../app/src/main/java" && pwd)
BIN="$1"
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

# Dump the string tables of every dex into one searchable file.
for f in "$BIN"/*.apk "$BIN"/*.jar; do
  [ -f "$f" ] || continue
  unzip -p "$f" 'classes*.dex' 2>/dev/null | LC_ALL=C grep -aoE '[[:print:]]{3,}' >> "$work/strings.txt"
done
sort -u "$work/strings.txt" -o "$work/strings.txt"

# Pull (class, method) pairs out of every hook call in the Kotlin sources,
# including calls split over several lines.
find "$SRC_DIR" -name '*.kt' -exec cat {} + | tr '\n' ' ' \
  | grep -oE '(replaceMethod|beforeMethod|afterMethod)\( *"[^"]+", *"[^"]+"' \
  | sed -E 's/.*\( *"([^"]+)", *"([^"]+)"/\1 \2/' | sort -u > "$work/targets.txt"

ok=0
missing=0
while read -r cls method; do
  desc="L$(echo "$cls" | tr '.' '/');"
  if grep -qxF "$desc" "$work/strings.txt" || grep -qF "$desc" "$work/strings.txt"; then
    if grep -qxF "$method" "$work/strings.txt"; then
      echo "ok       $cls#$method"; ok=$((ok + 1))
    else
      echo "MISSING  $cls#$method (method name not found)"; missing=$((missing + 1))
    fi
  else
    echo "MISSING  $cls#$method (class not found)"; missing=$((missing + 1))
  fi
done < "$work/targets.txt"

echo
echo "$ok found, $missing missing"
[ "$missing" -eq 0 ]
