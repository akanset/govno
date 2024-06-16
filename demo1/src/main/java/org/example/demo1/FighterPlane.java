package org.example.demo1;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FighterPlane extends LightPlane implements Cloneable{
    private String imageNormalPath = "file:src/main/resources/org/example/demo1/fighter.png";
    private String imageReversedPath = "file:src/main/resources/org/example/demo1/fighter_reversed.png";
    private ImageView image = new ImageView();
    private ImageView imageNormal = new ImageView(imageNormalPath);
    private ImageView imageReversed = new ImageView(imageReversedPath);

    public FighterPlane(String name, int x, int y) {
        super(name, x, y);
        this.spd = 30;
        this.dmg = 45;
    }
    public FighterPlane() {
        super();
    }
    public FighterPlane(String name, double hp, int dmg, double spd) {
        this.spd = spd;
        this.hp = hp;
        this.dmg = dmg;
        this.name = name;
        this.checkStats();
    }
    @Override
    public String toString(){
        String res = "Fighter Plane {"
                + "Name = " + this.name
                + ", HP = " + this.hp
                + ", Damage = " + this.dmg
                + ", Speed = " + this.spd
                + "}" + '\n';
        //res += this.name + "'s system {" + systemStats[0] + systemStats[1] + systemStats[2] + systemStats[3];
        return res;
    }

    @Override
    public void draw() {
        Rectangle rec = new Rectangle();
        if(belongs) {
            rec.setStyle("-fx-stroke: cyan; -fx-stroke-width: 2;");
        }
        else {
            if(isActive) {
                rec.setStyle("-fx-stroke: green; -fx-stroke-width: 2;");
            }
            else {
                rec.setStyle("-fx-stroke: red; -fx-stroke-width: 2;");
            }
        }
        rec.setFill(Color.TRANSPARENT);
        rec.setHeight(size);
        rec.setWidth(size);
        rec.setLayoutX(this.x);
        rec.setLayoutY(this.y);

        if(!this.isReversed()) {
            image.setImage(imageNormal.getImage());
        }
        else {
            image.setImage(imageReversed.getImage());
        }
        image.setX(this.x+2);
        image.setY(this.y+23);
        Label nameLabel = new Label(this.name);
        nameLabel.setStyle("-fx-font-weight: bold");
        nameLabel.setTextFill(Color.color(1, 1, 0));
        nameLabel.setLayoutX(this.x);
        nameLabel.setLayoutY(this.y+2);

        Label currentHP = new Label("HP: " + (int) this.getHP());
        currentHP.setLayoutX(this.x + this.size - 45);
        currentHP.setLayoutY(this.y - 20);
        currentHP.setStyle("-fx-font-weight: bold");
        currentHP.setTextFill(Color.color(1, 1, 1));

        Label coordsLabel = new Label("X: " + this.x + '\n' + "Y: " + this.y);
        coordsLabel.setTextFill(Color.color(1, 1, 1));
        coordsLabel.setLayoutY(this.y - 40);
        coordsLabel.setLayoutX(this.x);

        Label activeLbl = new Label("Active: " + this.isActive);
        ((Runnable) () -> activeLbl.setTextFill(isActive ? Color.DARKGREEN : Color.RED)).run();
        activeLbl.setStyle("-fx-font-weight: bold");
        activeLbl.setLayoutX(this.getX() + 17);
        activeLbl.setLayoutY(this.y + size);

        group.getChildren().add(rec);
        group.getChildren().add(image);
        group.getChildren().add(nameLabel);
        group.getChildren().add(coordsLabel);
        group.getChildren().add(activeLbl);
        group.getChildren().add(currentHP);
    }
}
