package perudo.view.impl.components;

import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIFactorySingleton;

/**
 * A customized MenuBar for gamePanel.
 */
public class TopMenuGame extends JMenuBar {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String MENU_GAME = "Game";
    private static final String MENU_GAME_EXITGAME = "Exit";
    private static final String MENU_GAME_EXITGAME_TOOLTIP = "Exit from game..";

    private Optional<User> user;

    /**
     * Initialize the menu with menu items.
     */
    public TopMenuGame() {
        super();
        this.user = Optional.empty();
        final GUIFactory factory = GUIFactorySingleton.getFactory();

        final JMenu gameMenu = (JMenu) factory.createMenu(MENU_GAME);
        gameMenu.setMnemonic(KeyEvent.VK_G);
        final JMenuItem miExitGame = (JMenuItem) factory.createMenuItem(MENU_GAME_EXITGAME, KeyEvent.VK_E,
                MENU_GAME_EXITGAME_TOOLTIP);
        miExitGame.addActionListener(e -> {
            if (this.user.isPresent()) {
                ControllerSingleton.getController().exitGame(this.user.get());
            }
        });
        gameMenu.add(miExitGame);
        this.add(gameMenu);
    }

    /**
     * Set user to menu.
     * 
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
    }
}
