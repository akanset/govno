package org.example.demo1;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.example.demo1.HelloApplication.mainGroup;

public class LightPlane implements Cloneable{
    protected double spd;
    protected double hp;
    protected int dmg;
    protected String name;
    protected String[] systemStats = new String[4];
    protected boolean isActive;
    protected boolean isReversed;
    protected Group group;
    protected int x = 0;
    protected int y = 0;
    private final String imageNormalPath = "file:src/main/resources/org/example/demo1/light.png";
    private final String imageReversedPath = "file:src/main/resources/org/example/demo1/light_reversed.png";
    protected ImageView image = new ImageView();
    protected ImageView imageNormal = new ImageView(imageNormalPath);
    protected ImageView imageReversed = new ImageView(imageReversedPath);
    protected static int size = 100;
    protected boolean belongs;
    protected String currentPlanet;

    public LightPlane(String name, double hp, int dmg, double spd) {
        this.spd = spd;
        this.hp = hp;
        this.dmg = dmg;
        this.name = name;
        this.checkStats();
    }
    public LightPlane(String name){
        this(name, 100.0, 20, 20);
    }
    public LightPlane() {
        this("DefaultLightPlane", 100.0, 20, 20);
        this.checkStats();
    }
    public LightPlane(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;

        this.hp = 100;
        this.dmg = 20;
        this.spd = 20.0;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public boolean isActive() {
        return isActive;
    }
    public boolean isReversed() {
        return isReversed;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setSystemStats(String[] systemStats) {
        this.systemStats = systemStats;
    }
    public void setReversed(boolean option){
        this.isReversed = option;
    }

    public String getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentPlanet(String currentPlanet) {
        this.currentPlanet = currentPlanet;
    }

    public void changeStats (LightPlane plane) {
        Stage changeStats = new Stage();

        VBox box = new VBox();
        Group group = new Group();

        Label nameLabel = new Label("Enter new name:");
        nameLabel.setFont(Font.font("Times New Roman", 14));

        TextField objname = new TextField();
        objname.setText(this.getName());
        objname.setFont(Font.font("Times New Roman", 14));

        Label dmgLabel = new Label("Enter new damage:");
        dmgLabel.setFont(Font.font("Times New Roman", 14));

        TextField objDMG = new TextField();
        objDMG.setText(String.valueOf(this.getDamage()));
        objDMG.setFont(Font.font("Times new Roman", 14));

        Label spdLabel = new Label("Enter new speed");
        spdLabel.setFont(Font.font("Times New Roman", 14));

        TextField objSPD = new TextField();
        objSPD.setText(String.valueOf(this.getSpeed()));
        objSPD.setFont(Font.font("Times New Roman", 14));

        Label hpLabel = new Label("Enter new HP");
        hpLabel.setFont(Font.font("Times New Roman", 14));

        TextField objHP = new TextField();
        objHP.setText(String.valueOf(this.getHP()));
        objHP.setFont(Font.font("Times New Roman", 14));

        Button btn = new Button("OK");
        btn.setFont(Font.font("Times New Roman", 14));

        RadioButton makeActive = new RadioButton();
        makeActive.setText("Make Active");
        if(this.isActive()) {
            makeActive.setSelected(true);
        }

        box.setSpacing(10);
        box.getChildren().addAll(nameLabel, objname, hpLabel, objHP, dmgLabel, objDMG, spdLabel, objSPD, makeActive, btn);

        box.setAlignment(Pos.CENTER);

        group.getChildren().add(box);

        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                plane.setName(objname.getText());
                plane.setHP(Double.parseDouble(objHP.getText()));
                plane.setDamage(Integer.parseInt(objDMG.getText()));
                plane.setSpeed(Double.parseDouble(objSPD.getText()));
                plane.setActive(makeActive.isSelected());

                plane.rerender();
                changeStats.close();
            }
        });

        Scene s = new Scene(group);

