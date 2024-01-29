
emoji_number="<tr><td class='rchars'>"
emoji_utf="<td class='code'><a href="
emoji_code="<td class='chars'>"
emoji_title="<td class='name'>"
number = "?"
symbol = ""
title = ""
utf=""

t = open("emoji_utf15_list.txt", "w")
f = open("emojis_utf15.txt", "r")
emoji_line=""
count=0
num=0
emoji_list=[]
possible = False
for line in f:
    line = line.rstrip('\r\n')
    if  (line.startswith(emoji_utf)):
        line = line.replace(emoji_utf, "")
        start = line.find(">")
        index = line.find("</a></td>")
        if (index>0):
            utf= line[start+1:index]
        count = 1
    if  (line.startswith(emoji_number)):
        line = line.replace(emoji_number, "")
        index = line.find("</td>")
        if (index>0):
            number= line[:index]
        count = count + 1
        print(line+"   find:"+str(index)+"  number:"+number)
    if  (line.startswith(emoji_code)):
        line = line.replace(emoji_code, "")
        index = line.find("</td>")
        if (index>0):
            symbol= line[:index]
        count=count+1  
    if  (line.startswith(emoji_title)):
        line = line.replace(emoji_title, "")
        index = line.find("</td>")
        if (index>0):
            title= line[:index] 
            title = title.replace(",","-")
        count=count+1
    if (count==4):
        num = num +1  
        if (number!="?"): 
            t.write(number+","+utf+","+symbol+","+title+"\n");
        number = "?"
        symbol = ""
        title = ""
        utf=""
print("Number of items:"+str(num));
#for item in emoji_list:
#    print(str(item))
f.close()
t.close()