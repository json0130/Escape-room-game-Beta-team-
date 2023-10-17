package nz.ac.auckland.se206;

import javafx.scene.image.ImageView;

public class Tile {

  private ImageView currentImage;
  private boolean freeSlot;
  private String letter;
  private double imageViewHorizontalCoordinates;
  private double imageViewVerticalCoordinates;

  private Tile leftTile;
  private Tile rightTile;
  private Tile bottomTile;
  private Tile topTile;

  public void tileInitialise(
      ImageView currentImage,
      boolean freeSlot,
      String letter,
      double imageViewHorizontalCoordinates,
      double imageViewVerticalCoordinates) {
    this.currentImage = currentImage;
    this.freeSlot = freeSlot;
    this.letter = letter;
    this.imageViewHorizontalCoordinates = imageViewHorizontalCoordinates;
    this.imageViewVerticalCoordinates = imageViewVerticalCoordinates;
  }

  public void setTilePos(Tile leftTile, Tile rightTile, Tile bottomTile, Tile topTile) {
    this.leftTile = leftTile;
    this.rightTile = rightTile;
    this.bottomTile = bottomTile;
    this.topTile = topTile;
  }

  public void setCurrentImage(ImageView image) {
    this.currentImage = image;
  }

  public void setLetter(String letter) {
    this.letter = letter;
  }

  public void setFreeSlot(boolean isFree) {
    this.freeSlot = isFree;
  }

  public ImageView getImage() {
    return this.currentImage;
  }

  public String getLetter() {
    return this.letter;
  }

  public boolean isFree() {
    return this.freeSlot;
  }

  public Tile getLeftTile() {
    return leftTile;
  }

  public Tile getRightTile() {
    return rightTile;
  }

  public Tile getBottomTile() {
    return bottomTile;
  }

  public Tile getTopTile() {
    return topTile;
  }

  public double getxCoordinates() {
    return imageViewHorizontalCoordinates;
  }

  public double getyCoordinates() {
    return imageViewVerticalCoordinates;
  }
}
