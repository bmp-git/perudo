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
    OWNER("/images/crown_gold.png"), PLUS("/images/plus.png"), MINUS("/images/minus.png"), DICE("/images/dice.png"), TIME("/images/time.png"), PLAYER("/images/player.png"), EXIT("/images/exit.png"), ENTER("/images/enter.png"), START("/images/start.png"), LEFT("/images/left.png"), RIGHT("/images/right.png");

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
