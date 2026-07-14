package game;

public class SceneManager {

     private static Scene currentScene;

     public static void setScene(Scene scene) {
          currentScene = scene;
     }

     public static Scene getCurrentScene() {
          return currentScene;
     }

}