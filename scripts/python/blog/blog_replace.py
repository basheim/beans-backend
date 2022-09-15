#!/usr/bin/python3

import sys
import getopt
import csv
import requests
import os
import markdown
from datetime import datetime

base_url = 'https://backend.programmingbean.com'
post_delete_url = '/api/v1/posts'
api_user = 'admin'
api_password = os.getenv('API_PASSWORD')


def main(argv):
    # get the file arg
    (input_file, md_directory, to_delete) = get_args(argv)
    # set up the session
    session = requests.session()
    session.auth = (api_user, api_password)
    # delete all data and start over
    if to_delete:
        session.delete(url=base_url + post_delete_url)

    # write the data to the db
    with open(input_file, newline='') as csv_file:
        reader = csv.DictReader(csv_file)
        raw_data = []
        for raw_row in reader:
            raw_data.append(raw_row)
        raw_data.sort(key=lambda r: datetime.strptime(r['createdDate'], '%Y-%m-%dT%H:%M:%S.%f%z'))
        for row in raw_data:
            md_path = md_directory + '/' + row['content']
            with open(md_path, 'r') as md_file:
                content = markdown.markdown(md_file.read())
            data = {
                "title": row['title'],
                "id": row['id'],
                "description": row['description'],
                "author": row['author'],
                "tags": row['tags'],
                "content": content,
                "prev": row['prev'],
                "next": row['next'],
                "createdDate": row['createdDate']
            }
            session.post(url=base_url + post_delete_url, json=data)


def get_args(argv):
    input_file = None
    md_directory = None
    to_delete = False
    try:
        opts, args = getopt.getopt(argv,"hdi:j:")
    except getopt.GetoptError:
        print('blog_replace.py -i opt:<input file> -j opt:<md directory> -d <delete all data>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('blog_replace.py -i opt:<input file> -j opt:<md directory> -d <delete all data>')
            sys.exit()
        elif opt == "-i":
            input_file = arg
        elif opt == "-j":
            md_directory = arg
        elif opt == "-d":
            to_delete = True
    if not input_file:
        print('-i is required')
        sys.exit()
    if not md_directory:
        print('-j is required')
        sys.exit()
    return input_file, md_directory, to_delete


if __name__ == "__main__":
    main(sys.argv[1:])
