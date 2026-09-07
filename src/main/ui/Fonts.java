package main.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class Fonts {
     // Loads the font
     public static Font loadFont() {
          try {
               InputStream is = Fonts.class.getResourceAsStream("/resources/fonts/Slackey-Regular.ttf");
               Font font = Font.createFont(Font.TRUETYPE_FONT, is);

               GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

               ge.registerFont(font);

               return font;
          } 
          catch (Exception e) {
               e.printStackTrace();
               return new Font("SansSerif", Font.PLAIN, 20);
          }
     }
}