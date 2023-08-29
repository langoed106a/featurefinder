function show_rows() {
    var data = localStorage.getItem("ffresults");
    var table = document.getElementById("rawresults");
    var count = 0;
    var row;
    var cell1,cell2,cell3,cell4,cell5,item;
    var rowsArray = [];
    data.split('\n').map(function (value) {
        rowsArray.push(JSON.parse(value));
    });
    for (var x=0;x<rowsArray.length;i++) {
        row = table.insertRow(count);
        item = rowsArray[x];
        cell1 = row.insertCell(0);
        cell2 = row.insertCell(1);
        cell3 = row.insertCell(2);
        cell4 = row.insertCell(3);
        cell5 = row.insertCell(4);
        cell1.innerHTML = item.file;
        cell2.innerHTML = item.hits;
        cell3.innerHTML = item.feature;
        cell4.innerHTML = item.match;
        cell5.innerHTML = item.group;
    }    
    localStorage.removeItem("ffresults");
}