package perudo.view.console;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;

/**
 * 
 */
public class GameMenuForm extends BaseForm {

    private final ActionListBox lstUsers, lstLobbies, lstGames;
    private final Controller controller;
    private User user;

    private final Set<User> users = new HashSet<>();
    private final Set<Lobby> lobbies = new HashSet<>();
    private final Set<Game> games = new HashSet<>();

    /**
     * 
     * @param controller
     *            to do
     * @param textGUI
     *            to do
     */
    public GameMenuForm(final Controller controller, final MultiWindowTextGUI textGUI) {
        super(textGUI);
        final Button btnMenuChangeName, btnCreateLobby, btnExit, btnRefresh;
        this.controller = controller;
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.EXPANDED));
        this.setUser(null);

        btnMenuChangeName = new Button("Change name", () -> {
            this.btnMenuChangeNameClicked();
        });
        btnCreateLobby = new Button("Create lobby", () -> {
            this.btnCreateLobbyClicked();
        });
        btnExit = new Button("Exit", () -> {
            this.controller.close(this.user);
        });
        btnRefresh = new Button("Refresh", () -> {
            this.controller.getGames(this.user);
            this.controller.getLobbies(this.user);
            this.controller.getUsers(this.user);
        });

        final Panel mainPanel = new Panel();
        // CHECKSTYLE:OFF: checkstyle:magicnumber
        this.lstUsers = new ActionListBox(new TerminalSize(1400, 2000));
        this.lstLobbies = new ActionListBox(new TerminalSize(1400, 2000));
        this.lstGames = new ActionListBox(new TerminalSize(1400, 2000));
        // CHECKSTYLE:ON: checkstyle:magicnumber

        mainPanel.setLayoutManager(new BorderLayout());

        final Panel leftPanel = new Panel();
        leftPanel.setLayoutManager(new BorderLayout());
        leftPanel.setLayoutData(BorderLayout.Location.LEFT);

        final Panel leftTopPanel = new Panel();
        leftTopPanel.setLayoutManager(new GridLayout(1));
        leftTopPanel.setLayoutData(BorderLayout.Location.TOP);

        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnMenuChangeName);
        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnCreateLobby);
        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnExit);

        final Panel leftBottomPanel = new Panel();
        leftBottomPanel.setLayoutManager(new GridLayout(1));
        leftBottomPanel.setLayoutData(BorderLayout.Location.BOTTOM);
        leftBottomPanel.addComponent(btnRefresh);
        leftBottomPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        leftPanel.addComponent(leftTopPanel);
        leftPanel.addComponent(leftBottomPanel);

        final Panel rightPanel = new Panel();
        rightPanel.setLayoutManager(new GridLayout(3));
        rightPanel.setLayoutData(BorderLayout.Location.RIGHT);

        rightPanel.addComponent(this.lstLobbies.withBorder(Borders.doubleLine("Lobbies")));
        rightPanel.addComponent(this.lstGames.withBorder(Borders.doubleLine("Games")));
        rightPanel.addComponent(this.lstUsers.withBorder(Borders.doubleLine("Users")));

        mainPanel.addComponent(leftPanel);
        mainPanel.addComponent(rightPanel);

        super.getWindow().setComponent(mainPanel);
    }

    private boolean checkUserNotNull() {
        if (this.user == null) {
            Utils.showMessageBox("User is null", "User is not been initialized yet.", super.getTextGUI());
            return false;
        }
        return true;
    }

    /**
     * 
     * @param user
     *            to do
     */
    public void setUser(final User user) {
        this.user = user;
        super.getWindow().setTitle("PERUDO - GAME MENU" + (this.user == null ? "" : " - " + this.user.getName()));
    }

    private void btnMenuChangeNameClicked() {
        if (!checkUserNotNull()) {
            return;
        }

        final String name = new TextInputDialogBuilder().setTitle("Change name").setDescription("Write your name")
                .build().showDialog(super.getTextGUI());

        if (name != null) {
            this.controller.changeUserName(this.user, name);
        }
    }

    private void btnCreateLobbyClicked() {
        if (!checkUserNotNull()) {
            return;
        }

        final CreateLobbyForm form = new CreateLobbyForm(super.getTextGUI());
        form.showDialog();
        if (form.getGameSettings().isPresent()) {
            this.controller.createLobby(this.user, form.getGameSettings().get());
        }
    }

    /**
     * 
     * @param user
     *            to do
     */
    public void addUser(final User user) {
        this.users.add(user);
        this.refreshUsersList();
    }

    /**
     * 
     * @param user
     *            to do
     */
    public void removeUser(final User user) {
        this.users.remove(user);
        this.refreshUsersList();
    }

    /**
     * 
     * @param users
     *            to do
     */
    public void setUsers(final Set<User> users) {
        this.users.clear();
        users.stream().forEach(u -> this.users.add(u));
        this.refreshUsersList();
    }

    private void refreshUsersList() {
        this.lstUsers.clearItems();
        users.stream().forEach(u -> {
            final Runnable run = () -> {
                Utils.showMessageBox("User", u.getName() + " - id: " + u.getId() + (u.getType().isBot() ? "\nBOT" : ""),
                        super.getTextGUI());
            };
            this.lstUsers.addItem(
                    " " + u.getName() + (u.equals(this.user) ? " <-" : "") + (u.getType().isBot() ? " (bot)" : ""),
                    run);
        });
    }

    /**
     * 
     * @param lobby
     *            to do
     */
    public void addLobby(final Lobby lobby) {
        this.lobbies.add(lobby);
        this.refreshLobbiesList();
    }

    /**
     * 
     * @param lobby
     *            to do
     */
    public void removeLobby(final Lobby lobby) {
        this.lobbies.remove(lobby);
        this.refreshLobbiesList();
    }

    /**
     * 
     * @param lobbies
     *            to do
     */
    public void setLobbies(final Set<Lobby> lobbies) {
        this.lobbies.clear();
        lobbies.stream().forEach(l -> this.lobbies.add(l));
        this.refreshLobbiesList();
    }

    private void refreshLobbiesList() {
        this.lstLobbies.clearItems();
        this.lobbies.forEach(l -> {
            final Runnable run = () -> {
                MessageDialogButton response = new MessageDialogBuilder().setTitle("Join lobby")
                        .setText("Do you want to join this lobby?\n\n" + Utils.lobbyToString(l))
                        .addButton(MessageDialogButton.Yes).addButton(MessageDialogButton.No).build()
                        .showDialog(super.getTextGUI());

                if (response.equals(MessageDialogButton.Yes)) {
                    if (!checkUserNotNull()) {
                        return;
                    }
                    this.controller.joinLobby(this.user, l);
                }
            };
            this.lstLobbies.addItem(
                    " " + l.getInfo().getName() + " " + l.getUsers().size() + "/" + l.getInfo().getMaxPlayer(), run);
        });
    }

    /**
     * 
     * @param game
     *            to do
     */
    public void addGame(final Game game) {
        this.games.add(game);
        this.refreshGamesList();
    }

    /**
     * 
     * @param game
     *            to do
     */
    public void removeGame(final Game game) {
        this.games.remove(game);
        this.refreshGamesList();
    }

    /**
     * 
     * @param games
     *            to do
     */
    public void setGames(final Set<Game> games) {
        this.games.clear();
        games.stream().forEach(l -> this.games.add(l));
        this.refreshGamesList();
    }

    private void refreshGamesList() {
        this.lstGames.clearItems();
        this.games.forEach(g -> {
            final Runnable run = () -> {
                Utils.showMessageBox("Spectate game", "This feature is not implemented yet", super.getTextGUI());
            };
            this.lstGames.addItem(" " + g.getSettings().getName(), run);
        });
    }

}
