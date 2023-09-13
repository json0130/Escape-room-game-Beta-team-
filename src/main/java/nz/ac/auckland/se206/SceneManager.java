package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  public enum AppUi {
    ROOM,
    CHAT,
    TUTORIAL,
    PLAYER
  }

  private static HashMap<AppUi, Parent> scenes = new HashMap<AppUi, Parent>();

  public static void addScene(AppUi appUi, Parent root) {
    scenes.put(appUi, root);
  }

  public static Parent getScene(AppUi appUi) {
    return scenes.get(appUi);
  }
}
