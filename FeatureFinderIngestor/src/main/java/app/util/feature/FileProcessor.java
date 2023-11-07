package app.util.feature;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class FileProcessor {
  private static String FILE="file";
  private static String FOLDER="folder";
  private static Integer THREAD_ALIVE_LIMIT=10;

  public HTTPAsyncSender asyncSender;
  private RemoteProcessor remoteProcessor;
  private Tracker tracker;

    public FileProcessor(ServiceLocator serviceLocator) {
       this.asyncSender = new HTTPAsyncSender(serviceLocator);
    }

    public void processDocuments(List<Document> documentList, String tokenid) {
        File fileToProcess = null;
        Integer fileCount = 0, threadAlive = 0, fileNumber=0;
        List<File> filePathList = this.getFileNames(documentList);
        this.tracker = new Tracker();
        for (File file: filePathList) {
            try {
                  fileToProcess = file;
                  tracker.incrementThreadAlive();
                  Thread t = new Thread(new FileReader(fileToProcess, this.asyncSender, tracker, tokenid));
                  t.start();
            } catch (Exception exception) {
                exception.printStackTrace(); 
            }
           threadAlive = tracker.getThreadAlive();
           while(threadAlive > THREAD_ALIVE_LIMIT){//This while loop will control number of thread to be not more than 10
              try{Thread.sleep(100);}catch(Exception e){}//Release the processor
                    threadAlive = tracker.getThreadAlive();
                    System.out.println("Reached maximum");
            } 
        }
    }

    public List<File> getFileNames(List<Document> documentList) {
        Integer numberOfFiles=0;
        String folderPath="";
        List<File> filePathList=new ArrayList<>();
        File directoryPath=null;
        File filesList[]=null;
        if ((documentList!=null) && (documentList.size()>0)) {
            for (Document document: documentList) {
                if (document.getType().equalsIgnoreCase(FILE)) {
                    filePathList.add(new File(document.getContents()));
                } else if (document.getType().equalsIgnoreCase(FOLDER)) {
                      folderPath = document.getContents();
                      directoryPath = new File(folderPath);
                      if (directoryPath !=null) {
                         filesList = directoryPath.listFiles();
                         if (filesList != null) {
                             for (int i=0; i<filesList.length; i++) {
                                 filePathList.add(filesList[i]);
                             }
                         }
                      }
                }

            }
        }
      return filePathList;
    }
}