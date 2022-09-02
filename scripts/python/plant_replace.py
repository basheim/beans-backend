#!/usr/bin/python3

import sys
import getopt
import csv
import requests
import os

base_url = "http://beans-backend-lb-282639646.us-west-2.elb.amazonaws.com:80"
post_delete_url = "/api/v1/plants"
to_delete = False
api_user = "admin"
api_password = os.getenv('API_PASSWORD')

def main(argv):
    # get the file arg
    input_file = get_input_file(argv)
    # delete all data
    session = requests.session()
    session.auth = (api_user, api_password)
    if to_delete:
        session.delete(url=base_url + post_delete_url)

    with open(input_file, newline='') as csv_file:
        reader = csv.DictReader(csv_file)
        for row in reader:
            print(row)


def get_input_file(argv):
    input_file = None
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
    return input_file


if __name__ == "__main__":
    main(sys.argv[1:])
