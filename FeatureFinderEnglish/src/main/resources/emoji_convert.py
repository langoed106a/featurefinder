f = open("emojis.txt", "r")
emoji_line=""
number=0
emoji_list=[]
possible = False
for line in f:
    line = line.rstrip('\r\n')
    if  (line.startswith("<tr><td class='rchars'>")):
        possible = True
        count=1
        if (len(emoji_line)>0):
            emoji_list.append(emoji_line)
            number = number + 1
        emoji_line = ""
    elif possible:
        print("Line:"+line)
        if (line.startswith('<td>')):
            line = line.replace("<td>","")
            line = line.replace("</td>","")
            if (count<3):
                emoji_line = emoji_line + line +","
                count = count +1
            else:
                possible = False
print("Number of items:"+str(number));
#for item in emoji_list:
#    print(str(item))
f.close()