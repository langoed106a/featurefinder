package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

public class WordListStorage {
private static final String COMMON_WORDS_FILE = "bealeassortedwordlist.txt";
private WebApplicationContext applicationContext;

public WordListStorage(WebApplicationContext applicationContext) {
this.applicationContext = applicationContext;
}
public List<String> readCommonWords() {
List<String> commonwords = new ArrayList<>();
commonwords = this.readWordList(COMMON_WORDS_FILE);
return commonwords;
}
public List<String> readWordList(String filename) {
BufferedReader bufferedReader=null;
InputStream inputStream=null;
InputStreamReader inputStreamReader = null;
List<String> lines = new ArrayList<>();
Resource resource=null;
String line="";
try {
resource = applicationContext.getResource("classpath:"+filename);
inputStream = resource.getInputStream();
inputStreamReader = new InputStreamReader(inputStream);
bufferedReader = new BufferedReader(inputStreamReader);
line = bufferedReader.readLine();
while (line!=null) {
lines.add(line);
line = bufferedReader.readLine();
}
bufferedReader.close();
inputStreamReader.close();
} catch (Exception exception) {
exception.printStackTrace();
}
return lines;
}
}