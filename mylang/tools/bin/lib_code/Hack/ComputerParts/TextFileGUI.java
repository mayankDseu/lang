package Hack.ComputerParts;

public interface TextFileGUI extends ComputerPartGUI {
   void addTextFileListener(TextFileEventListener var1);

   void removeTextFileListener(TextFileEventListener var1);

   void notifyTextFileListeners(String var1, int var2);

   void setContents(String var1);

   void setContents(String[] var1);

   void addLine(String var1);

   void addHighlight(int var1, boolean var2);

   void clearHighlights();

   void addEmphasis(int var1);

   void removeEmphasis(int var1);

   String getLineAt(int var1);

   void setLineAt(int var1, String var2);

   int getNumberOfLines();

   void select(int var1, int var2);

   void hideSelect();
}
