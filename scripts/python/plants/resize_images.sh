#!/bin/bash

dest="./data/images"
src="./data/raw_images"
file_names=`ls ./data/raw_images/*.jpg`

rm -rf "$dest" || true
mkdir "$dest"
for entry in $file_names
do
  echo "$entry"
  cp "$entry" "$dest"
  temp_name="$dest/$(basename $entry)"
  percent=90
  while [ "$percent" -gt 10 ] && [ "$(ls -s $temp_name | grep -o '[0-9]*')" -gt 250 ]
  do
    convert "$entry" -quality "$percent"% "$temp_name"
    percent=$((percent-5))
  done
done