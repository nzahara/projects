import importlib

import csvReader

selected_root_domain_values = list()
state_list = []
failed = False
back_track_count = 0
out = ""


def read_csv(map_name=None):
    """
    The function reads the neighbor_state_dict from csv and sets state_domain_dict for the neighbor_state_dict read.
    :return: 2 dictionaries, one mapping state and its neighbours and the other
    mapping state and its state_domain_dict i.e valid assignable colors.
    """
    global neighbor_state_dict, state_domain_dict
    neighbor_state_dict,state_domain_dict = csvReader.read_csv(map_name)
    global visited_node
    visited_node = set()
    global selected_dict
    selected_dict = dict()
    global selected_backtrack_node
    selected_backtrack_node = set()


def forward_checking_singleton(failed_index, state_domain_dict, neighbor_state_dict, is_backtrack=False, parent_domain_to_be_chosen=None):
    """
    The function implements Forward Checking Algorithm to solve
    Map couloring problem
    :param failed_index:Start Index during backtracking process.
    :param state_domain_dict:Dictionary mapping states and their state_domain_dicts i.e List of legal colors available to be chosen.
    :param neighbor_state_dict: Dictionary mapping states and their neigbours
    :param is_backtrack: Flag to differentiate between backtracking process and the normal flow
    :param parent_domain_to_be_chosen: Parent Domain to be chosen once the algorithm reaches the root node and restarts the algorithm
    :return:
    """
    global out
    global back_track_count
    global failed
    key = state_list[0]
    i = failed_index
    while i < len(state_list):

        previous_selected_parent_domain = None
        key = state_list[i]
        singleton_domain_list = singleton(state_domain_dict, neighbor_state_dict, key, visited_node)

        if singleton_domain_list is not []:
            for singleton_domain in singleton_domain_list:
                if singleton_domain in state_domain_dict[key]:
                    state_domain_dict[key].remove(singleton_domain)

        if len(state_domain_dict[key]) <= 0:
            out += key.lower() + "," + "White" + "\n"
            failed = True
            failed_index = i
            break
        else:
            failed = False
        if key not in visited_node and len(state_domain_dict[key]) > 0:
            if is_backtrack and selected_dict.get(key, None) is not None:
                previous_selected_parent_domain = selected_dict.get(key).pop()
                if previous_selected_parent_domain in state_domain_dict[key]:
                    state_domain_dict[key].remove(previous_selected_parent_domain)
                if len(selected_dict[key]) == 0:
                    del selected_dict[key]
            if parent_domain_to_be_chosen is not None and key == state_list[0]:
                parent_domain = parent_domain_to_be_chosen
                previous_selected_parent_domain = selected_root_domain_values[-1]
            else:
                parent_domain = lcv(key, state_domain_dict, neighbor_state_dict, selected_dict)
            state_domain_dict[key].remove(parent_domain)
            selected_dict.setdefault(key,list()).append(parent_domain)
            visited_node.add(key)
            for neighbours in neighbor_state_dict[key]:
                if previous_selected_parent_domain is not None and previous_selected_parent_domain not in state_domain_dict[neighbours]:
                    if neighbours in selected_dict.keys() and previous_selected_parent_domain not in selected_dict[neighbours]:
                        state_domain_dict[neighbours].append(previous_selected_parent_domain)
                if parent_domain in state_domain_dict[neighbours] and neighbours not in list(visited_node):
                    state_domain_dict[neighbours].remove(parent_domain)
        i += 1
        out += key.lower() + "," + parent_domain + "\n"
    if failed:
        length = len(list( selected_backtrack_node))
        if length > 0:
            last_element = list(sorted(selected_backtrack_node))[0]
            failed_index = last_element - 1
        else:
            failed_index -= 1
        while len(state_domain_dict[state_list[failed_index]]) <= 1:
            failed_index -= 1
        selected_backtrack_node.add(failed_index)
        if failed_index > 0:
            visited_node.clear()
            forward_checking_singleton(failed_index, state_domain_dict, neighbor_state_dict, True, None)
        else:
            state_domain_dict_list = ['Red', 'Green', 'Blue', 'Yellow']
            previous_root_values = selected_dict[state_list[0]]
            selected_root_domain_values.extend(previous_root_values)
            for previous_domain in selected_root_domain_values:
                    state_domain_dict_list.remove(previous_domain)
            if len(state_domain_dict_list) == 0:
                return
            read_csv()
            forward_checking_singleton(0, state_domain_dict, neighbor_state_dict, True, state_domain_dict_list[0])
    else:
        pass


def singleton(state_domain_dict, neighbor_state_dict, key, visited_node):
    """
    The function checks for a neighbor of a given key has a singleton domain and returns the same
    if it finds one.
    :param state_domain_dict: Dictionary having a mapping of state and the legal colors.
    :param visited_node: A set containing the states that have already been visited during the
    algorithm and a state_domain_dict has been chosen.
    :return: key or None
    """
    domain_list = []
    for neighbours in neighbor_state_dict[key]:
        if neighbours in visited_node:
            continue
        if len(state_domain_dict[neighbours]) == 1:
            domain_list.append(state_domain_dict[neighbours][0])
    return domain_list


def lcv(key_state, state_domain, neighbour_list, selected_dict):
    """
    :param key: The state for which color have to be selected
    :param state_domain: Dictionary mapping states and their state_domain_dicts i.e List of legal colors available to be chosen.
    :param neighbour_list: Dictionary mapping states and their neigbours
    :return:
    """
    num_neighbour_colors = 0
    color_dict = dict()
    for color in state_domain[key_state]:
        for neighbour in neighbour_list[key_state]:
            if neighbour not in selected_dict.keys():
                if color in state_domain[neighbour]:
                    if len(state_domain[neighbour]) - 1 == 0:
                        color_dict[color] = float("inf")
                    else:
                        num_neighbour_colors += len(state_domain[neighbour]) - 1

        if color not in color_dict.keys():
            color_dict[color] = num_neighbour_colors

    return min(color_dict, key=lambda k: color_dict[k])


def main(map_name=None):
    importlib.reload(csvReader)
    read_csv(map_name)
    global state_list
    state_list = ['au-wa', 'au-sa', 'au-nt', 'au-qld', 'au-vic', 'au-nsw', 'au-ta']
    forward_checking_singleton(0, state_domain_dict, neighbor_state_dict)
    print("Backtrack Count", back_track_count)
    return out
