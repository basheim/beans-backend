#!/usr/bin/python3

import sys
import getopt
import requests
import os
from datetime import date, datetime, timedelta, time, timezone

base_url = 'https://backend.programmingbean.com'
post_delete_url = '/api/v1/plants'
get_latest_date_url = '/api/v1/plants/latestDate'
upload_image_url = '/api/v1/plants/image/save'
api_user = 'admin'
api_password = os.getenv('API_PASSWORD')


def main(argv):
    start_date = datetime.combine(date.today(), time.min).replace(tzinfo=timezone(offset=timedelta()))
    # get the file arg
    (input_file, image_directory, script_path) = get_args(argv)
    # set up the session
    session = requests.session()
    session.auth = (api_user, api_password)
    # delete all data and start over
    res = session.get(url=base_url + get_latest_date_url)
    last_date = datetime.strptime(res.json()['latestDate'], '%Y-%m-%dT%H:%M:%S.%f%z')

    if last_date > start_date + timedelta(days=1):
        sys.exit()

    os.system('python -s ' + script_path + ' -i ' + input_file + ' -j ' + image_directory + ' -d')


def get_args(argv):
    input_file = None
    image_directory = None
    script_path = None
    try:
        opts, args = getopt.getopt(argv, "hi:j:s:")
    except getopt.GetoptError:
        print('plant_refresh.py -i opt:<input file> -j opt:<image directory> -s opt:<script path>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('plant_refresh.py -i opt:<input file> -j opt:<image directory> -s opt:<script path>')
            sys.exit()
        elif opt == "-i":
            input_file = arg
        elif opt == "-j":
            image_directory = arg
        elif opt == "-s":
            script_path = arg
    if not input_file:
        print('-i is required')
        sys.exit()
    if not image_directory:
        print('-j is required')
        sys.exit()
    if not script_path:
        print('-s is required')
        sys.exit()
    return input_file, image_directory, script_path


if __name__ == "__main__":
    main(sys.argv[1:])
