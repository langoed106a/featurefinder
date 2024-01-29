f = open("emoji_utf15_list.txt", "r")
g = open("emoji_postag_list.txt", "w")
emoji_line=""
number=0
list_line=""
possible = False
for line in f:
    line = line.rstrip('\r\n')
    line = line + ",n"
    g.write(line+"\n")
f.close()
g.close()