package test.model;

import perudo.model.GameSettings;
import perudo.model.impl.GameSettingsImpl;

import static org.junit.Assert.*;

public class GameSettingsTest {
    @org.junit.Test
    public void main() {
        GameSettings sett = null;

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER - 1, GameSettingsImpl.MIN_MAX_DICE_VALUE,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER, GameSettingsImpl.MIN_MAX_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER, GameSettingsImpl.MIN_MAX_DICE_VALUE - 1,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER, GameSettingsImpl.MIN_MAX_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER, GameSettingsImpl.MIN_MAX_DICE_VALUE,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER - 1, GameSettingsImpl.MIN_MAX_TURN_TIME);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER, GameSettingsImpl.MIN_MAX_DICE_VALUE,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER, GameSettingsImpl.MIN_MAX_TURN_TIME.minusMinutes(1));
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {

        }

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_MAX_PLAYER + 1, GameSettingsImpl.MAX_MAX_DICE_VALUE,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBER, GameSettingsImpl.MAX_MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }

        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_MAX_PLAYER, GameSettingsImpl.MAX_MAX_DICE_VALUE + 1,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBER, GameSettingsImpl.MAX_MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_MAX_PLAYER, GameSettingsImpl.MAX_MAX_DICE_VALUE,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBER + 1, GameSettingsImpl.MAX_MAX_TURN_TIME);
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_MAX_PLAYER + 1, GameSettingsImpl.MAX_MAX_DICE_VALUE,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBER, GameSettingsImpl.MAX_MAX_TURN_TIME.plusMinutes(1));
            throw new IllegalStateException("Over limit");
        } catch (IllegalArgumentException ex) {

        }
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MAX_MAX_PLAYER, GameSettingsImpl.MAX_MAX_DICE_VALUE,
                    GameSettingsImpl.MAX_INITIAL_DICE_NUMBER, GameSettingsImpl.MAX_MAX_TURN_TIME);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        
        assertEquals(sett.getInitialDiceNumber(), GameSettingsImpl.MAX_INITIAL_DICE_NUMBER);
        assertEquals(sett.getMaxDiceValue(), GameSettingsImpl.MAX_MAX_DICE_VALUE);
        assertEquals(sett.getMaxPlayer(), GameSettingsImpl.MAX_MAX_PLAYER);
        assertEquals(sett.getMaxTurnTime(), GameSettingsImpl.MAX_MAX_TURN_TIME);
        
        try {
            sett = new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER, GameSettingsImpl.MIN_MAX_DICE_VALUE,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER, GameSettingsImpl.MIN_MAX_TURN_TIME);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        
        assertEquals(sett.getInitialDiceNumber(), GameSettingsImpl.MIN_INITIAL_DICE_NUMBER);
        assertEquals(sett.getMaxDiceValue(), GameSettingsImpl.MIN_MAX_DICE_VALUE);
        assertEquals(sett.getMaxPlayer(), GameSettingsImpl.MIN_MAX_PLAYER);
        assertEquals(sett.getMaxTurnTime(), GameSettingsImpl.MIN_MAX_TURN_TIME);
        
        assertEquals(sett, new GameSettingsImpl(GameSettingsImpl.MIN_MAX_PLAYER, GameSettingsImpl.MIN_MAX_DICE_VALUE,
                    GameSettingsImpl.MIN_INITIAL_DICE_NUMBER, GameSettingsImpl.MIN_MAX_TURN_TIME));
    }
}
