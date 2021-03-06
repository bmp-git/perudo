package perudo.utility.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import perudo.utility.LogSeverity;
import perudo.utility.Logger;

/**
 * A singleton Logger.
 *
 */
public final class LoggerSingleton implements Logger {
    private static class Holder {
        private static final LoggerSingleton SINGLETON = newLoggerSingleton();
        private static volatile boolean enabled;
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.ITALY).withZone(ZoneId.systemDefault());
    }

    private static LoggerSingleton newLoggerSingleton() {
        return new LoggerSingleton();
    }

    private LoggerSingleton() {
    };

    /**
     * Provides the logger singleton.
     * 
     * @return the logger singleton
     */
    public static LoggerSingleton get() {
        return Holder.SINGLETON;
    }

    @Override
    public void add(final LogSeverity severity, final Class<?> source, final String message) {
        if (Holder.enabled) {
            System.out.println("<" + Holder.FORMATTER.format(Instant.now()) + ">" + severity.getDescriber() + ": "
                    + source.getSimpleName() + ": " + message);
        }
    }

    @Override
    public void enable() {
        Holder.enabled = true;
    }

    @Override
    public void disable() {
        Holder.enabled = false;
    }

}
