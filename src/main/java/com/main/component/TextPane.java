package com.main.component;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/8/2.
 */
public class TextPane extends JTextPane{
    public TextPane(){
        String[] initString =
                { "This is an editable JTextPane, ",            //regular
                        "another ",                                   //italic
                        "styled ",                                    //bold
                        "text ",                                      //small
                        "component, ",                                //large
                        "which supports embedded components..." + "\n",//regular
                        " " + "\n",                                //button
                        "...and embedded icons..." + "\n",         //regular
                        " ",                                          //icon
                        "\n" + "JTextPane is a subclass of JEditorPane that " +
                                "uses a StyledEditorKit and StyledDocument, and provides " +
                                "cover methods for interacting with those objects."
                };

        String[] initStyles =
                { "regular", "italic", "bold", "small", "large",
                        "regular", "button", "regular", "icon",
                        "regular"
                };

//        JTextPane textPane = new JTextPane();
        StyledDocument doc = this.getStyledDocument();
        addStylesToDocument(doc);

        try {
            for (int i=0; i < initString.length; i++) {
                doc.insertString(doc.getLength(), initString[i],
                        doc.getStyle(initStyles[i]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
    }

    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
                "a cute pig");
        if (pigIcon != null) {
            StyleConstants.setIcon(s, pigIcon);
        }

        s = doc.addStyle("button", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon soundIcon = createImageIcon("images/sound.gif",
                "sound icon");
        JButton button = new JButton();
        if (soundIcon != null) {
            button.setIcon(soundIcon);
        } else {
            button.setText("BEEP");
        }
        button.setCursor(Cursor.getDefaultCursor());
        button.setMargin(new Insets(0,0,0,0));
//        button.setActionCommand(buttonString);
//        button.addActionListener(this);
        StyleConstants.setComponent(s, button);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = this.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
