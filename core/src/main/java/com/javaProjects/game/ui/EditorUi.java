package com.javaProjects.game.ui;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EditorUi {

    private static final int MIN_TILES = 1;
    private static final int MAX_TILES = 512;

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
    }

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

    public boolean isSetupDone() {
        return setupDone;
    }

    public void bindInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public boolean isPointerOverUI() {
        if (!setupDone) return true;
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