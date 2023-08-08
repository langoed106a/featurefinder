package app.util.feature;

public class NodeServer {
   private String url;
   private Boolean busy;
   
   public NodeServer(String url) {
      this.url = url;
      this.busy = false;
   }

   public void finished() {
      this.busy = false;
   }

   public void startJob() {
      this.busy = true;
   }
   
}
