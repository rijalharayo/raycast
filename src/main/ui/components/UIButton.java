package main.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics2D;

import main.game.Scene;
import main.math.algebra.Vector2;
import main.ui.Fonts;
import main.ui.UIComponent;

// Buttons
public class UIButton extends UIComponent {
     public static final Font DEFAULT_FONT = Fonts.loadFont();
     private static final Color DEFAULT_BUTTON_COLOR = new Color(28, 27, 29); // Dark grey-ish color

     private UIText uiText;
     private float borderWidth = 10f;
     private Color buttonColor = DEFAULT_BUTTON_COLOR; // Dark grey-ish color
     private Color borderColor = new Color(59, 59, 59); // Lighter grey-ish color

     private Color hoverColor = new Color(41, 41, 41); // A little lighter grey-ish color than buttonColor
     private Color clickColor = new Color(31, 31, 31); // A little darker than hover color

     private Runnable onClick;

     // Constructors
     public UIButton(String text, int width, int height) {
          super(width, height);

          this.uiText = new UIText(text, 20f);
          uiText.setColor(Color.WHITE);
     }

     // Getters
     public String getText() {
          return uiText.getText();
     }

     // Setters

     public void setText(String txt) {
          uiText.setText(txt);
     }

     public void setTextSize(float sz) {
          uiText.setSize(sz);
     }

     public void setButtonColors(Color mainColor, Color borderColor) {
          this.borderColor = borderColor;
          this.buttonColor = mainColor;
     }

     public void setOnClick(Runnable action) {
          this.onClick = action;
     }

     @Override
     protected void onHover() {
          if(!buttonColor.equals(hoverColor)) {
               buttonColor = hoverColor;
          }
     }

     @Override
     protected void onClick() {
          // Flicker the click color when clicked
          if(!buttonColor.equals(clickColor)) {
               buttonColor = clickColor;
          }

          // Trigger click event
          click();
     }

     @Override
     protected void setAsDefault() {
          if(!buttonColor.equals(DEFAULT_BUTTON_COLOR)) {
               buttonColor = DEFAULT_BUTTON_COLOR;
          }
     }

     // Does action when clicked
     public void click() {
          if (onClick != null) {
               onClick.run();
          }
     }

     @Override
     public void render(Graphics2D g) {
          Vector2 screenPos = Scene.worldToScreen(position);

          float x = screenPos.getX() - (width() / 2f);
          float y = screenPos.getY() - (height() / 2f);

          // Button
          g.setColor(buttonColor);
          g.fillRect((int) x, (int) y, width(), height());

          // Border
          g.setColor(borderColor);
          g.setStroke(new BasicStroke(borderWidth));

          float borderOffset = borderWidth / 2f;

          g.drawRect(
               (int) (x + borderOffset),
               (int) (y + borderOffset),
               (int) (width() - borderWidth),
               (int) (height() - borderWidth)
          );

          // Text
          uiText.render(g, position);
     }
}
