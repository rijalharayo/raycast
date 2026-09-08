package main.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.game.Scene;
import main.math.algebra.Vector2;
import main.ui.Fonts;
import main.ui.UIComponent;

// Standard text
public class UIText extends UIComponent {
     public static final Font DEFAULT_FONT = Fonts.loadFont();
     private float fontSize = 20f;
     private String text;
     private Color fontColor;

     // Constructor
     public UIText(String text) {
          super();

          this.text = text;
          this.position = Vector2.ZERO;
     }

     public UIText(String text, float size) {
          super();

          this.text = text;
          this.fontSize = size;
          this.position = Vector2.ZERO;
     }

     public UIText(float x, float y, String text, float size) {
          super(x, y);
          this.text = text;
          this.fontSize = size;
     }

     // Getters
     public String getText() {
          return text;
     }

     // Setters
     public void setColor(Color color) {
          this.fontColor = color;
     }

     public void setSize(float size) {
          this.fontSize = size;
     }

     public void setText(String textString) {
          this.text = textString;
     }

     @Override
     public void render(Graphics2D g) {
          Font font = DEFAULT_FONT.deriveFont(fontSize);

          g.setFont(font);
          g.setColor(fontColor);

          Vector2 screenPos = Scene.worldToScreen(position);

          FontMetrics metrics = g.getFontMetrics();

          // Positions by center
          float x = screenPos.getX() - metrics.stringWidth(text) / 2f;
          float y = screenPos.getY() + (metrics.getAscent() - metrics.getDescent()) / 2f;

          g.drawString(text, x, y);
     }

     @Override
     public void render(Graphics2D g, Vector2 position) {
          Font font = DEFAULT_FONT.deriveFont(fontSize);

          g.setFont(font);
          g.setColor(fontColor);

          Vector2 screenPos = Scene.worldToScreen(position);

          FontMetrics metrics = g.getFontMetrics();

          // Positions by center
          float x = screenPos.getX() - metrics.stringWidth(text) / 2f;
          float y = screenPos.getY() + (metrics.getAscent() - metrics.getDescent()) / 2f;

          g.drawString(text, x, y);
     }
}
