package app.util.feature;

public class UTFCharacter {
   private Character character;
   private String type;
   private String codepoint;


   public UTFCharacter(Character character, String type, String codepoint) {
      this.character = character;
      this.type = type;
      this.codepoint = codepoint;
   }

   public String getType() {
      return type;
   }

   public Character getCharacter() {
      return character;
   }

   public String getCodepoint() {
      return codepoint;
   }

}