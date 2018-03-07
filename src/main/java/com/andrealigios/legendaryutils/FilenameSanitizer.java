package com.andrealigios.legendaryutils;

import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class FilenameSanitizer {

	private final static Logger LOG = LoggerFactory.getLogger(FilenameSanitizer.class);
	
	public static String sanitizeSafely(String filename) {
		if (filename == null) {
			LOG.warn("Filename was null, and a safe, unique filename has been generated");
			return FILENAME_NULL + System.currentTimeMillis() + "-rnd-" + new Random(System.nanoTime()).nextLong() + EXTENSION_UNKNOWN;
		}
		if (filename.trim().isEmpty()) {
			LOG.warn("Filename was empty, and a safe, unique filename has been generated"); 
			return FILENAME_EMPTY + System.currentTimeMillis() + "-rnd-" + new Random(System.nanoTime()).nextLong() + EXTENSION_UNKNOWN;
		}
		if (filename.length()>MAX_LENGTH) {
			String shortenedFileName = filename.substring(0, MAX_LENGTH); 
			LOG.warn("Filename proposed was too long (" + filename.length() + " chars against " + MAX_LENGTH + " of maximum length), and it has been shortened to:\n" + shortenedFileName);
			return FilenameSanitizer.sanitize(shortenedFileName);
		}
		
		return FilenameSanitizer.sanitize(filename);
	}

	public static String sanitize(String filename) throws NullPointerException, IllegalArgumentException {

		if (filename.trim().isEmpty()) {
			String error = "Input String has zero non-whitespace characters";
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}
		if (filename.length()>256) {
			String error = "Input String is too long, " + MAX_LENGTH + " chars is the maximum allowed.";
			LOG.error(error);
			throw new IllegalArgumentException(error);
		}


		IntStream chars = filename.codePoints();

		String output = chars.map(i -> i > 31 ? i : SANITIZATION_TOKEN)
							 .map(i -> reservedSpecialChars
									 	.codePoints()
									 	.noneMatch(num -> Objects.equals(num, i)) 
									 	? i 
									 	: SANITIZATION_TOKEN)
							 .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
							 .toString();

		LOG.debug("\n INPUT : " + filename + "\n" + "OUTPUT : " + output);
		return output;

	}

	
	private final static int MAX_LENGTH = 256;  // MAX_PATH is 260 and includes drive, colon, backslash and the invisible terminating <NUL> char
	private final static char SANITIZATION_TOKEN = '_';
	
	private final static String FILENAME_NULL 		= "Filename-parameter-was-null-at-time-";
	private final static String FILENAME_EMPTY 		= "Filename-parameter-was-empty-at-time-";
	private final static String EXTENSION_UNKNOWN 	= ".unknown";
	
	private final static String reservedSpecialChars = "<>:\"/\\|?*";
	
	

}
