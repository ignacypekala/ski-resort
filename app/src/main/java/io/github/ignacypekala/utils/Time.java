package io.github.ignacypekala.utils;

public class Time {
    private int hours;
    private int minutes;
    private int seconds;

    private void normalize() {
        int secondsCarry = seconds / 60;
        seconds = seconds % 60;
        int minutesCarry = (minutes + secondsCarry) / 60;
        minutes = (minutes + secondsCarry) % 60;
        hours = (hours + minutesCarry) % 24;
    }

    public Time(int hours, int minutes, int seconds) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours shouldn't be negative.");
        }
        this.seconds = seconds;
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes shouldn't be negative.");
        }
        this.minutes = minutes;
        if (seconds < 0) {
            throw new IllegalArgumentException("Minutes shouldn't be negative.");
        }
        this.hours = hours;
        normalize();
    }

    public static Time secondsLater(Time time, int delay) {
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

    private static String paddedInt(int number) {
        return String.format("%02d", number);
    }
    public String toString() {
        return String.format(
            "%s:%s:%s",
            paddedInt(hours),
            paddedInt(minutes),
            paddedInt(seconds)
        );
    }

    public int compareTo(Time other) {
        return Integer.compare(this.toSeconds(), other.toSeconds());
    }
}