        changeStats.setTitle("Change stats of object");
        changeStats.setWidth(400);
        changeStats.setHeight(350);
        changeStats.setScene(s);
        changeStats.show();
    }

    public void checkIntersect(planetEarth earth, planetMars mars, planetKepler kepler) {

        if(this.getX()+(this.getSize()/2) >= earth.getX() && this.getX()+(this.getSize()/2) <= earth.getX()+earth.getSize()) {
            if(this.getY()+(this.getSize()/2) >= earth.getY() && this.getY()+(this.getSize()/2) <= earth.getY()+earth.getSize()) {
                if(!earth.getCurrentPlanes().contains(this)){
                    earth.getCurrentPlanes().add(this);
                    this.belongs = true;
                    this.setCurrentPlanet("Earth");
                }
            }
            else if(earth.getCurrentPlanes().contains(this)) {
                earth.getCurrentPlanes().remove(this);
                this.belongs = false;
            }
        }
        else if(earth.getCurrentPlanes().contains(this)) {
            earth.getCurrentPlanes().remove(this);
            this.belongs = false;
        }

        if(this.getX()+(this.getSize()/2) >= mars.getX() && this.getX()+(this.getSize()/2) <= mars.getX()+mars.getSize()) {
            if(this.getY()+(this.getSize()/2) >= mars.getY() && this.getY()+(this.getSize()/2) <= mars.getY()+mars.getSize()) {
                if(!mars.getCurrentPlanes().contains(this)){
                    mars.getCurrentPlanes().add(this);
                    this.belongs = true;
                    this.setCurrentPlanet("Mars");
                }
            }
            else if(mars.getCurrentPlanes().contains(this)) {
                mars.getCurrentPlanes().remove(this);
                this.belongs = false;
            }
        }
        else if(mars.getCurrentPlanes().contains(this)) {
            mars.getCurrentPlanes().remove(this);
            this.belongs = false;
        }

        if(this.getX()+(this.getSize()/2) >= kepler.getX() && this.getX()+(this.getSize()/2) <= kepler.getX()+kepler.getSize()) {
            if(this.getY()+(this.getSize()/2) >= kepler.getY() && this.getY()+(this.getSize()/2) <= kepler.getY()+kepler.getSize()) {
                if(!kepler.getCurrentPlanes().contains(this)) {
                    kepler.getCurrentPlanes().add(this);
                    this.belongs = true;
                    this.setCurrentPlanet("Kepler");
                }
            }
            else if(kepler.getCurrentPlanes().contains(this)) {
                kepler.getCurrentPlanes().remove(this);
                this.belongs = false;
            }
        }
        else if(kepler.getCurrentPlanes().contains(this)) {
            kepler.getCurrentPlanes().remove(this);
            this.belongs = false;
        }
    }

    @Override
    public LightPlane clone() throws CloneNotSupportedException {
        LightPlane cloned = (LightPlane) super.clone();
        String[] arrayToSet = (String[]) Arrays.stream(this.systemStats).toArray();
        cloned.setSystemStats(arrayToSet);
        return cloned;
    }
    @Override
    public String toString(){
        String res = "Light Plane {"
                + "Name = " + this.name
                + ", HP = " + this.hp
                + ", Damage = " + this.dmg
                + ", Speed = " + this.spd
                + "}" + '\n';
        //res += this.name + "'s system {" + systemStats[0] + systemStats[1] + systemStats[2] + systemStats[3];
        return res;
    }
    public void checkStats() {
        if(!this.name.isBlank()) systemStats[0] = "Identification: success!" + '\n';
        else systemStats[0] = "Identification: failed!" + '\n';

        if(this.hp != 0 && this.hp > 0) systemStats[1] = "Health: operable." + '\n';
        else systemStats[1] = "Health: non-operable." + '\n';

        if(this.spd > 0) systemStats[2] = "Speed: operable." + '\n';
        else systemStats[2] = "Speed: non-operable." + '\n';

        if(this.dmg != 0) systemStats[3] = "Weapons: operable.";
        else systemStats[3] = "Weapons: non-operable.";
    }

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LightPlane that = (LightPlane) object;
        return Double.compare(spd, that.spd) == 0 && Double.compare(hp, that.hp) == 0 && dmg == that.dmg && Objects.equals(name, that.name);
    }
    public void draw(){
        Label activeLbl = new Label("Active: " + this.isActive);
        activeLbl.setStyle("-fx-font-weight: bold");
        ((Runnable) () -> activeLbl.setTextFill(isActive ? Color.DARKGREEN : Color.RED)).run();
        activeLbl.setLayoutX(this.x + 17);
        activeLbl.setLayoutY(this.y + size);

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
        image.setX(this.x+27);
        image.setY(this.y+27);
        Label nameLabel = new Label(this.name);
        nameLabel.setStyle("-fx-font-weight: bold");
        nameLabel.setTextFill(Color.color(1, 1, 0));
        nameLabel.setLayoutX(this.x);
        nameLabel.setLayoutY(this.y+2);

        Label coordsLabel = new Label("X: " + this.x + "; Y: " + this.y);
        coordsLabel.setTextFill(Color.color(1, 1, 1));
        coordsLabel.setLayoutY(this.y - 20);
        coordsLabel.setLayoutX(this.x);

        group.getChildren().add(rec);
        group.getChildren().add(image);
        group.getChildren().add(nameLabel);
        group.getChildren().add(coordsLabel);
        group.getChildren().add(activeLbl);
    }

    public void rerender() {
        this.group.getChildren().clear();
        draw();
    }
    //GETTERS
    public int getDamage() {
        return this.dmg;
    }
    public double getSpeed() {
        return this.spd;
    }
    public double getHP() {
        return this.hp;
    }
    public String getName() {
        return this.name;
    }

    public static int getSize() {
        return size;
    }

    public boolean isBelongs() {
        return belongs;
    }

    //SETTERS
    public void setDamage(int n) {
        this.dmg = n;
    }
    public void setSpeed(double n) {
        this.spd = n;
    }
    public void setHP(double n) {
        LightPlane.this.hp = n;
    }
    public void setName(String line) {
        this.name = line;
    }
    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    //ACTIVITIES
    public void AttackShip(LightPlane object) {
        object.setHP(object.getHP() - this.getDamage());
        System.out.println(this.getName() + " attacked " + object.getName() + "!");
    }
    public void AttackedByShip(LightPlane object) {
        this.setHP(this.getHP() - object.getDamage());
    }
    public void RepairShip(LightPlane object) {
        if(object.hp != 100) {
            object.setHP(100.0);
            this.setHP(this.getHP() - 15.0);
        }
    }
    public void PutShieldOnShip(LightPlane object) {
        object.setHP(200);
        this.setHP(this.getHP()/2);
    }
}
