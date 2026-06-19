package io.github.ignacypekala.utils;

public class Time implements Comparable<Time> {
    private int hours;
    private int minutes;
    private int seconds;

    private void normalize() {
        final int secondsCarry = seconds / 60;
        seconds = seconds % 60;
        final int minutesCarry = (minutes + secondsCarry) / 60;
        minutes = (minutes + secondsCarry) % 60;
        hours = (hours + minutesCarry) % 24;
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
        return seconds + minutes * 60 + hours * 3600;
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
