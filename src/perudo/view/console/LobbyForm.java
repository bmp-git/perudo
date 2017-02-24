package perudo.view.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import perudo.controller.Controller;
import perudo.model.Lobby;
import perudo.model.User;

public class LobbyForm extends BaseForm {

    private final Button btnExit, btnStart;
    private final Label lblInfo;
    private final List<UserPanel> userSpacePanel;
    private final Panel centerPanel;
    private final Controller controller;
    private User user;
    private Lobby lobby;

    class UserPanel extends Panel {

        public UserPanel(final Optional<User> user) {
            Label label;
            Button btnAddBot;
            this.setLayoutManager(new BorderLayout());

            if (user.isPresent()) {
                label = new Label(user.get().getName());
            } else {
                label = new Label("Free Space");
                btnAddBot = new Button("Add bot", () -> {
                    new MessageDialogBuilder().setTitle("Error").setText("Not implemented yet")
                            .addButton(MessageDialogButton.OK).build().showDialog(textGUI);
                });
                this.addComponent(btnAddBot, BorderLayout.Location.RIGHT);
            }

            this.setPreferredSize(new TerminalSize(25, 1));

            this.addComponent(label, BorderLayout.Location.LEFT);
        }
    }

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
        this.window.setTitle("PERUDO - LOBBY");
        this.window.setHints(Arrays.asList(Window.Hint.CENTERED));
        this.userSpacePanel = new ArrayList<>();

        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new BorderLayout());
        Panel topPanel = new Panel();
        this.centerPanel = new Panel();
        this.centerPanel.setLayoutManager(new GridLayout(1));
        Panel bottomPanel = new Panel();
        bottomPanel.setLayoutManager(new BorderLayout());

        mainPanel.addComponent(topPanel, BorderLayout.Location.LEFT);
        mainPanel.addComponent(centerPanel, BorderLayout.Location.CENTER);
        mainPanel.addComponent(bottomPanel, BorderLayout.Location.BOTTOM);

        topPanel.addComponent(this.lblInfo.withBorder(Borders.singleLine("Info")));

        bottomPanel.addComponent(btnStart, BorderLayout.Location.LEFT);
        bottomPanel.addComponent(btnExit, BorderLayout.Location.RIGHT);
        this.window.setComponent(mainPanel);
    }

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

    public void updateLobby(final Lobby lobby) {
        if (lobby.equals(this.lobby)) {
            this.setLobby(lobby);
        }
    }

    public void setLobby(final Lobby lobby) {
        this.lobby = lobby;
        
        this.btnStart.setEnabled(lobby.getOwner() != null && lobby.getOwner().equals(user));
        this.lblInfo.setText(Utils.lobbyToString(lobby));

        this.centerPanel.removeAllComponents();
        Panel panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        this.userSpacePanel.clear();

        for (int i = 0; i < lobby.getInfo().getMaxPlayer(); i++) {
            UserPanel p = new UserPanel(lobby.getUsers().stream().skip(i).findFirst());
            this.userSpacePanel.add(p);
            this.centerPanel.addComponent(p.withBorder(Borders.singleLine()));
        }
    }

    public Window getWindow() {
        return this.window;
    }

}
