package perudo.model;

import java.time.Duration;

public interface GameSettings {
    
	int getMaxPlayer();

	int getMaxDiceValue();
	
	int getInitialDiceNumber();

	Duration getMaxTurnTime();
}
