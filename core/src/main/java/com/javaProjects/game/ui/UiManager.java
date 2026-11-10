package com.javaProjects.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * "Macro" layer over Scene2D. Build UIs with the create... / addRow / show... functions
 * and never touch Stage, Table, Cell or listeners directly.
 *
 * Typical use (inside a subclass):
 *   Window w = createWindow("Title");
 *   TextField f = createNumberField("32");
 *   addRow(w, createTextLabel("Size:"), f);
 *   addRow(w, createButton("OK", () -> { ... }));
 *   showWindow(w);
 */
public class UiManager {

    private static final float FIELD_WIDTH = 200f;
    private static final float BUTTON_WIDTH = 160f;
    private static final float ROW_HEIGHT = 40f;
    private static final float PAD = 4f;

    public Stage stage;
    public Skin skin;

    public UiManager(){
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/metalui/metal-ui.json"));

        Gdx.input.setInputProcessor(stage);
    }

    // ------------------------------------------------------------------
    // Create widgets
    // ------------------------------------------------------------------

    /** Creates a hidden, fixed-position window. Fill it with addRow, then call showWindow. */
    public Window createWindow(String title){
        Window window = new Window(title, skin);
        window.setMovable(false);
        window.pad(12);
        window.padTop(36); // room for the title bar
        window.setVisible(false);
        stage.addActor(window);
        return window;
    }

    public Label createTextLabel(String text){
        return new Label(text, skin);
    }

    /** Button that runs onClick when clicked. */
    public TextButton createButton(String text, Runnable onClick){
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (onClick != null) onClick.run();
            }
        });
        return button;
    }

    /** Free text input. The hint is shown while the field is empty. */
    public TextField createTextField(String hint){
        TextField field = new TextField("", skin);
        field.setMessageText(hint);
        return field;
    }

    /** Input that only accepts digits. */
    public TextField createNumberField(String defaultValue){
        TextField field = new TextField(defaultValue, skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        return field;
    }

    /** Reads a number field. Returns the fallback if it is empty or invalid. */
    public int readInt(TextField field, int fallback){
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    // ------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------

    /**
     * Adds one row to a window (or any table). Two actors = two columns (label + field).
     * A single actor spans the whole row. Sizes for fields and buttons are applied for you.
     */
    public void addRow(Table table, Actor... actors){
        for (Actor actor : actors) {
            Cell<Actor> cell = table.add(actor).pad(PAD);

            if (actor instanceof TextField) {
                cell.width(FIELD_WIDTH).height(ROW_HEIGHT);
            } else if (actor instanceof TextButton) {
                cell.width(BUTTON_WIDTH).height(ROW_HEIGHT);
            }

            if (actors.length == 1) {
                cell.colspan(2);
            }
        }
        table.row();
    }

    // ------------------------------------------------------------------
    // Show / hide
    // ------------------------------------------------------------------

    /** Sizes the window to its content and shows it in the center of the screen. */
    public void showWindow(Window window){
        window.pack();
        window.setPosition(
            Math.round((stage.getWidth() - window.getWidth()) / 2f),
            Math.round((stage.getHeight() - window.getHeight()) / 2f));
        window.setVisible(true);
        window.toFront();
    }

    /** Sizes the window to its content and shows it with its top-left corner at the mouse. */
    public void showWindowAtCursor(Window window){
        window.pack();
        Vector2 p = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float x = MathUtils.clamp(p.x, 0, stage.getWidth() - window.getWidth());
        float y = MathUtils.clamp(p.y - window.getHeight(), 0, stage.getHeight() - window.getHeight());
        window.setPosition(x, y);
        window.setVisible(true);
        window.toFront();
    }

    public void hideWindow(Window window){
        window.setVisible(false);
    }

    // ------------------------------------------------------------------
    // Frame plumbing
    // ------------------------------------------------------------------

    /** True if the mouse is over any visible UI element. Use it to block game clicks. */
    public boolean isPointerOverUI(){
        Vector2 p = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        return stage.hit(p.x, p.y, true) != null;
    }

    /** Call every frame, after the game is drawn. */
    public void drawUi(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
}