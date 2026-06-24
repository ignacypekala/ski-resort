package io.github.ignacypekala.utils;

public class Time implements Comparable<Time> {
    private static int SECONDS_IN_A_MINUTE = 60;
    private static int MINUTES_IN_AN_HOUR = 60;
    private static int SECONDS_IN_AN_HOUR = SECONDS_IN_A_MINUTE * MINUTES_IN_AN_HOUR;
    private static int HOURS_IN_A_DAY = 24;

    private int hours;
    private int minutes;
    private int seconds;

    private void normalize() {
        final int secondsCarry = seconds / SECONDS_IN_A_MINUTE;
        seconds = seconds % SECONDS_IN_A_MINUTE;
        final int minutesCarry = (minutes + secondsCarry) / MINUTES_IN_AN_HOUR;
        minutes = (minutes + secondsCarry) % MINUTES_IN_AN_HOUR;
        hours = (hours + minutesCarry) % HOURS_IN_A_DAY;
    }

    public Time(final int hours, final int minutes, final int seconds) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours shouldn't be negative.");
        }
        this.hours = hours;

        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes shouldn't be negative.");
        }
        this.minutes = minutes;

        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds shouldn't be negative.");
        }
        this.seconds = seconds;

        normalize();
    }

    public static Time secondsLater(final Time time, final int delay) {
        return new Time(time.getHours(), time.getMinutes(), time.getSeconds() + delay);
    }

    public int toSeconds() {
        return seconds + minutes * SECONDS_IN_A_MINUTE + hours * SECONDS_IN_AN_HOUR;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public static int secondsBetween(final Time end, final Time start) {
        return end.toSeconds() - start.toSeconds();
    }

    private static String paddedInt(final int number) {
        return String.format("%02d", number);
    }

    public String toString() {
        return String.format(
                "%s:%s:%s",
                paddedInt(hours),
                paddedInt(minutes),
                paddedInt(seconds));
    }

    @Override
    public int compareTo(final Time other) {
        return Integer.compare(this.toSeconds(), other.toSeconds());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hours;
        result = prime * result + minutes;
        result = prime * result + seconds;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } if (obj == null) {
            return false;
        } if (getClass() != obj.getClass()) {
            return false;
        }
        Time other = (Time) obj;
        if (hours != other.hours) {
            return false;
        } if (minutes != other.minutes) {
            return false;
        } if (seconds != other.seconds) {
            return false;
        }
        return true;
    }

}
