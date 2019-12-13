package com.appirio.muleapp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public final class CloudHubLogUtils {
	
	private CloudHubLogUtils() {}
	
	private static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static DateTimeFormatter DTF_FOR_DIR_NAME = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
	private static final ZoneId JST = ZoneId.of("Asia/Tokyo");
	private static final ZoneId UTC = ZoneId.of("UTC");
	
	
	/**
	 * @return current JST DateTime with format "yyyy-MM-dd-HHmmss"
	 */
	public static String getCurrentDateTimeStr() {
		return parseToJST(LocalDateTime.now()).format(DTF_FOR_DIR_NAME);
	}
	
	/**
	 * @param prevDateTimeStr (e.g. "2019-11-13 08:24:32.157")
	 * @param logLines
	 * @return newLogLines str
	 */
	public static String extractDiff(String prevDateTimeStr, String[] logLines) {
		LocalDateTime prevDateTime = parseToJST(LocalDateTime.parse(prevDateTimeStr.substring(0, 19), DTF));
		StringBuilder sb = new StringBuilder();
		
		List<String> jstLogLines = Arrays.asList(logLines).stream()
				.map(s -> convert2JstLogLine(s))
				.collect(Collectors.toList());
		
		boolean isNewLogLine = false;
		for(String s: jstLogLines) {
			if (isNewLogLine) {
				sb.append(s).append("\n");
			} else if (isCloudHubLogPrefix(s)) {
				String loggedDateTimeStr = getCloudHubLoggedTimeStr(s);
				LocalDateTime loggedDateTime = parseToJST(LocalDateTime.parse(loggedDateTimeStr.substring(0, 19), DTF));
				boolean isAfter = (loggedDateTime.isEqual(prevDateTime) && isAfterMilliSec(prevDateTimeStr, loggedDateTimeStr)) 
						|| loggedDateTime.isAfter(prevDateTime);
				if (isAfter) {
					sb.append(s).append("\n");
					isNewLogLine = true;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Get latest logged time as String, for example, "2019-11-13 08:24:32.157".
	 * @param logLines
	 * @return Latest Logged Time
	 */
	public static String getLatestLoggedTimeStr(String[] logLines) {
		return Arrays.asList(logLines).stream()
				.filter(s -> StringUtils.isNotBlank(s) && isCloudHubLogPrefix(s))
				.map(s -> convert2JstLogLine(s))
				.map(s -> getCloudHubLoggedTimeStr(s))
				.reduce((first, second) -> second) // get last element
				.orElse(StringUtils.EMPTY);
	}
	
	/**
	 * @param str
	 * @return whether the line has CloudHub Log prefix or not
	 */
	private static boolean isCloudHubLogPrefix(String str) {
		// e.g. [2019-11-13 08:24:32.157] INFO    com.mulesoft.ch.queue.boot.PersistentQueueCoreExtension......
		return StringUtils.isNotBlank(str) && str.startsWith("[") && StringUtils.isNumeric(str.substring(1, 4));
	}
	
	/**
	 * @param logLine
	 * @return DateTime str
	 */
	private static String getCloudHubLoggedTimeStr(String logLine) {
		// e.g. [2019-11-13 08:24:32.157] INFO    com.mulesoft.ch.queue.boot.PersistentQueueCoreExtension......
		return StringUtils.isNotBlank(logLine) ? logLine.replace("[",StringUtils.EMPTY).substring(0, 23) : StringUtils.EMPTY;
	}
	
	/**
	 * @param timeStr
	 * @param targetTimeStr
	 * @return whether target time str's ms is after of not
	 */
	private static boolean isAfterMilliSec(String timeStr, String targetTimeStr) {
		// e.g. 2019-11-13 08:24:32.157
		return Integer.parseInt(timeStr.substring(21, 23)) < Integer.parseInt(targetTimeStr.substring(21, 23));
	}

	/**
	 * @param dateTimeStr (e.g. "2019-11-13 08:24:32.157")
	 * @return converted to JST LocalDateTime
	 */
	private static LocalDateTime parseToJST(LocalDateTime targetTime) {
		return targetTime.atZone(UTC)
				.withZoneSameInstant(JST)
				.toLocalDateTime();
	}
	
	/**
	 * @param line
	 * @return Replaced to JST time Log line
	 */
	private static String convert2JstLogLine(String line) {
		if (isCloudHubLogPrefix(line)) {
			String loggedDateTimeStr = getCloudHubLoggedTimeStr(line);
			String formattedLoggedTime = parseToJST(LocalDateTime.parse(loggedDateTimeStr.substring(0, 19), DTF)).format(DTF);
			return line.replace(loggedDateTimeStr.substring(0, 19), formattedLoggedTime);
		}
		return line;
	}
}
