package perudo.view.console;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;

import perudo.model.GameSettings;
import perudo.model.impl.GameSettingsImpl;

/**
 * 
 */
public class CreateLobbyForm extends BaseForm {

    private final TextBox txbPlayers, txbDiceFaces, txbDiceN, txbTurnTime, txbName;
    private final Button btnOk, btnCancel;
    private GameSettings lobby;

    /**
     * 
     * @param textGUI
     *            to do
     */

    public CreateLobbyForm(final MultiWindowTextGUI textGUI) {
        super(textGUI);
        this.lobby = null;
        // CHECKSTYLE:OFF: checkstyle:magicnumber
        this.txbPlayers = new TextBox(new TerminalSize(30, 1)).setText("4");
        this.txbDiceFaces = new TextBox(new TerminalSize(30, 1)).setText("6");
        this.txbDiceN = new TextBox(new TerminalSize(30, 1)).setText("5");
        this.txbTurnTime = new TextBox(new TerminalSize(30, 1)).setText("60");
        this.txbName = new TextBox(new TerminalSize(30, 1));
        // CHECKSTYLE:ON: checkstyle:magicnumber
        this.btnOk = new Button("Done", new Runnable() {
            @Override
            public void run() {
                btnOkClicked();
            }
        });
        this.btnCancel = new Button("Cancel", new Runnable() {
            @Override
            public void run() {
                btnCancelClicked();
            }
        });

        super.getWindow().setTitle("PERUDO - CREATE LOBBY");
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED));
        final Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        panel.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        panel.addComponent(this.txbPlayers.setValidationPattern(Pattern.compile("\\d{1,2}"))
                .withBorder(Borders.singleLine("Player number ( " + GameSettingsImpl.MIN_PLAYER_NUMBERS + " - "
                        + GameSettingsImpl.MAX_PLAYER_NUMBERS + " )")));

        panel.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        panel.addComponent(this.txbDiceFaces.setValidationPattern(Pattern.compile("\\d{1,2}"))
                .withBorder(Borders.singleLine("Dice faces ( " + GameSettingsImpl.MIN_DICE_FACES + " - "
                        + GameSettingsImpl.MAX_DICE_FACES + " )")));

        panel.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        panel.addComponent(this.txbDiceN.setValidationPattern(Pattern.compile("\\d{1,2}"))
                .withBorder(Borders.singleLine("Initial dice ( " + GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS + " - "
                        + GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS + " )")));

        panel.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        panel.addComponent(this.txbTurnTime.setValidationPattern(Pattern.compile("\\d{1,3}"))
                .withBorder(Borders.singleLine("Turn time ( " + GameSettingsImpl.MIN_TURN_TIME.getSeconds() + "s - "
                        + GameSettingsImpl.MAX_TURN_TIME.getSeconds() + "s )")));

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(this.txbName.withBorder(Borders.singleLine("Lobby name")));

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(2, 1)));

        final Panel subPanel = new Panel();
        subPanel.setLayoutManager(new GridLayout(3));
        subPanel.addComponent(this.btnOk);
        // CHECKSTYLE:OFF: checkstyle:magicnumber
        subPanel.addComponent(new EmptySpace(new TerminalSize(12, 1)));
        // CHECKSTYLE:ON: checkstyle:magicnumber
        subPanel.addComponent(this.btnCancel);

        panel.addComponent(subPanel);

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        super.getWindow().setComponent(panel);
        this.txbName.takeFocus();
    }

    private void btnOkClicked() {
        if (this.txbName.getText().trim().length() == 0) {
            Utils.showMessageBox("Input error", "Enter a name for the lobby.", super.getTextGUI());
            this.lobby = null;
            return;
        }
        try {
            this.lobby = new GameSettingsImpl(Integer.parseInt(this.txbPlayers.getText()),
                    Integer.parseInt(this.txbDiceFaces.getText()), Integer.parseInt(this.txbDiceN.getText()),
                    Duration.ofSeconds(Integer.parseInt(this.txbTurnTime.getText())), txbName.getText().trim());
            this.close();
        } catch (final Exception ex) {
            Utils.showMessageBox("Input error", ex.getMessage(), super.getTextGUI());
            this.lobby = null;
        }
    }

    private void btnCancelClicked() {
        this.lobby = null;
        this.close();
    }

    /**
     * 
     * @return to do
     */
    public Optional<GameSettings> getGameSettings() {
        if (this.lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

}
