package main.physics.optics;

import java.awt.Color;

// Materials that absorb light
public class Material {
     private final String materialName;
     private final float absorptionCoefficient;
     private final Color materialColor;

     // Predefined materials
     public static final Material BLACK = new Material("Black", 1.00f, new Color(20, 20, 20, 255));
     public static final Material RUBBER = new Material("Rubber", 0.90f, new Color(30, 30, 30, 255));
     public static final Material PAPER = new Material("Paper", 0.15f, new Color(245, 245, 245, 255));
     public static final Material WHITE = new Material("White", 0.05f, new Color(255, 255, 255, 255));

     // Constructors
     public Material(String name, float absorptionCoefficient, Color color) {
          if(absorptionCoefficient < 0) {
               throw new IllegalArgumentException("Absorption coefficient cannot be negative");
          }

          if(color == null) {
               throw new IllegalArgumentException("The material must have a distinct color");
          }

          this.materialName = name;
          this.absorptionCoefficient = absorptionCoefficient;
          this.materialColor = color;
     }

     // Getters

     public String getName() {
          return materialName;
     }

     public float getAbsorptionCoefficient() {
          return absorptionCoefficient;
     }

     public Color getColor() {
          return materialColor;
     }

     @Override
     public String toString() {
          return materialName + "\n" + "Absorption: " + absorptionCoefficient;
     }

}