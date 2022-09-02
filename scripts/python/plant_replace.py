#!/usr/bin/python3

import sys
import getopt
import csv
import requests
import os
import uuid
import random
from datetime import date, datetime, timedelta, time

base_url = 'http://beans-backend-lb-282639646.us-west-2.elb.amazonaws.com:80'
post_delete_url = '/api/v1/plants'
api_user = 'admin'
api_password = os.getenv('API_PASSWORD')


def main(argv):
    # get the file arg
    (input_file, to_delete) = get_args(argv)
    # set up the session
    session = requests.session()
    session.auth = (api_user, api_password)
    # delete all data
    if to_delete:
        session.delete(url=base_url + post_delete_url)
    # write the data to the db
    with open(input_file, newline='') as csv_file:
        start_date = datetime.combine(date.today(), time.min)
        reader = csv.DictReader(csv_file)
        date_counter = 0
        raw_data = []
        for raw_row in reader:
            raw_data.append(raw_row)
        random.shuffle(raw_data)
        for row in raw_data:
            data = {
                'id': str(uuid.uuid4()),
                'english': row['english'],
                'latin': row['latin'],
                'edibility': row['edibility'],
                'imageUrl': row['imageUrl'],
                'poisonousLookAlike': row['poisonousLookAlike'],
                'foundNear': row['foundNear'],
                'keyFeatures': row['keyFeatures'],
                'start': (start_date + timedelta(days=date_counter)).isoformat(),
                'end': (start_date + timedelta(days=date_counter + 1)).isoformat()
            }
            date_counter += 1
            session.post(url=base_url + post_delete_url, json=data)


def get_args(argv):
    input_file = None
    to_delete = False
    try:
        opts, args = getopt.getopt(argv,"hdi:")
    except getopt.GetoptError:
        print('plant_replace.py -i opt:<input file> -d <delete all data>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('plant_replace.py -i opt:<input file> -d <delete all data>')
            sys.exit()
        elif opt == "-i":
            input_file = arg
        elif opt == "-d":
            to_delete = True
    if not input_file:
        print('-i is required')
        sys.exit()
    return input_file, to_delete


if __name__ == "__main__":
    main(sys.argv[1:])
