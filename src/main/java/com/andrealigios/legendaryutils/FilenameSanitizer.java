/* MIT License

Copyright (c) 2018 Andrea Ligios

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.andrealigios.legendaryutils;

import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrea Ligios
 * @version 1.0.0
 * @see <a href="https://github.com/andrea-ligios/legendary-utils">Legendary Utils</a>
 * 
 * FilenameSanitizer performs the sanitization of a filename in order to make it safe to be used for a file creation on every modern File System. 
 * <p>
 * Unallowed chars are replaced with a safe token (an underscore) according to every modern File System naming convention.
 * <p>
 * <b>Standard Sanitization</b> replaces:
 * <ul>
 * <li> the {@code NUL} (0) character
 * <li> Control Codes between 1 and 31
 * <li> {@code <} (less than)
 * <li> {@code >} (greater than)
 * <li> {@code :} (colon)
 * <li> {@code "} (double quote)
 * <li> {@code /} (forward slash)
 * <li> {@code \} (backslash)
 * <li> {@code |} (vertical bar or pipe)
 * <li> {@code ?} (question mark)
 * <li> {@code *} (asterisk)
 * <li> leading and trailing spaces 
 * </ul>
 * 
 * In addition, it prepends a safe token to files which correspond to one of the Windows reserved filenames (CON, PRN, AUX, NUL, COM1, COM2, COM3, COM4, COM5, COM6, COM7, COM8, COM9, LPT1, LPT2, LPT3, LPT4, LPT5, LPT6, LPT7, LPT8, and LPT9), with or without extension.
 * <p>
 * 
 * <b>Safe Sanitization</b> also handles invalid inputs, specifically:
 * <ul>
 * <li> null file names, by generating a safe, unique filename
 * <li> empty or whitespace-only file names, by generating a safe, unique filename 
 * <li> file names too long (higher than 256 charaters, since on Windows the MAX_PATH is 260 characters and includes drive letter, colon, backslash and terminal NUL, like C:\file-256-chars-long<NUL>.
 * </ul><p>
 * 
 * <b>Pretty Sanitization</b> behaves identically to a Safe Sanitization, but it also removes characters which (even if allowed) could be dangerous or annoying. Specifically:
 * <ul>
 * <li> leading hyphens (make tricky to perform "rm -filename" since - is used to express command options)
 * <li> leading dots (make files semi-hidden on *NIX systems, and dangerous to be removed massively with "rm .*")
 * <li> trailing dots (make Windows angry)
 * </ul><p>
 *
 * The logging is performed through SLF4J, which defaults to NOP (No OPeration) if no binding is be specified. 
 *
 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa365247(v=vs.85).aspx">Naming Files, Paths, and Namespaces</a>
 * @see <a href="https://www.dwheeler.com/essays/fixing-unix-linux-filenames.html">Fixing Unix/Linux/POSIX Filenames: Control Characters (such as Newline), Leading Dashes, and Other Problems</a>
 * 
 */
public class FilenameSanitizer {

    private final static Logger LOG = LoggerFactory.getLogger(FilenameSanitizer.class);

