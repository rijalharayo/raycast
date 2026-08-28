package main.physics.optics;

import java.awt.Color;

// Mediums of light travel
public class Medium {
     private final String mediumName;
     private final float refractiveIndex;
     private final Color mediumColor;

     // Predefined mediums
     public static final Medium AIR = new Medium("Air", 1.00f, new Color(220, 255, 255, 20));
     public static final Medium WATER = new Medium("Water", 1.33f, new Color(50, 210, 255, 70));
     public static final Medium GLASS = new Medium("Glass", 1.50f, new Color(120, 240, 255, 45));
     public static final Medium DIAMOND = new Medium("Diamond", 2.42f, new Color(180, 255, 255, 60));
     public static final Medium ACRYLIC = new Medium("Acrylic", 1.49f, new Color(80, 230, 255, 55));

     // Constructors
     public Medium(String name, float refractiveIndex, Color color) {
          this.mediumName = name;
          this.refractiveIndex = refractiveIndex;
          this.mediumColor = color;
     }

     // Getters
     public String getName() {
          return mediumName;
     }

     public float getRefractiveIndex() {
          return refractiveIndex;
     }

     public Color getColor() {
          return mediumColor;
     }

     @Override
     public String toString() {
          return mediumName + "\n" + "Ref. Index: " + refractiveIndex;
     }
}