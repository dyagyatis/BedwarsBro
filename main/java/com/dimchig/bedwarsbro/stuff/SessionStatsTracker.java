package com.dimchig.bedwarsbro.stuff;

public class SessionStatsTracker {
    public static int gamesPlayed = 0;
    public static int gamesWon = 0;
    public static int bedsBroken = 0;
    public static int kills = 0;
    public static int finalKills = 0;
    public static int deaths = 0;
    public static int currentWinstreak = 0;
    public static int bestWinstreak = 0;

    public static void reset() {
        gamesPlayed = 0;
        gamesWon = 0;
        bedsBroken = 0;
        kills = 0;
        finalKills = 0;
        deaths = 0;
        currentWinstreak = 0;
        bestWinstreak = 0;
    }

    public static void addBedBreak() {
        bedsBroken++;
    }

    public static void addKill(boolean isFinal) {
        kills++;
        if (isFinal) {
            finalKills++;
        }
    }

    public static void addDeath() {
        deaths++;
    }

    public static void addGameResult(boolean won) {
        gamesPlayed++;
        if (won) {
            gamesWon++;
            currentWinstreak++;
            if (currentWinstreak > bestWinstreak) {
                bestWinstreak = currentWinstreak;
            }
        } else {
            currentWinstreak = 0;
        }
    }

    public static String getFormattedStats() {
        return String.format("§eИгр: §f%d §7| §aПобед: §f%d §7| §cКроватей: §f%d §7| §bУбийств: §f%d §7(§dФинальных: %d§7) §7| §eВинстрик: §f%d",
                gamesPlayed, gamesWon, bedsBroken, kills, finalKills, currentWinstreak);
    }
}
