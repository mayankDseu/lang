package HackGUI;

import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

class Hyperactive implements HyperlinkListener {
   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         JEditorPane var2 = (JEditorPane)var1.getSource();
         if (var1 instanceof HTMLFrameHyperlinkEvent) {
            HTMLFrameHyperlinkEvent var3 = (HTMLFrameHyperlinkEvent)var1;
            HTMLDocument var4 = (HTMLDocument)var2.getDocument();
            var4.processHTMLFrameHyperlinkEvent(var3);
         } else {
            try {
               var2.setPage(var1.getURL());
            } catch (IOException var5) {
               System.out.println(var5.getMessage());
               System.exit(0);
            }
         }
      }

   }
}
