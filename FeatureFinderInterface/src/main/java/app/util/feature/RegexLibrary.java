package app.util.feature;

public class RegexLibrary {
   private String url;
   private Boolean busy;
   
   public RegexLibrary(String url) {
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
