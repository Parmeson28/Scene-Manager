package com.javaProjects.game.ui;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class EditorUi extends UiManager{

    public boolean isSetupDone = false;
    public int width, height;

    // Setup screen
    private Window setupWindow;
    private TextField widthField;
    private TextField heightField;

    // Right click menu
    private Window objectMenu;
    private Consumer<String> pendingPick;

    public EditorUi(){
        super();
        buildObjectMenu();
    }

    public void setupScreen(){
        setupWindow = createWindow("New map");

        widthField = createNumberField("32");
        heightField = createNumberField("32");

        addRow(setupWindow, createTextLabel("Width:"), widthField);
        addRow(setupWindow, createTextLabel("Height:"), heightField);
        addRow(setupWindow, createButton("Create map", this::confirmSetup));

        showWindow(setupWindow);
    }

    private void confirmSetup(){
        int w = readInt(widthField, 0);
        int h = readInt(heightField, 0);

        if (w > 0 && h > 0) {
            width = w;
            height = h;
            hideWindow(setupWindow);
            setSetupDone(true);
        }
    }

    private void buildObjectMenu(){
        objectMenu = createWindow("Object type");

        addRow(objectMenu, createButton("Structure", () -> pickObject("structure")));
        addRow(objectMenu, createButton("Waypoint", () -> pickObject("waypoint")));
    }

    
    /** Opens a small form with one text field per hint. Calls onConfirm with the typed values. */
    public void openForm(String title, String[] hints, Consumer<String[]> onConfirm){
        Window form = createWindow(title);

        TextField[] fields = new TextField[hints.length];
        for (int i = 0; i < hints.length; i++) {
            fields[i] = createTextField(hints[i]);
            addRow(form, fields[i]);
        }

        addRow(form, createButton("Confirm", () -> {
            String[] values = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                values[i] = fields[i].getText().trim();
            }
            if (values[0].isEmpty()) return;   // first field (name) is required

            form.remove();                      // removes it from the stage
            onConfirm.accept(values);
        }));
        addRow(form, createButton("Cancel", form::remove));

        showWindowAtCursor(form);
        stage.setKeyboardFocus(fields[0]);
    }

    public void openObjectMenu(Consumer<String> onPick){
        if (!isSetupDone) return;

        pendingPick = onPick;
        showWindowAtCursor(objectMenu);
        
    }

    private void pickObject(String type){
        hideWindow(objectMenu);

        if (pendingPick != null) {
            pendingPick.accept(type);
            pendingPick = null;
        }
    }

    public boolean isSetupDone() {
        return isSetupDone;
    }

    public void setSetupDone(boolean isSetupDone) {
        this.isSetupDone = isSetupDone;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}