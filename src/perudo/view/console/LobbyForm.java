package perudo.view.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;

import perudo.controller.Controller;
import perudo.model.Lobby;
import perudo.model.User;

/**
 * 
 */
public class LobbyForm extends BaseForm {

    private final Button btnExit, btnStart;
    private final Label lblInfo;
    private final List<UserPanel> userSpacePanel;
    private final Panel centerPanel;
    private final Controller controller;
    private User user;
    private Lobby lobby;

    class UserPanel extends Panel {

        /**
         * 
         * @param user
         *            to do
         * @param lobby
         *            to do
         * @param me
         *            to do
         * @param textGUI
         *            to do
         */
        protected UserPanel(final Optional<User> user, final Lobby lobby, final User me,
                final MultiWindowTextGUI textGUI) {
            Label label;
            Button btnAddBot;
            this.setLayoutManager(new BorderLayout());

            if (user.isPresent()) {

                label = new Label(user.get().getName());
                if (user.get().equals(me)) {
                    label.setForegroundColor(ANSI.RED);
                }
                if (user.get().getType().isBot() && me.equals(lobby.getOwner())) {
                    btnAddBot = new Button("Remove bot", () -> {
                        btnExit.takeFocus();
                        controller.closeNow(user.get());
                    });
                    this.addComponent(btnAddBot, BorderLayout.Location.RIGHT);
                }
            } else {
                label = new Label("Free Space");
                if (me.equals(lobby.getOwner())) {
                    btnAddBot = new Button("Add bot", () -> {
                        final BotSelectForm botForm = new BotSelectForm(textGUI);
                        botForm.showDialog();
                        if (botForm.getBotType().isPresent()) {
                            btnExit.takeFocus();
                            controller.addBotToLobby(me, lobby, botForm.getBotType().get());
                        }
                    });
                    this.addComponent(btnAddBot, BorderLayout.Location.RIGHT);
                }
            }
            // CHECKSTYLE:OFF: checkstyle:magicnumber
            this.setPreferredSize(new TerminalSize(25, 1));
            // CHECKSTYLE:ON: checkstyle:magicnumber
            this.addComponent(label, BorderLayout.Location.LEFT);
        }
    }

    /**
     * 
     * @param controller
     *            to do
     * @param textGUI
     *            to do
     */
    public LobbyForm(final Controller controller, final MultiWindowTextGUI textGUI) {
        super(textGUI);
        this.controller = controller;
        this.btnExit = new Button("Exit", () -> {
            this.btnExitClicked();
        });
        this.btnStart = new Button("Start", () -> {
            this.btnStartClicked();
        });
        this.btnStart.setEnabled(false);
        this.lblInfo = new Label("Info");
        super.getWindow().setTitle("PERUDO - LOBBY");
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED));
        this.userSpacePanel = new ArrayList<>();

        final Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new BorderLayout());
        final Panel topPanel = new Panel();
        this.centerPanel = new Panel();
        this.centerPanel.setLayoutManager(new GridLayout(1));
        final Panel bottomPanel = new Panel();
        bottomPanel.setLayoutManager(new BorderLayout());

        mainPanel.addComponent(topPanel, BorderLayout.Location.LEFT);
        mainPanel.addComponent(centerPanel, BorderLayout.Location.CENTER);
        mainPanel.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        topPanel.addComponent(this.lblInfo.withBorder(Borders.singleLine("Info")));

        bottomPanel.addComponent(btnStart, BorderLayout.Location.LEFT);
        bottomPanel.addComponent(btnExit, BorderLayout.Location.RIGHT);
        super.getWindow().setComponent(mainPanel);
    }

    /**
     * 
     * @param user
     *            to do
     */
    public void setUser(final User user) {
        this.user = user;
    }

    private void btnExitClicked() {
        if (this.user == null) {
            throw new IllegalStateException("User is null (LobbyForm.java, btnExitClicked())");
        } else {
            this.controller.exitLobby(this.user);
        }

    }

    private void btnStartClicked() {
        if (this.user == null) {
            throw new IllegalStateException("User is null (LobbyForm.java, btnStartClicked())");
        } else {
            this.controller.startLobby(this.user);
        }
    }

    /**
     * 
     * @param lobby
     *            to do
     */
    public void updateLobby(final Lobby lobby) {
        if (lobby.equals(this.lobby)) {
            this.setLobby(lobby);
        }
    }

    /**
     * 
     * @param lobby
     *            to do
     */
    public void setLobby(final Lobby lobby) {
        this.lobby = lobby;

        this.btnStart.setEnabled(lobby.getOwner() != null && lobby.getOwner().equals(user));
        this.lblInfo.setText(Utils.lobbyToString(lobby));

        this.centerPanel.removeAllComponents();
        final Panel panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        this.userSpacePanel.clear();

        for (int i = 0; i < lobby.getInfo().getMaxPlayer(); i++) {
            final UserPanel p = new UserPanel(lobby.getUsers().stream().skip(i).findFirst(), this.lobby, this.user,
                    super.getTextGUI());
            this.userSpacePanel.add(p);
            this.centerPanel.addComponent(p.withBorder(Borders.singleLine()));
        }
    }
}
