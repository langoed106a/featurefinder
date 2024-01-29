f = open("emoji_postag_list.txt", "r")
emoji_line=""
number=0
list_line=""
possible = False
for line in f:
    line = line.rstrip('\r\n')
    items = line.split(',')
    list_item=""
    for item in items:
        list_item = list_item +"\"" + item + "\","
    list_item = list_item[:-1]
    list_item = "{" + list_item + "},"
    list_line = list_line + list_item
list_line = list_line[:-1]
list_line = "[" + list_line + "]"
f.close()
g = open("emoji_json_list.txt", "w")
g.write(list_line)
g.close()