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

public class GameMenuForm extends BaseForm {

    private final Button btnMenuChangeName, btnCreateLobby, btnExit, btnRefresh;
    private final ActionListBox lstUsers, lstLobbies, lstGames;
    private final Controller controller;
    private User user;

    public GameMenuForm(final Controller controller, final MultiWindowTextGUI textGUI) {
        super(textGUI);
        this.controller = controller;
        this.window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.EXPANDED));
        this.setUser(null);

        btnMenuChangeName = new Button("Change name", () -> {
            this.btnMenuChangeNameClicked();
        });
        btnCreateLobby = new Button("Create lobby", () -> {
            this.btnCreateLobbyClicked();
        });
        btnExit = new Button("Exit", () -> {
            this.controller.closeNow(this.user);
        });
        btnRefresh = new Button("Refresh", () -> {
            this.controller.getGames(this.user);
            this.controller.getLobbies(this.user);
            this.controller.getUsers(this.user);
        });

        Panel mainPanel = new Panel();
        this.lstUsers = new ActionListBox(new TerminalSize(1400, 2000));
        this.lstLobbies = new ActionListBox(new TerminalSize(1400, 2000));
        this.lstGames = new ActionListBox(new TerminalSize(1400, 2000));

        mainPanel.setLayoutManager(new BorderLayout());

        Panel leftPanel = new Panel();
        leftPanel.setLayoutManager(new BorderLayout());
        leftPanel.setLayoutData(BorderLayout.Location.LEFT);

        Panel leftTopPanel = new Panel();
        leftTopPanel.setLayoutManager(new GridLayout(1));
        leftTopPanel.setLayoutData(BorderLayout.Location.TOP);

        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnMenuChangeName);
        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnCreateLobby);
        leftTopPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        leftTopPanel.addComponent(btnExit);

        Panel leftBottomPanel = new Panel();
        leftBottomPanel.setLayoutManager(new GridLayout(1));
        leftBottomPanel.setLayoutData(BorderLayout.Location.BOTTOM);
        leftBottomPanel.addComponent(btnRefresh);
        leftBottomPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        leftPanel.addComponent(leftTopPanel);
        leftPanel.addComponent(leftBottomPanel);

        Panel rightPanel = new Panel();
        rightPanel.setLayoutManager(new GridLayout(3));
        rightPanel.setLayoutData(BorderLayout.Location.RIGHT);

        rightPanel.addComponent(this.lstLobbies.withBorder(Borders.doubleLine("Lobbies")));
        rightPanel.addComponent(this.lstGames.withBorder(Borders.doubleLine("Games")));
        rightPanel.addComponent(this.lstUsers.withBorder(Borders.doubleLine("Users")));

        mainPanel.addComponent(leftPanel);
        mainPanel.addComponent(rightPanel);

        window.setComponent(mainPanel);
    }

    private boolean checkUserNotNull() {
        if (this.user == null) {
            Utils.showMessageBox("User is null", "User is not been initialized yet.", this.textGUI);
            return false;
        }
        return true;
    }

    public void setUser(User user) {
        this.user = user;
        this.window.setTitle("PERUDO - GAME MENU" + (this.user == null ? "" : " - " + this.user.getName()));
    }


    private void btnMenuChangeNameClicked() {
        if (!checkUserNotNull()) {
            return;
        }

        String name = new TextInputDialogBuilder().setTitle("Change name").setDescription("Write your name").build()
                .showDialog(this.textGUI);

        if (name != null) {
            this.controller.changeUserName(this.user, name);
        }
    }

    private void btnCreateLobbyClicked() {
        if (!checkUserNotNull()) {
            return;
        }

        CreateLobbyForm form = new CreateLobbyForm(this.textGUI);
        form.showDialog();
        if (form.getGameSettings().isPresent()) {
            this.controller.createLobby(this.user, form.getGameSettings().get());
        }
    }

    // NOTIFY
    private final Set<User> users = new HashSet<>();
    private final Set<Lobby> lobbies = new HashSet<>();
    private final Set<Game> games = new HashSet<>();

    public void addUser(final User user) {
        this.users.add(user);
        this.refreshUsersList();
    }

    public void removeUser(final User user) {
        this.users.remove(user);
        this.refreshUsersList();
    }

    public void setUsers(final Set<User> users) {
        this.users.clear();
        users.stream().forEach(u -> this.users.add(u));
        this.refreshUsersList();
    }

    private void refreshUsersList() {
        this.lstUsers.clearItems();
        users.stream().forEach(u -> {
            Runnable run = () -> {
                Utils.showMessageBox("User", u.getName() + " - id: " + u.getId(), this.textGUI);
            };
            this.lstUsers.addItem(" "+u.getName() + (u.equals(this.user) ? " <-" : ""), run);
        });
    }

    public void addLobby(final Lobby lobby) {
        this.lobbies.add(lobby);
        this.refreshLobbiesList();
    }

    void removeLobby(final Lobby lobby) {
        this.lobbies.remove(lobby);
        this.refreshLobbiesList();
    }

    public void setLobbies(final Set<Lobby> lobbies) {
        this.lobbies.clear();
        lobbies.stream().forEach(l -> this.lobbies.add(l));
        this.refreshLobbiesList();
    }
    
    private void refreshLobbiesList(){
        this.lstLobbies.clearItems();
        this.lobbies.forEach(l->{
            Runnable run = () -> {
                MessageDialogButton response = new MessageDialogBuilder().setTitle("Join lobby")
                        .setText("Do you want to join this lobby?\n\n" + Utils.lobbyToString(l))
                        .addButton(MessageDialogButton.Yes).addButton(MessageDialogButton.No).build()
                        .showDialog(this.textGUI);

                if (response.equals(MessageDialogButton.Yes)) {
                    if (!checkUserNotNull()) {
                        return;
                    }
                    this.controller.joinLobby(this.user, l);
                }
            };
            this.lstLobbies.addItem(" "+
                    l.getInfo().getName() + " " + l.getUsers().size() + "/" + l.getInfo().getMaxPlayer(), run);
        });
    }
    
    public void addGame(final Game game) {
        this.games.add(game);
        this.refreshGamesList();
    }

    void removeGame(final Game game) {
        this.games.remove(game);
        this.refreshGamesList();
    }

    public void setGames(final Set<Game> games) {
        this.games.clear();
        games.stream().forEach(l -> this.games.add(l));
        this.refreshGamesList();
    }
    
    private void refreshGamesList(){
        this.lstGames.clearItems();
        this.games.forEach(g->{
            Runnable run = () -> {
                new MessageDialogBuilder().setTitle("Spectate game")
                        .setText("This feature is not implemented yet")
                        .addButton(MessageDialogButton.OK).build()
                        .showDialog(this.textGUI);
            };
            this.lstGames.addItem(" "+
                    g.getSettings().getName(), run);
        });
    }

    public Window getWindow() {
        return this.window;
    }

}
