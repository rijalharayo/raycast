package main.ui.layouts;

import main.ui.UIComponent;
import main.ui.UILayout;

// Wraps components into multiple rows (like flex-box ig)
public class UIWrapLayout extends UILayout {
     private float width;
     private float gap;

     public UIWrapLayout(float width, float gap) {
          this.width = width;
          this.gap = gap;
     }

     @Override
     public void layout() {
          float x = 0;
          float y = 0;
          float rowHeight = 0;
          float totalHeight = 0;

          // Calculates the total height required by the layout
          for (UIComponent component : components) {
               float componentWidth = component.width();
               float componentHeight = component.height();

               // Starts a new row when the component does not fit
               if (x + componentWidth > width) {
                    x = 0;
                    y += rowHeight + gap;
                    rowHeight = 0;
               }

               x += componentWidth + gap;
               rowHeight = Math.max(rowHeight, componentHeight);
          }

          totalHeight = y + rowHeight;

          // Calculates the top-left corner of the layout from its center position
          float startX = position.getX() - width / 2f;
          float startY = position.getY() - totalHeight / 2f;

          x = startX;
          y = startY;
          rowHeight = 0;

          // Positions the components within the layout
          for (UIComponent component : components) {
               float componentWidth = component.width();
               float componentHeight = component.height();

               // Starts a new row when the component does not fit
               if (x + componentWidth > startX + width) {
                    x = startX;
                    y += rowHeight + gap;
                    rowHeight = 0;
               }

               component.setPosition(x, y);

               x += componentWidth + gap;
               rowHeight = Math.max(rowHeight, componentHeight);
          }
     }
}