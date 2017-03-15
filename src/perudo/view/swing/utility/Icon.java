package perudo.view.swing.utility;

import javax.swing.ImageIcon;

/**
 * Enum listing all application icons.
 *
 */
public enum Icon {
    /**
     * Application icon.
     */
    APPLICATION_ICON("/images/perudo-logo.png"),
    /**
     * Lobby owner icon.
     */
    OWNER("/images/crown_gold.png"),
    /**
     * Add bot icon.
     */
    PLUS("/images/plus.png"),
    /**
     * Remove bot icon.
     */
    MINUS("/images/minus.png"),
    /**
     * Number of dice icon in lobby settings.
     */
    DICE("/images/dice.png"),
    /**
     * Turn time icon in lobby settings.
     */
    TIME("/images/time.png"),
    /**
     * Player icon in lobby settings.
     */
    PLAYER("/images/player.png"),
    /**
     * Exit icon in lobby.
     */
    EXIT("/images/exit.png"),
    /**
     * Enter icon in lobby.
     */
    ENTER("/images/enter.png"),
    /**
     * Start icon in lobby.
     */
    START("/images/start.png"),
    /**
     * Left icon in bid panel inside game panel.
     */
    LEFT("/images/left.png"),
    /**
     * Right icon in bid panel inside game panel.
     */
    RIGHT("/images/right.png");

    private final ImageIcon icon;

    Icon(final String respath) {
        this.icon = new ImageIcon(Icon.class.getResource(respath));
    }

    /**
     * Get the icon.
     * 
     * @return the icon
     */
    public ImageIcon getIcon() {
        return this.icon;
    }
}
