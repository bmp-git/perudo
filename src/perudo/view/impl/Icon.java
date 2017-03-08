package perudo.view.impl;

import javax.swing.ImageIcon;

/**
 * Enum listing all application icons.
 *
 */
public enum Icon {
    /**
     * Icons.
     */
    OWNER("/images/crown_gold.png"), PLUS("/images/plus.png"), DICE("/images/dice.png"), TIME("/images/time.png"), EXIT("/images/close.png"), PLAYER("/images/player.png");

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
