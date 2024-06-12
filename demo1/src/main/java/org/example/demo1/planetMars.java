package org.example.demo1;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class planetMars extends planetEarth{
    private String name = "MARS";
    private final String imagePath = "file:src/main/resources/org/example/demo1/planetMars.png";
    private ArrayList<LightPlane> currentPlanes = new ArrayList<>();
    public planetMars(Group group) {
        super(500, 200, group);
    }
    public planetMars(int x, int y, Group group) {
        super(x, y, group);
    }

    @Override
    public ArrayList<LightPlane> getCurrentPlanes() {
        return currentPlanes;
    }

    @Override
    public void draw() {
        Rectangle rec = new Rectangle();
        rec.setStyle("-fx-stroke: white; -fx-stroke-width: 2; -fx-stroke-dash-array: 10 15;");
        rec.setFill(Color.TRANSPARENT);
        rec.setHeight(this.size);
        rec.setWidth(this.size);
        rec.setLayoutX(this.x);
        rec.setLayoutY(this.y);
        group.getChildren().add(rec);

        Label currentPl = new Label(String.valueOf(this.currentPlanes.size()));
        currentPl.setTextFill(Color.WHITE);
        currentPl.setStyle("-fx-font-weight: bold");
        currentPl.setLayoutX(this.x + 235);
        currentPl.setLayoutY(this.y);
        group.getChildren().add(currentPl);

        Label objname = new Label(this.name);
        objname.setTextFill(Color.YELLOW);
        objname.setStyle("-fx-font-weight: bold");
        objname.setLayoutX(this.x + 5);
        objname.setLayoutY(this.y);
        group.getChildren().add(objname);

        ImageView image = new ImageView(imagePath);
        image.setFitHeight(this.size);
        image.setFitWidth(this.size);
        image.setX(this.x);
        image.setY(this.y);
        group.getChildren().add(image);
    }
    @Override
    public void rerender() {
        this.group.getChildren().clear();
        draw();
    }
}
