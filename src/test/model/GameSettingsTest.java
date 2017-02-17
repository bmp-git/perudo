package test.model;

import perudo.model.GameSettings;
import perudo.model.impl.GameSettingsImpl;

import static org.junit.Assert.*;

public class GameSettingsTest {
    @org.junit.Test
    public void main() {
        GameSettings sett = null;

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS - 1, GameSettingsImpl.MIN_DICE_FACES,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MIN_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MIN_DICE_FACES - 1,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MIN_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MIN_DICE_FACES,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS - 1, GameSettingsImpl.MIN_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MIN_DICE_FACES,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MIN_TURN_TIME.minusMinutes(1));
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_PLAYER_NUMBERS + 1, GameSettingsImpl.MAX_DICE_FACES,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_PLAYER_NUMBERS, GameSettingsImpl.MAX_DICE_FACES + 1,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_PLAYER_NUMBERS, GameSettingsImpl.MAX_DICE_FACES,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS + 1, GameSettingsImpl.MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_PLAYER_NUMBERS + 1, GameSettingsImpl.MAX_DICE_FACES,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_TURN_TIME.plusMinutes(1));
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_PLAYER_NUMBERS, GameSettingsImpl.MAX_DICE_FACES,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS, GameSettingsImpl.MAX_TURN_TIME);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        
        assertEquals(sett.getInitialDiceNumber(), GameSettingsImpl.MAX_INITIAL_DICE_NUMBERS);
        assertEquals(sett.getMaxDiceValue(), GameSettingsImpl.MAX_DICE_FACES);
        assertEquals(sett.getMaxPlayer(), GameSettingsImpl.MAX_PLAYER_NUMBERS);
        assertEquals(sett.getMaxTurnTime(), GameSettingsImpl.MAX_TURN_TIME);
        
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MIN_DICE_FACES,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MIN_TURN_TIME);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        
        assertEquals(sett.getInitialDiceNumber(), GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS);
        assertEquals(sett.getMaxDiceValue(), GameSettingsImpl.MIN_DICE_FACES);
        assertEquals(sett.getMaxPlayer(), GameSettingsImpl.MIN_PLAYER_NUMBERS);
        assertEquals(sett.getMaxTurnTime(), GameSettingsImpl.MIN_TURN_TIME);
        
        assertEquals(sett, new GameSettingsImpl(GameSettingsImpl.MIN_PLAYER_NUMBERS, GameSettingsImpl.MIN_DICE_FACES,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBERS, GameSettingsImpl.MIN_TURN_TIME));
    }
}
