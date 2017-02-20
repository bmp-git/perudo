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

public class CreateLobbyPanel extends JPanel {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private final static String LABEL_NAME = "Name";
        private final static String LABEL_PLAYERS_NUM = "Max players";
        private final static String LABEL_DICES_NUM = "Max dices";
        private final static String LABEL_DICES_VALUE = "Max dices value";
        private final static String LABEL_TURN_TIME = "Turn time in seconds";
        private final static int TURN_TIME_LABEL_TICK = 117;
        
        private final static Object[] N_PLAYERS = IntStream
                        .rangeClosed(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MAX_PLAYER_NUMBERS)
                        .mapToObj(e -> String.valueOf(e)).toArray();
        private final static Object[] N_DICE = IntStream
                        .rangeClosed(GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS)
                        .mapToObj(e -> String.valueOf(e)).toArray();
        private final static Object[] DICES_VALUE = IntStream
                        .rangeClosed(GameSettingsImpl.MIN_DICE_FACES, GameSettingsImpl.MAX_DICE_FACES)
                        .mapToObj(e -> String.valueOf(e)).toArray();
        
        private final GUIFactory factory;
        private JTextField txfName;
        private JComboBox<Object> cmbPlayers;
        private JComboBox<Object> cmbDices;
        private JComboBox<Object> cmbDicesValue;
        private JSlider sldTime;

        public CreateLobbyPanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                this.factory = new StandardGUIFactory();
                this.txfName = (JTextField) this.factory.createTextField();
                this.cmbPlayers = (JComboBox<Object>) this.factory.createComboBox(N_PLAYERS);
                this.cmbDices = (JComboBox<Object>) this.factory.createComboBox(N_DICE);
                this.cmbDicesValue = (JComboBox<Object>) this.factory.createComboBox(DICES_VALUE);
                int middle = ((int)GameSettingsImpl.MAX_TURN_TIME.getSeconds() - (int)GameSettingsImpl.MIN_TURN_TIME.getSeconds()) /2;
                this.sldTime = (JSlider) this.factory.createSliderHorizontal((int)GameSettingsImpl.MIN_TURN_TIME.getSeconds(), (int)GameSettingsImpl.MAX_TURN_TIME.getSeconds(), middle);
                this.sldTime.setMajorTickSpacing(TURN_TIME_LABEL_TICK);
                this.sldTime.setPaintLabels(true);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
                this.add(this.factory.createLabel(LABEL_NAME));
                this.add(this.txfName);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
                this.add(this.factory.createLabel(LABEL_PLAYERS_NUM));
                this.add(this.cmbPlayers);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
                this.add(this.factory.createLabel(LABEL_DICES_NUM));
                this.add(this.cmbDices);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
                this.add(this.factory.createLabel(LABEL_DICES_VALUE));
                this.add(this.cmbDicesValue);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
                this.add(this.factory.createLabel(LABEL_TURN_TIME));
                this.add(this.sldTime);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        public String getName() {
                return this.txfName.getText();
        }
        
        public int getMaxPlayers() {
                return Integer.parseInt(this.cmbPlayers.getSelectedItem().toString());
        }
        public int getMaxDices() {
                return Integer.parseInt(this.cmbDices.getSelectedItem().toString());
        }
        public int getMaxDicesValue() {
                return Integer.parseInt(this.cmbDicesValue.getSelectedItem().toString());
        }
        
        public Duration getTurnTime() {
                return Duration.ofSeconds(this.sldTime.getValue());
        }
        
        public GameSettings getGameSettings() {
            return new GameSettingsImpl(this.getMaxPlayers(),this.getMaxDicesValue(),this.getMaxDices(),this.getTurnTime());
        }
}