    final static int            MAX_LENGTH              = 256;  
    final static char           SANITIZATION_TOKEN      = '_';  
    final static String         FILENAME_NULL           = "Filename-parameter-was-null-at-time-";
    final static String         FILENAME_EMPTY          = "Filename-parameter-was-empty-at-time-";
    final static String         EXTENSION_UNKNOWN       = ".unknown";   
    final static String         RESERVED_CHARS          = "<>:\"/\\|?*";
    final static String[]       RESERVED_NAMES          = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", 
                                                           "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", 
                                                           "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", 
                                                           "LPT6", "LPT7", "LPT8", "LPT9"};
    

    /**
     * Make the filename safe to be used on basically every modern File System. 
     * 
     * @param   filename    the file name to sanitize
     * @return              the file name sanitized and trimmed 
     * @throws              NullPointerException if filename is null
     * @throws              IllegalArgumentException if filename is empty or whitespace only
     */
    public static String sanitize(String filename) {
        return sanitization(filename,false,false);
    }

    /**
     * Make the filename safe to be used on basically every modern File System. 
     * If a null, empty or whitespaces-only filename is passed, a safe filename is generated and no exception is thrown.  
     * 
     * @param   filename    the file name to sanitize
     * @return              the file name sanitized and trimmed 
     */
    public static String sanitizeSafely(String filename) {
        return sanitization(filename,true,false);
    }

    /**
     * Make the filename safe to be used on basically every modern File System. 
     * If a null, empty or whitespaces-only filename is passed, a safe filename is generated and no exception is thrown.  
     * Trailing dots, leading dots and leading hyphens are stripped out in order to make the file OS-friendly.
     * 
     * 
     * @param   filename    the file name to sanitize
     * @return              the file name sanitized, trimmed and prettified
     */
    public static String sanitizePrettily(String filename) {
        return sanitization(filename,true,true);
    }
    

    
    private static String sanitization(String input, boolean safely, boolean prettily) {
        
        String filename = (safely) 
                    ? ((prettily) 
                        ? makePretty(makeSafe(input)) 
                        : makeSafe(input)) 
                    : input;

        throwErrorIfUnsafe(filename);

        String output = handleReservedNames(handleReservedChars(filename));
        
        LOG.debug("\n INPUT : {}\nOUTPUT : {}\n" , input, output);
        return output;
    }

    /** 
     * If a sanitized filename, extension or not, is equals to one of the Windows reserved names, prefix the file with a safe token (an underscore), making the filename legit.
     */
    private static String handleReservedNames(String filename) {
        String filenameWithoutExtension = stripExtensionIfAny(filename);

        if (Stream.of(RESERVED_NAMES).anyMatch(s -> s.equalsIgnoreCase(filenameWithoutExtension))){
            return SANITIZATION_TOKEN + filename;
        }        
        return filename;
    }
    
    private static String stripExtensionIfAny(String filename) {
        if (filename.indexOf('.')>0) {
            return filename.substring(0,filename.lastIndexOf('.'));
        }
        return filename;
    }
    
    private static String handleReservedChars(String filename) {
        return filename
                .codePoints()
                .map(i -> i > 31 ? i : SANITIZATION_TOKEN)
                .map(i -> RESERVED_CHARS
                        .codePoints()
                        .noneMatch(num -> Objects.equals(num, i)) 
                        ? i 
                        : SANITIZATION_TOKEN)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().trim();
    }
    
    private static String makeSafe(String filename) {
        return handleMaxLength(handleEmpty(handleNull(filename)));
    }
    
    private static String makePretty(String filename) {
        String output = filename;

        // Prevents filename ending in dot, which are dangerous on windows
        while (output.length()>0 && (output.endsWith(".") || output.endsWith(" "))) {
            output = output.substring(0,output.length()-1);
        }

        // Prevents filename starting with an hyphen which are dangerous in bash commands
        while (output.length()>0 && (output.startsWith("-") || output.startsWith(".") || output.startsWith(" "))) {
            output = output.substring(1,output.length());
        }
        
        return handleEmpty(output.trim());
    }

    private static String handleNull(String filename) {
        if (filename == null) {
            LOG.warn("Filename was null, and a safe, unique filename has been generated");
            return FILENAME_NULL + System.currentTimeMillis() + "-rnd-" + new Random(System.nanoTime()).nextLong() + EXTENSION_UNKNOWN;
        }
        return filename;
    }
    
    private static String handleEmpty(String filename) {
        if (filename.trim().isEmpty()) {
            LOG.warn("Filename was empty, and a safe, unique filename has been generated"); 
            return FILENAME_EMPTY + System.currentTimeMillis() + "-rnd-" + new Random(System.nanoTime()).nextLong() + EXTENSION_UNKNOWN;
        }
        return filename;
    }
    
    private static String handleMaxLength(String filename) {
        if (filename.length()>MAX_LENGTH) {
            String shortenedFileName = filename.substring(0, MAX_LENGTH); 
            LOG.warn("Filename proposed was too long ({} chars against {} of maximum length), and it has been shortened to: {}",filename.length(), MAX_LENGTH, shortenedFileName);
            return shortenedFileName;
        }
        return filename;
    }

    private static void throwErrorIfUnsafe(String filename) {
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
    }

}
