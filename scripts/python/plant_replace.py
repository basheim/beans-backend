#!/usr/bin/python3

import sys
import getopt
import csv


def main(argv):
    input_file = get_input_file(argv)
    with open(input_file, newline='') as csv_file:
        reader = csv.DictReader(csv_file)
        for row in reader:
            print(row)


def get_input_file(argv):
    input_file = None
    try:
        opts, args = getopt.getopt(argv,"hi:")
    except getopt.GetoptError:
        print('plant_replace.py -i <inputfile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('plant_replace.py -i <inputfile>')
            sys.exit()
        elif opt == "-i":
            input_file = arg
    if not input_file:
        print('-i is required')
        sys.exit()
    print('Input file is "', input_file)
    return input_file


if __name__ == "__main__":
    main(sys.argv[1:])
