package perudo.view.impl;

import javax.swing.ImageIcon;

public enum Icon {
	OWNER("/images/crown_gold.png"), PLUS("/images/plus.png"), DICE("/images/dice.png"), TIME("/images/time.png"), EXIT("/images/close.png"), PLAYER("/images/player.png");

	private final ImageIcon icon;

	private Icon(String respath) {
		this.icon = new ImageIcon(Icon.class.getResource(respath));
	}

	public ImageIcon getIcon() {
		return this.icon;
	}
}
