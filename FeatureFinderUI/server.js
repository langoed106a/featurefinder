
var http = require('http');
var fs = require('fs');
var path = require('path');
var url = require('url');

var directoryTreeToObj = function(dir, done) {
    var results = [];

    fs.readdir(dir, function(err, list) {
        if (err)
            return done(err);

        var pending = list.length;

        if (!pending)
            return done(null, {name: path.basename(dir), type: 'folder', children: results});

        list.forEach(function(file) {
            file = path.resolve(dir, file);
            fs.stat(file, function(err, stat) {
                if (stat && stat.isDirectory()) {
                    directoryTreeToObj(file, function(err, res) {
                        results.push({
                            name: path.basename(file),
                            type: 'folder',
                            children: res
                        });
                        if (!--pending)
                            done(null, results);
                    });
                }
                else {
                    results.push({
                        type: 'file',
                        name: path.basename(file)
                    });
                    if (!--pending)
                        done(null, results);
                }
            });
        });
    });
};

function rehearsalList(response) {
    var rehearsallist=[];
    var dir="./dist/rehearsals/";
    var allLines=[]
    var songlist=[]
    var contentType="";
    var json;
    var index=0;
    fs.readdir(dir, function(err, files) {
       var sessionlist=[];
       console.log("Here1");
       var readFiles = function(index, sessionlist) {
                         var songlist=[]
                         var session={};
                         console.log("Files:"+files);
                         if ( index == files.length ) {
                            contentType = getContentType(".json");
                            json = JSON.stringify(sessionlist); 
                            response.writeHead(200, { 'Content-Type': contentType });
                            response.end(json, 'utf-8');
                            console.log("SessionList:"+sessionlist);
                         } else {
                         fs.readFile(dir+files[index],"utf-8", function( error, data ) {
                             allLines = data.split(/\r\n|\n/); 
                             for (var j=0;j<allLines.length;j++) {
                                  console.log("Line:"+allLines[j]);
                                  songlist.push(allLines[j]);
                             }
                             session["date"]=files[index];
                             session["songs"]=songlist;
                             sessionlist.push(session); 
                             readFiles(index + 1, sessionlist);
                         });
                        }
                 }
            readFiles(0,sessionlist)
        });
}

function createDirectory(dir) {
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir);
    }
}

function getContentType(extname) {
    switch (extname) {
        case '.html':
            contentType = 'text/html';
            break;
        case '.htm':
            contentType = 'text/html';
            break;
        case '.js':
            contentType = 'text/javascript';
            break;
        case '.css':
            contentType = 'text/css';
            break;
        case '.json':
            contentType = 'application/json';
            break;
        case '.ico':
            contentType = 'image/jpg';
            break;
        case '.jpg':
            contentType = 'image/jpg';
            break;
        case '.wav':
            contentType = 'audio/wav';
            break;
    }
   return contentType;
}

http.createServer(function (request, response) {
    console.log('request starting...'+request.url);
    var folder = "";
    var params = "";
    var incomingurl = request.url;
    var filePath = incomingurl;
    var location =incomingurl.indexOf("?");
    if (location>0) {
        filePath = incomingurl.substring(0,location);
        params = incomingurl.substring(location+1,incomingurl.length);
    } 
    console.log("Request:"+filePath);
    console.log("Params:"+params);
    if (filePath == "/mediafilelist") {
        var dir = "./dist/media/";
        directoryTreeToObj(dir, function(err, resp){
            var contentType = getContentType(".json");
            var json="";
            if(err)
                console.error(err);
            json = JSON.stringify(resp); 
            response.writeHead(200, { 'Content-Type': contentType });
            response.end(json, 'utf-8');
        });
    } else if (filePath == "/mediafilecreate") {
        location=params.indexOf("name=");
        if (location>-1) {
            folder = params.substring(location+5,params.length);
            console.log("**Parameter:"+folder);
            // createDirectory(dir);
        }    
    } else if (filePath == "/rehearsallist") {
            rehearsalList(response);   
    } else {
         if (filePath=='/') {
             filePath='index.html';
         }
         var extname = path.extname(filePath);
         console.log("Extname:"+extname);
         var contentType = getContentType(extname);
         filePath = "./dist/"+filePath;
         fs.readFile(filePath, function(error, content) {
            if (error) {
                if(error.code == 'ENOENT'){
                    fs.readFile('./404.html', function(error, content) {
                        response.writeHead(200, { 'Content-Type': contentType });
                        response.end(content, 'utf-8');
                    });
                }
                else {
                    response.writeHead(500);
                    response.end('Sorry, check with the site admin for error: '+error.code+' ..\n');
                    response.end(); 
                }
            }
            else {
                response.writeHead(200, { 'Content-Type': contentType });
                response.end(content, 'utf-8');
            }
         });
    }
}).listen(3000);
console.log('Server running at http://127.0.0.1:3000/');
