package com.javaProjects.game.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class EditorUi extends UiManager{

    public boolean isSetupDone = false;
    public int width, height;

    // Main menu
    private Window mainMenuWindow;

    // Setup screen (new map)
    private Window setupWindow;
    private TextField widthField;
    private TextField heightField;

    // Load screen
    private Window loadWindow;
    private String loadedFileName; // null = new map, non-null = loaded map

    // Right click menu
    private Window objectMenu;
    private Consumer<String> pendingPick;

    public EditorUi(){
        super();
        buildObjectMenu();
    }

    // ---- Main Menu ----

    public void mainMenu(){
        mainMenuWindow = createWindow("Scene Manager");

        addRow(mainMenuWindow, createButton("Create New Map", this::showNewMapSetup));
        addRow(mainMenuWindow, createButton("Load Map", this::showLoadScreen));

        showWindow(mainMenuWindow);
    }

    private void showNewMapSetup(){
        hideWindow(mainMenuWindow);
        setupScreen();
    }

    private void showLoadScreen(){
        hideWindow(mainMenuWindow);

        loadWindow = createWindow("Load Map");

        FileHandle savesDir = Gdx.files.local("assets/saves/");
        boolean hasSaves = false;

        if (savesDir.exists() && savesDir.isDirectory()) {
            for (FileHandle file : savesDir.list()) {
                if (!file.name().endsWith(".json")) continue;
                String name = file.nameWithoutExtension();
                addRow(loadWindow, createButton(name, () -> confirmLoad(name)));
                hasSaves = true;
            }
        }

        if (!hasSaves) {
            addRow(loadWindow, createTextLabel("No saved maps found."));
        }

        addRow(loadWindow, createButton("Back", () -> {
            hideWindow(loadWindow);
            showWindow(mainMenuWindow);
        }));

        showWindow(loadWindow);
    }

    private void confirmLoad(String fileName){
        loadedFileName = fileName;
        hideWindow(loadWindow);
        setSetupDone(true);
    }

    // ---- Setup Screen (new map) ----

    public void setupScreen(){
        setupWindow = createWindow("New Map");

        widthField = createNumberField("32");
        heightField = createNumberField("32");

        addRow(setupWindow, createTextLabel("Width:"), widthField);
        addRow(setupWindow, createTextLabel("Height:"), heightField);
        addRow(setupWindow, createButton("Create", this::confirmSetup));
        addRow(setupWindow, createButton("Back", () -> {
            hideWindow(setupWindow);
            showWindow(mainMenuWindow);
        }));

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
    public List<String> openForm(String title, String[] hints, Consumer<List<String>> onConfirm){
        Window form = createWindow(title);
        
        List<String> values = new ArrayList<>();

        TextField[] fields = new TextField[hints.length];
        for (int i = 0; i < hints.length; i++) {
            fields[i] = createTextField(hints[i]);
            addRow(form, fields[i]);
        }

        addRow(form, createButton("Confirm", () -> {
            for (TextField f : fields) values.add(f.getText().trim());

            if (values.get(0).isEmpty()) return; 

            form.remove();
            onConfirm.accept(values);
        }));
        addRow(form, createButton("Cancel", form::remove));

        showWindowAtCursor(form);
        stage.setKeyboardFocus(fields[0]);
        return values;
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

    public String getLoadedFileName(){
        return loadedFileName;
    }
}