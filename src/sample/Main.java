package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.util.stream.IntStream;

public class Main extends Application {

    static final int SCALE = 3;
    static final int SPRITE_SIZE = 32;
    static final int CELL_SIZE = SPRITE_SIZE * SCALE;
    static final int HORIZONTAL_CELLS = 10;
    static final int VERTICAL_CELLS = 7;
    static final int BOARD_WIDTH = HORIZONTAL_CELLS * CELL_SIZE;
    static final int BOARD_HEIGHT = VERTICAL_CELLS * CELL_SIZE;
    static MapObject[][] map = new MapObject[HORIZONTAL_CELLS][VERTICAL_CELLS];

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mary Had a Little Lambda");
        Group root = new Group();
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT, Color.WHITE);
        primaryStage.setScene(scene);
        populateBackground(root);

        root.getChildren().add(new MapObject.Barn(new Location(2, 3)));
        root.getChildren().add(new MapObject.Rainbow(new Location(5, 0)));
        root.getChildren().add(new MapObject.Church(new Location(6, 2)));
        root.getChildren().add(new MapObject.ChickenCoop(new Location(5, 4)));
        root.getChildren().add(new MapObject.Nest(new Location(3, 5)));
        MapObject.Fox fox = new MapObject.Fox(new Location(9, 4));
        fox.setDirection(Direction.LEFT);
        fox.setScaleX(.5);
        fox.setScaleY(.5);
        root.getChildren().add(fox);

        SpriteView.Mary mary = new SpriteView.Mary(new Location(0, 3));
        root.getChildren().add(mary);
        addKeyHandler(scene, mary);

        primaryStage.show();
    }

    private void populateBackground(Group root) {
        // Image by Victor Szalvay: http://www.flickr.com/photos/55502991@N00/172603855
        ImageView background = new ImageView(getClass().getResource("images/field.jpg").toString());
        background.setFitHeight(BOARD_HEIGHT);

// Gratuitous use of lambdas to do nested iteration!
//        for (int i = 0; i < HORIZONTAL_CELLS; i++) {
//            for (int j = 0; j < VERTICAL_CELLS; j++) {
        Group cells = new Group(IntStream.range(0, HORIZONTAL_CELLS).mapToObj(i ->
            IntStream.range(0, VERTICAL_CELLS).mapToObj(j -> {
                Rectangle rect = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                rect.setFill(null);
                rect.setStrokeType(StrokeType.INSIDE);
                rect.setStroke(Color.BLACK);
                rect.getStrokeDashArray().setAll(0.7 * CELL_SIZE / 4, 0.3 * CELL_SIZE / 4);
                rect.setStrokeDashOffset(0.35 * CELL_SIZE / 4);
                return rect;
            })
        ).flatMap(s -> s).toArray(Rectangle[]::new));

        root.getChildren().addAll(background, cells);
    }

    private void addKeyHandler(Scene scene, SpriteView mary) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            KeyCode keyCode = ke.getCode();
            switch (keyCode) {
                case W:
                case UP:
                    mary.move(Direction.UP);
                    break;
                case A:
                case LEFT:
                    mary.move(Direction.LEFT);
                    break;
                case S:
                case DOWN:
                    mary.move(Direction.DOWN);
                    break;
                case D:
                case RIGHT:
                    mary.move(Direction.RIGHT);
                    break;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static enum Direction {
        DOWN(0), LEFT(1), RIGHT(2), UP(3);
        private final int offset;
        Direction(int offset) {
            this.offset = offset;
        }
        public int getOffset() {
            return offset;
        }
        public int getXOffset() {
            switch (this) {
                case LEFT:
                    return -1;
                case RIGHT:
                    return 1;
                default:
                    return 0;
            }
        }
        public int getYOffset() {
            switch (this) {
                case UP:
                    return -1;
                case DOWN:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    public static class Location {
        int cell_x;
        int cell_y;
        public Location(int cell_x, int cell_y) {
            this.cell_x = cell_x;
            this.cell_y = cell_y;
        }
        public int getX() {
            return cell_x;
        }
        public int getY() {
            return cell_y;
        }
        public Location offset(int x, int y) {
            return new Location(cell_x + x, cell_y + y);
        }
        public Direction directionTo(Location loc) {
            if (Math.abs(loc.cell_x - cell_x) > Math.abs(loc.cell_y - cell_y)) {
                return (loc.cell_x > cell_x) ? Direction.RIGHT : Direction.LEFT;
            } else {
                return (loc.cell_y > cell_y) ? Direction.DOWN : Direction.UP;
            }
        }
    }

}
