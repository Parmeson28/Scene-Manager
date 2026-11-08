package com.javaProjects.game.ui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EditorUi {

    public static final String TYPE_STRUCTURE = "Structure";
    public static final String TYPE_WAYPOINT = "Waypoint";

    /** Data filled in by the player in the "new object" form. */
    public static class NewObjectData {
        public final String name;
        public final String type;
        public final float x, y;

        public NewObjectData(String name, String type, float x, float y) {
            this.name = name;
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    private static final int MIN_TILES = 1;
    private static final int MAX_TILES = 512;
    private static final float MENU_WIDTH = 220f;
    private static final float MENU_LIST_MAX_HEIGHT = 140f;

    private final Stage stage;
    private final Skin skin;

    private final Table panel;
    private final Table setupTable;

    private final IntConsumer onTileSelected;
    private final BiConsumer<Integer, Integer> onMapCreated;

    private Label selectedLabel;
    private TextField widthField;
    private TextField heightField;

    private boolean setupDone = false;

    // ---- context menu ----
    private Consumer<String> onStructureSelected = s -> {};
    private Consumer<String> onWaypointSelected = s -> {};
    private Consumer<NewObjectData> onObjectCreated = d -> {};

    private final Window contextMenu;
    private final Window newObjectWindow;
    private TextField objNameField;
    private SelectBox<String> objTypeBox;
    private TextField objXField;
    private TextField objYField;

    public EditorUi(IntConsumer onTileSelected, BiConsumer<Integer, Integer> onMapCreated) {
        this.onTileSelected = onTileSelected;
        this.onMapCreated = onMapCreated;

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/metalui/metal-ui.json"));

        panel = new Table();
        panel.setFillParent(true);
        panel.top().left().pad(10);
        panel.setVisible(false);
        stage.addActor(panel);
        buildPanel();

        setupTable = new Table();
        setupTable.setFillParent(true);
        stage.addActor(setupTable);
        buildSetup();

        contextMenu = new Window("", skin);
        contextMenu.setMovable(false);
        contextMenu.setVisible(false);
        stage.addActor(contextMenu);

        newObjectWindow = new Window("Novo objeto", skin);
        newObjectWindow.setVisible(false);
        stage.addActor(newObjectWindow);
        buildNewObjectWindow();

        // Click outside the menu closes it
        stage.addCaptureListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (contextMenu.isVisible() && !contextMenu.isAscendantOf(event.getTarget())) {
                    contextMenu.setVisible(false);
                }
                return false; // never consume
            }
        });
    }

    // ------------------------------------------------------------------
    // Setup / tile panel
    // ------------------------------------------------------------------
    private void buildSetup() {
        setupTable.add(new Label("Tamanho do mapa (em tiles)", skin)).colspan(2).padBottom(12);
        setupTable.row();

        widthField = createNumberField("32");
        heightField = createNumberField("32");

        setupTable.add(new Label("Largura:", skin)).right().padRight(8).padBottom(6);
        setupTable.add(widthField).width(120).padBottom(6);
        setupTable.row();

        setupTable.add(new Label("Altura:", skin)).right().padRight(8).padBottom(12);
        setupTable.add(heightField).width(120).padBottom(12);
        setupTable.row();

        TextButton create = new TextButton("Criar mapa", skin);
        create.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmSetup();
            }
        });
        setupTable.add(create).colspan(2).width(160).height(40);

        stage.setKeyboardFocus(widthField);
    }

    private TextField createNumberField(String defaultText) {
        TextField field = new TextField(defaultText, skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        return field;
    }

    private void confirmSetup() {
        int w = parse(widthField.getText());
        int h = parse(heightField.getText());

        setupDone = true;
        setupTable.setVisible(false);
        panel.setVisible(true);
        stage.setKeyboardFocus(null);

        onMapCreated.accept(w, h);
    }

    private int parse(String text) {
        try {
            return MathUtils.clamp(Integer.parseInt(text.trim()), MIN_TILES, MAX_TILES);
        } catch (NumberFormatException e) {
            return 32;
        }
    }

    private void buildPanel() {
        selectedLabel = new Label("Tile: 1", skin);
        panel.add(selectedLabel).left().padBottom(8);
        panel.row();

        for (int i = 1; i <= 2; i++) {
            addTileButton(i);
        }
    }

    private void addTileButton(final int tileId) {
        TextButton button = new TextButton("Tile " + tileId, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectTile(tileId);
            }
        });
        panel.add(button).left().width(100).height(40).padBottom(4);
        panel.row();
    }

    public void selectTile(int tileId) {
        selectedLabel.setText("Tile: " + tileId);
        onTileSelected.accept(tileId);
    }

    // ------------------------------------------------------------------
    // Context menu
    // ------------------------------------------------------------------

    public void setOnStructureSelected(Consumer<String> c) { this.onStructureSelected = c; }
    public void setOnWaypointSelected(Consumer<String> c)  { this.onWaypointSelected = c; }
    public void setOnObjectCreated(Consumer<NewObjectData> c) { this.onObjectCreated = c; }

    /**
     * Opens the menu at the cursor.
     *
     * @param screenX    Gdx.input.getX()
     * @param screenY    Gdx.input.getY()
     * @param worldPos   clicked position in world coordinates (used to prefill X/Y)
     * @param structures names of existing structures
     * @param waypoints  names of existing waypoints
     */
    public void showContextMenu(float screenX, float screenY, final Vector2 worldPos,
                                Array<String> structures, Array<String> waypoints) {
        if (!setupDone) return;

        newObjectWindow.setVisible(false);
        contextMenu.clearChildren();
        contextMenu.pad(8);
        contextMenu.defaults().left().growX().padBottom(4);

        addSection(structures, "Structures", true);
        contextMenu.add(new Label("----------", skin)).row();
        addSection(waypoints, "Waypoints", false);
        contextMenu.add(new Label("----------", skin)).row();

        TextButton add = new TextButton("+ Adicionar novo objeto", skin);
        add.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                contextMenu.setVisible(false);
                openNewObjectWindow(worldPos);
            }
        });
        contextMenu.add(add).height(36).row();

        contextMenu.setWidth(MENU_WIDTH);
        contextMenu.pack();
        contextMenu.setWidth(MENU_WIDTH);

        // top-left corner at the cursor, clamped inside the screen
        Vector2 s = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        float x = MathUtils.clamp(s.x, 0, stage.getWidth() - contextMenu.getWidth());
        float y = MathUtils.clamp(s.y - contextMenu.getHeight(), 0, stage.getHeight() - contextMenu.getHeight());
        contextMenu.setPosition(x, y);

        contextMenu.setVisible(true);
        contextMenu.toFront();
    }

    private void addSection(Array<String> names, String title, final boolean structure) {
        contextMenu.add(new Label(title, skin)).row();

        Table list = new Table(skin);
        list.defaults().left().growX().padBottom(2);

        if (names == null || names.size == 0) {
            list.add(new Label("(nenhum)", skin)).row();
        } else {
            for (final String name : names) {
                TextButton b = new TextButton(name, skin);
                b.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        contextMenu.setVisible(false);
                        if (structure) onStructureSelected.accept(name);
                        else onWaypointSelected.accept(name);
                    }
                });
                list.add(b).height(30).row();
            }
        }

        ScrollPane scroll = new ScrollPane(list, skin);
        scroll.setScrollingDisabled(true, false);
        scroll.setFadeScrollBars(false);
        float h = Math.min(list.getPrefHeight(), MENU_LIST_MAX_HEIGHT);
        contextMenu.add(scroll).height(h).row();
    }

    // ------------------------------------------------------------------
    // "New object" form
    // ------------------------------------------------------------------

    private void buildNewObjectWindow() {
        TextField.TextFieldFilter floatFilter =
                (field, c) -> Character.isDigit(c) || c == '.' || c == '-';

        objNameField = new TextField("", skin);
        objNameField.setMessageText("Nome");

        objTypeBox = new SelectBox<>(skin);
        objTypeBox.setItems(TYPE_STRUCTURE, TYPE_WAYPOINT);

        objXField = new TextField("0", skin);
        objXField.setTextFieldFilter(floatFilter);
        objYField = new TextField("0", skin);
        objYField.setTextFieldFilter(floatFilter);

        newObjectWindow.pad(12);
        newObjectWindow.padTop(32);
        newObjectWindow.defaults().pad(3);

        newObjectWindow.add(new Label("Nome:", skin)).right();
        newObjectWindow.add(objNameField).width(180).row();

        newObjectWindow.add(new Label("Tipo:", skin)).right();
        newObjectWindow.add(objTypeBox).width(180).row();

        newObjectWindow.add(new Label("X:", skin)).right();
        newObjectWindow.add(objXField).width(180).row();

        newObjectWindow.add(new Label("Y:", skin)).right();
        newObjectWindow.add(objYField).width(180).row();

        TextButton cancel = new TextButton("Cancelar", skin);
        cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                closeNewObjectWindow();
            }
        });

        TextButton ok = new TextButton("Criar", skin);
        ok.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmNewObject();
            }
        });

        Table buttons = new Table();
        buttons.add(cancel).width(90).height(34).padRight(8);
        buttons.add(ok).width(90).height(34);
        newObjectWindow.add(buttons).colspan(2).padTop(8);

        newObjectWindow.pack();
    }

    private void openNewObjectWindow(Vector2 worldPos) {
        objNameField.setText("");
        objXField.setText(String.valueOf(MathUtils.round(worldPos.x * 100f) / 100f));
        objYField.setText(String.valueOf(MathUtils.round(worldPos.y * 100f) / 100f));

        newObjectWindow.pack();
        newObjectWindow.setPosition(
                Math.round((stage.getWidth() - newObjectWindow.getWidth()) / 2f),
                Math.round((stage.getHeight() - newObjectWindow.getHeight()) / 2f));
        newObjectWindow.setVisible(true);
        newObjectWindow.toFront();
        stage.setKeyboardFocus(objNameField);
    }

    private void closeNewObjectWindow() {
        newObjectWindow.setVisible(false);
        stage.setKeyboardFocus(null);
    }

    private void confirmNewObject() {
        String name = objNameField.getText().trim();
        if (name.isEmpty()) {
            stage.setKeyboardFocus(objNameField);
            return;
        }
        float x = parseFloat(objXField.getText());
        float y = parseFloat(objYField.getText());

        closeNewObjectWindow();
        onObjectCreated.accept(new NewObjectData(name, objTypeBox.getSelected(), x, y));
    }

    private float parseFloat(String text) {
        try {
            return Float.parseFloat(text.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    // ------------------------------------------------------------------
    // Common
    // ------------------------------------------------------------------

    public boolean isSetupDone() {
        return setupDone;
    }

    public void bindInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public boolean isPointerOverUI() {
        if (!setupDone) return true;
        if (newObjectWindow.isVisible()) return true; // form is modal-like
        Vector2 p = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        return stage.hit(p.x, p.y, true) != null;
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}