#!/usr/bin/python3

import sys
import getopt
import csv
import requests
import os
import uuid
import random
from datetime import date, datetime, timedelta

base_url = 'http://localhost:8080'
post_delete_url = '/api/v1/plants'
get_latest_date_url = '/api/v1/plants/latestDate'
upload_image_url = '/api/v1/plants/image/save'
api_user = 'admin'
api_password = os.getenv('API_PASSWORD')


def main(argv):
    start_date = datetime.combine(date.today(), datetime.min.time())
    # get the file arg
    (input_file, image_directory, to_delete) = get_args(argv)
    # set up the session
    session = requests.session()
    session.auth = (api_user, api_password)
    # delete all data and start over
    if to_delete:
        session.delete(url=base_url + post_delete_url)
    else:
        res = session.get(url=base_url + get_latest_date_url)
        start_date = datetime.combine(datetime.strptime(res.json()['latestDate'], '%Y-%m-%dT%H:%M:%S.%f%z').date(), datetime.min.time())
    start_date = start_date.astimezone(None)
    # write the data to the db
    with open(input_file, newline='') as csv_file:
        reader = csv.DictReader(csv_file)
        date_counter = 0
        raw_data = []
        for raw_row in reader:
            raw_data.append(raw_row)
        random.shuffle(raw_data)
        for row in raw_data:
            image_path = image_directory + '/' + row['image']
            with open(image_path, 'rb') as image_file:
                data = {'file': image_file}
                image_response = session.post(url=base_url + upload_image_url, files=data)
                print(str(image_response.json()))
                image_url = image_response.json()['url']
            data = {
                'id': str(uuid.uuid4()),
                'english': row['english'],
                'latin': row['latin'],
                'edibility': row['edibility'],
                'imageUrl': image_url,
                'poisonousLookAlike': row['poisonousLookAlike'],
                'foundNear': row['foundNear'],
                'keyFeatures': row['keyFeatures'],
                'start': start_date.isoformat(),
                'end': (start_date + timedelta(days=1)).isoformat()
            }
            start_date = datetime.combine((start_date + timedelta(days=1)).date(), datetime.min.time()).astimezone(None)
            session.post(url=base_url + post_delete_url, json=data)


def get_args(argv):
    input_file = None
    image_directory = None
    to_delete = False
    try:
        opts, args = getopt.getopt(argv,"hdi:j:")
    except getopt.GetoptError:
        print('plant_replace.py -i opt:<input file> -j opt:<image directory> -d <delete all data>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('plant_replace.py -i opt:<input file> -j opt:<image directory> -d <delete all data>')
            sys.exit()
        elif opt == "-i":
            input_file = arg
        elif opt == "-j":
            image_directory = arg
        elif opt == "-d":
            to_delete = True
    if not input_file:
        print('-i is required')
        sys.exit()
    if not image_directory:
        print('-j is required')
        sys.exit()
    return input_file, image_directory, to_delete


if __name__ == "__main__":
    main(sys.argv[1:])
