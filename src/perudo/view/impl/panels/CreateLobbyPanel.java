package perudo.view.impl.panels;

import java.awt.Dimension;
import java.time.Duration;
import java.util.stream.IntStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import perudo.model.GameSettings;
import perudo.model.impl.GameSettingsImpl;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

/**
 * Panel rappresenting a form for create a lobby.
 */
public class CreateLobbyPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Panel title.
     */
    public static final String TITLE = "Create a new lobby...";
    /**
     * Panel icon respath.
     */
    public static final String ICON_RESPATH = "/images/perudo-logo.png";
    private static final String LABEL_NAME = "Name";
    private static final String LABEL_PLAYERS_NUM = "Max players";
    private static final String LABEL_DICES_NUM = "Max dices";
    private static final String LABEL_DICES_VALUE = "Max dices value";
    private static final String LABEL_TURN_TIME = "Turn time in seconds";
    private static final int TURN_TIME_LABEL_TICK = 117;
    private static final int TURN_TIME_DEF = 60;
    private static final int DICE_NUM_DEF = 5 - GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS;
    private static final int DICE_VALUE_DEF = 6 - GameSettingsImpl.MIN_DICE_FACES;
    private static final int PLAYER_NUM_DEF = 4 - GameSettingsImpl.MIN_PLAYER_NUMBERS;

    private static final Object[] N_PLAYERS = IntStream
            .rangeClosed(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MAX_PLAYER_NUMBERS)
            .mapToObj(e -> String.valueOf(e)).toArray();
    private static final Object[] N_DICE = IntStream
            .rangeClosed(GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS)
            .mapToObj(e -> String.valueOf(e)).toArray();
    private static final Object[] DICES_VALUE = IntStream
            .rangeClosed(GameSettingsImpl.MIN_DICE_FACES, GameSettingsImpl.MAX_DICE_FACES).mapToObj(e -> String.valueOf(e))
            .toArray();

    private final JTextField txfName;
    private final JComboBox<Object> cmbPlayers;
    private final JComboBox<Object> cmbDices;
    private final JComboBox<Object> cmbDicesValue;
    private final JSlider sldTime;

    /**
     * Initialize and create the form panel.
     */
    public CreateLobbyPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final GUIFactory factory = new StandardGUIFactory();
        this.txfName = (JTextField) factory.createTextField();
        this.cmbPlayers = (JComboBox<Object>) factory.createComboBox(N_PLAYERS);
        this.cmbPlayers.setSelectedIndex(PLAYER_NUM_DEF);
        this.cmbDices = (JComboBox<Object>) factory.createComboBox(N_DICE);
        this.cmbDices.setSelectedIndex(DICE_NUM_DEF);
        this.cmbDicesValue = (JComboBox<Object>) factory.createComboBox(DICES_VALUE);
        this.cmbDicesValue.setSelectedIndex(DICE_VALUE_DEF);
        this.sldTime = (JSlider) factory.createSliderHorizontal((int) GameSettingsImpl.MIN_TURN_TIME.getSeconds(),
                (int) GameSettingsImpl.MAX_TURN_TIME.getSeconds(), TURN_TIME_DEF);
        this.sldTime.setMajorTickSpacing(TURN_TIME_LABEL_TICK);
        this.sldTime.setPaintLabels(true);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_NAME));
        this.add(this.txfName);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_PLAYERS_NUM));
        this.add(this.cmbPlayers);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_DICES_NUM));
        this.add(this.cmbDices);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_DICES_VALUE));
        this.add(this.cmbDicesValue);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_TURN_TIME));
        this.add(this.sldTime);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * Get lobby name.
     * 
     * @return the lobby name
     */
    public String getName() {
        return this.txfName.getText();
    }

    /**
     * Get lobby max players.
     * 
     * @return the max players number
     */
    public int getMaxPlayers() {
        return Integer.parseInt(this.cmbPlayers.getSelectedItem().toString());
    }

    /**
     * Get lobby max dices.
     * 
     * @return the max dices number
     */
    public int getMaxDices() {
        return Integer.parseInt(this.cmbDices.getSelectedItem().toString());
    }

    /**
     * Get lobby max dices value.
     * 
     * @return the max dices value
     */
    public int getMaxDicesValue() {
        return Integer.parseInt(this.cmbDicesValue.getSelectedItem().toString());
    }

    /**
     * Get lobby turn time.
     * 
     * @return the lobby turn time
     */
    public Duration getTurnTime() {
        return Duration.ofSeconds(this.sldTime.getValue());
    }

    /**
     * Get all lobby setting.
     * 
     * @return the lobby settings
     */
    public GameSettings getGameSettings() {
        return new GameSettingsImpl(this.getMaxPlayers(), this.getMaxDicesValue(), this.getMaxDices(), this.getTurnTime(),
                this.getName());
    }
}
