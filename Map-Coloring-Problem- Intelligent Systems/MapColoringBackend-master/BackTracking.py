import importlib

import csvReader
import csv
import random

selected_root_domain_values = []
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
    global state_list
    global selected_backtrack_node
    selected_backtrack_node =set()


def backtracking(failed_index, state_domain_dict, neighbor_state_dict, is_backtrack=False, parent_domain_to_be_chosen=None):
    """
    The function implements Backtracking Algorithm with depth first search to solve
    Map couloring problem
    :param failed_index:Start Index during backtracking process.
    :param state_domain_dict:Dictionary mapping states and their state_domain_dicts i.e List of legal colors available to be chosen.
    :param neighbor_state_dict: Dictionary mapping states and their neigbours
    :param is_backtrack: Flag to differentiate between backtracking process and the normal flow
    :param parent_domain_to_be_chosen: Parent Domain to be chosen once the algorithm reaches the root node and restarts the algorithm
    :return:
    """
    global out
    global failed
    global back_track_count
    key = state_list[0]
    i = failed_index
    visited_flag = False
    while i < len(state_list):
        previous_selected_parent_domain = None
        key = state_list[i]
        if len(state_domain_dict[key]) <= 0 or (selected_dict.get(key,None) is not None
                                                and len(selected_dict[key]) == 4):
            back_track_count += 1
            out += key.lower() + "," + "White" + "\n"
            failed = True
            failed_index = i
            break
        else:
            failed = False
        if key not in visited_node and len(state_domain_dict[key]) > 0:
            if is_backtrack and selected_dict.get(key,None) is not None:
                index=len(selected_dict.get(key))
                parent_domain = state_domain_dict[key][index]
                previous_selected_parent_domain = selected_dict.get(key)[-1]
            elif is_backtrack and selected_dict.get(key,None) is None:
                parent_domain=state_domain_dict[key][0]
            if parent_domain_to_be_chosen is not None and key == state_list[0]:
                parent_domain = parent_domain_to_be_chosen
                previous_selected_parent_domain = selected_root_domain_values[-1]
            elif not is_backtrack:
                if selected_dict.get(key) is not None:
                    index=len(selected_dict.get(key))
                    parent_domain=state_domain_dict[key][index]
                else:
                    parent_domain = state_domain_dict[key][0]
            selected_dict.setdefault(key,list()).append(parent_domain)
            for neighbours in neighbor_state_dict[key]:
                if neighbours in selected_dict.keys():
                    if parent_domain == selected_dict[neighbours][-1]:
                        i -= 1
                        visited_flag = True
                        break
        if not visited_flag:
            visited_node.add(key)
        visited_flag = False
        i += 1
        out += key.lower() + "," + parent_domain + "\n"
    if failed:
        del selected_dict[state_list[failed_index]]
        failed_index -= 1
        while len(selected_dict[state_list[failed_index]]) >= 3:
            if len(selected_dict[state_list[failed_index]]) == 4:
                del selected_dict[state_list[failed_index]]
            failed_index -= 1
        if failed_index > 0:
            visited_node.clear()
            backtracking(failed_index,state_domain_dict,neighbor_state_dict,True,None)
        else:
            state_domain_dict_list = ['Red', 'Green', 'Blue', 'Yellow']
            previous_root_values = selected_dict[state_list[0]]
            selected_root_domain_values.extend(previous_root_values)
            for previous_domain in selected_root_domain_values:
                    state_domain_dict_list.remove(previous_domain)
            if len(state_domain_dict_list) == 0:
                return
            read_csv()
            selected_dict.clear()
            visited_node.clear()
            selected_backtrack_node.clear()
            backtracking(0, state_domain_dict, neighbor_state_dict, True, state_domain_dict_list[0])
    else:
        pass


def main(map_name=None):
    importlib.reload(csvReader)
    read_csv(map_name)
    global state_list
    if not map_name:
        state_list = ['WA', 'OR', 'CA', 'NV', 'ID', 'MT', 'WY', 'UT', 'AZ', 'NM', 'CO', 'ND', 'SD', 'NE', 'KS', 'OK', 'TX', 'MN', 'IA', 'MO', 'AR', 'LA', 'MS', 'AL', 'FL', 'GA', 'SC', 'NC', 'TN', 'KY', 'VA', 'DE', 'MD', 'WV', 'OH', 'IN', 'IL', 'WI', 'MI', 'PA', 'NJ', 'CT', 'RI', 'NY', 'VT', 'NH', 'MA', 'ME', 'AK', 'HI', 'DC']
    else:
        state_list = ['au-wa', 'au-sa', 'au-nt', 'au-qld', 'au-vic', 'au-nsw', 'au-ta']
    backtracking(0, state_domain_dict, neighbor_state_dict)
    print("Backtrack Count", back_track_count)
    return out
