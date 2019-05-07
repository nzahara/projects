import csv

neighbor_state_dict = dict()
state_domain_dict = dict()


def read_csv(map_name=None):
    """
    The function reads content from the csv file mentioned and returns a dictionary
    mapping state and its neigbours. And also returns the dictionary mapping state and
    the valid list of colors to be assigned.
    :return:
    """
    global neighbor_state_dict
    global state_domain_dict
    if map_name:
        file = 'australia-neighbors.csv'
    else:
        file = 'neighbours.csv'
    with open(file) as csv_file:
        csv_reader = csv.DictReader(csv_file)
        for row in csv_reader:
            neighbor_state_dict.setdefault(row['StateCode'],set()).add(row['NeighborStateCode'])
            neighbor_state_dict.setdefault(row['NeighborStateCode'], set()).add(row['StateCode'])
            state_domain_dict[row['StateCode']] = ['Red', 'Green', 'Blue','Yellow']
            state_domain_dict[row['NeighborStateCode']] = ['Red', 'Green', 'Blue','Yellow']
    return neighbor_state_dict, state_domain_dict
