package app.util.feature;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class FileProcessor {
  private static String FILE="file";
  private static String FOLDER="folder";
  private static Integer THREAD_ALIVE_LIMIT=10;
  private RemoteProcessor remoteProcessor;
  private Tracker tracker;

    public FileProcessor(RemoteProcessor remoteProcessor) {
       this.remoteProcessor = remoteProcessor;
       this.tracker = new Tracker();
    }

    public void processDocuments(List<Document> documentList, String tokenid) {
        File fileToProcess = null;
        Integer fileCount = 0, threadAlive = 0, fileNumber=0;
        List<File> filePathList = this.getFileNames(documentList);
        for (File file: filePathList) {
            try {
                  fileToProcess = file;
                  tracker.incrementThreadAlive();
                  Thread t = new Thread(new FileReader(fileToProcess, remoteProcessor, tracker, tokenid));
                  t.start();
            }catch (Exception e) {
                    //do something  
            }
           threadAlive = tracker.getThreadAlive();
           while(threadAlive > THREAD_ALIVE_LIMIT){//This while loop will control number of thread to be not more than 10
              try{Thread.sleep(100);}catch(Exception e){}//Release the processor
                    threadAlive = tracker.getThreadAlive();
                    System.out.println("Reached maximum");
            } 
            fileNumber = tracker.getFilesProcessed();
            while(fileNumber < filePathList.size()){
                //wait till last thread complete it's task
                //I am not using thread.join() for performance
                  fileNumber = tracker.getFilesProcessed();
                  System.out.println("Number of file processed :" + fileNumber);
                try{Thread.sleep(100);}catch(Exception e){}
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
                      directoryPath = new File(folderPath);
                      if (directoryPath !=null) {
                         filesList = directoryPath.listFiles();
                         for (int i=0; i<filesList.length; i++) {
                             filePathList.add(filesList[i]);
                         }
                      }
                }

            }
        }
      return filePathList;
    }
}