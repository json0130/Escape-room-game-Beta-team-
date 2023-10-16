package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/** Manages the scenes in the application. */
public class SceneManager {
  public enum AppUi {
    CHAT,
    ROOM1,
    PLAYER,
    TUTORIAL,
    ANIMATION,
    INTRO,
    TILEPUZZLE,
    TILEROOM,
    ROOM3,
    END,
    WIN,
    LOSE,
    END1,
    HELPERCHAT,
    AIWINDOW
  }

  private static HashMap<AppUi, Parent> scenes = new HashMap<AppUi, Parent>();

  public static void addScene(AppUi appUi, Parent root) {
    scenes.put(appUi, root);
  }

  public static Parent getScene(AppUi appUi) {
    return scenes.get(appUi);
  }
}
