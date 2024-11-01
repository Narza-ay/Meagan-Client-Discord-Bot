package fr.meagan.discord.utils;

public class ReformatTimeUtil {

	public static String secondsToTime(long seconds) {
		StringBuilder builder = new StringBuilder();
		int years = (int) (seconds / (60 * 60 * 24 * 365));
		if (years > 0) {
			builder.append(years).append(" année");
			if (years > 1)
				builder.append("s");
			builder.append(", ");
			seconds = seconds % (60 * 60 * 24 * 365);
		}
		int weeks = (int) (seconds / (60 * 60 * 24 * 7));
		if (weeks > 0) {
			builder.append(weeks).append(" semaine");
			if (weeks > 1)
				builder.append("s");
			builder.append(", ");
			seconds = seconds % (60 * 60 * 24 * 7);
		}
		int days = (int) (seconds / (60 * 60 * 24));
		if (days > 0) {
			builder.append(days).append(" jour");
			if (days > 1)
				builder.append("s");
			builder.append(", ");
			seconds = seconds % (60 * 60 * 24);
		}
		int hours = (int) (seconds / (60 * 60));
		if (hours > 0) {
			builder.append(hours).append(" heure");
			if (hours > 1)
				builder.append("s");
			builder.append(", ");
			seconds = seconds % (60 * 60);
		}
		int minutes = (int) (seconds / (60));
		if (minutes > 0) {
			builder.append(minutes).append(" minute");
			if (minutes > 1)
				builder.append("s");
			builder.append(", ");
			seconds = seconds % (60);
		}
		if (seconds > 0) {
			builder.append(seconds).append(" seconde");
			if (seconds > 1)
				builder.append("s");
		}
		String str = builder.toString();
		if (str.endsWith(", "))
			str = str.substring(0, str.length() - 2);
		if (str.equals(""))
			str = "Aucun temps";
		return str;
	}

}
