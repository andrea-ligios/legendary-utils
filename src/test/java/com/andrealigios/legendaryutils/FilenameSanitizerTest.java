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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
/**
 * Unit test for FilenameSanitizer.
 */
class FilenameSanitizerTest {

    // FILENAME WITH INTERNATIONALIZED INPUTS
    @Test
    void givenProperMultilingualInput_whenSanitized_ThenOutputUnchanged() {
        String FILENAME_ENGLISH = "This is an English document.txt";
        String FILENAME_ITALIAN = "Questo è un documento Italiano.txt";
        String FILENAME_RUSSIAN = "Это российский документ.txt";
        String FILENAME_CHINESE = "这是一个中文文档.txt";
        String FILENAME_HINDI   = "यह एक हिंदी दस्तावेज़ है.txt";
        String FILENAME_ARAB    = "هذه وثيقة عربية.txt";
    
        assertEquals(FilenameSanitizer.sanitize(FILENAME_ENGLISH), FILENAME_ENGLISH);
        assertEquals(FilenameSanitizer.sanitize(FILENAME_ITALIAN), FILENAME_ITALIAN);
        assertEquals(FilenameSanitizer.sanitize(FILENAME_RUSSIAN), FILENAME_RUSSIAN);
        assertEquals(FilenameSanitizer.sanitize(FILENAME_CHINESE), FILENAME_CHINESE);
        assertEquals(FilenameSanitizer.sanitize(FILENAME_HINDI),   FILENAME_HINDI);
        assertEquals(FilenameSanitizer.sanitize(FILENAME_ARAB),    FILENAME_ARAB);
    }

    // FILENAME WITH SURROGATE PAIR 
    @Test
    void givenSurrogatePair_whenSanitized_ThenOutputUnchanged() {
        String FILENAME_WITH_SURROGATE_PAIR = "This is a document with a Surrogate Pair (𐐂) in it.txt";
        assertEquals(FilenameSanitizer.sanitize(FILENAME_WITH_SURROGATE_PAIR), FILENAME_WITH_SURROGATE_PAIR);
    }

    // FILENAME WITH CONTROL CODES
    @Test
    void givenControlCodes_whenSanitized_ThenReplacedWithUnderscore() {
        for (int x=0;x<32;x++) {
            assertEquals(FilenameSanitizer.sanitize("A filename with control code n."+x+"["+(char)x+"]"), 
                                                    "A filename with control code n."+x+"[_]");
        }
    }

    // FILENAME WITH RESERVED CHARS
    @Test
    void givenSpecialCharacters_whenSanitized_ThenReplacedWithUnderscore() {
        assertEquals(FilenameSanitizer.sanitize("A filename with a [<] less-than sign"), 
                                                "A filename with a [_] less-than sign");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [>] more-than sign"), 
                                                "A filename with a [_] more-than sign");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [:] colon"), 
                                                "A filename with a [_] colon");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [\"] quotation marks"), 
                                                "A filename with a [_] quotation marks");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [/] slash"), 
                                                "A filename with a [_] slash");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [\\] backslash"), 
                                                "A filename with a [_] backslash");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [|] vertical bar"), 
                                                "A filename with a [_] vertical bar");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [?] question mark"), 
                                                "A filename with a [_] question mark");
        
        assertEquals(FilenameSanitizer.sanitize("A filename with a [*] asterisk"), 
                                                "A filename with a [_] asterisk");
    }

    // FILENAME WITH RESERVED NAMES
    @Test
    void givenReservedNames_whenSanitized_ThenPrefixedWithUnderscore() {
        Stream.of(FilenameSanitizer.RESERVED_NAMES).forEach(reservedName -> {           
            assertEquals(FilenameSanitizer.sanitize(reservedName), "_" + reservedName);
            assertEquals(FilenameSanitizer.sanitize(reservedName + ".txt"), "_" + reservedName + ".txt");
        });
    }
    
    // FILENAME TOO LONG
    @Test
    void givenFilenameTooLong_whenSanitized_ThenIllegalArgumentExceptionIsThrown() {
        String stringOf16Chars = "1234567890abcdef";
        String maximumFileNameOf256Chars    = String.join("", Collections.nCopies(16, stringOf16Chars));
        String excessiveFileNameOf272Chars  = String.join("", Collections.nCopies(17, stringOf16Chars));        
        
        assertEquals(FilenameSanitizer.sanitize(maximumFileNameOf256Chars), maximumFileNameOf256Chars);
        assertThrows(IllegalArgumentException.class, () -> FilenameSanitizer.sanitize(excessiveFileNameOf272Chars));
    }
    @Test
    void givenFilenameTooLong_whenSanitizedSafely_ThenFilenameShortened() {
        String stringOf16Chars = "1234567890abcdef";
        String maximumFileNameOf256Chars    = String.join("", Collections.nCopies(16, stringOf16Chars));
        String excessiveFileNameOf272Chars  = String.join("", Collections.nCopies(17, stringOf16Chars));        

        assertEquals(FilenameSanitizer.sanitizeSafely(excessiveFileNameOf272Chars), maximumFileNameOf256Chars);
    }    

    // FILENAME EMPTY or WHITESPACES-ONLY
    @Test
    void givenEmptyOrWhitespaces_whenSanitized_ThenIllegalArgumentExceptionIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> FilenameSanitizer.sanitize(""));
        assertThrows(IllegalArgumentException.class, () -> FilenameSanitizer.sanitize(" "));
    }    
    @Test
    void givenEmptyOrWhitespaces_whenSanitizedSafely_ThenPlaceholderIsGenerated() {
        assertTrue(FilenameSanitizer.sanitizeSafely("").startsWith(FilenameSanitizer.FILENAME_EMPTY));
        assertTrue(FilenameSanitizer.sanitizeSafely(" ").startsWith(FilenameSanitizer.FILENAME_EMPTY));
    }

    // FILENAME NULL
    @Test
    void givenNull_whenSanitized_ThenNullPonterExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> FilenameSanitizer.sanitize(null));
    }    
    @Test
    void givenNull_whenSanitizedSafely_ThenPlaceholderIsGenerated() {
        assertTrue(FilenameSanitizer.sanitizeSafely(null).startsWith(FilenameSanitizer.FILENAME_NULL));     
    }

    // FILENAME WITH TRAILING DOTS / WHITESPACES
    @Test
    void givenTrailingDots_whenSanitizedPrettily_ThenStrippedOut() {        
        String filenameEndingIndot      = "A filename ending in dot.";      
        String filenameEndingIndots1    = "A filename ending in dot....";
        String filenameEndingIndots2    = " - A filename ending in dot .. .. ";
        String expected                 = "A filename ending in dot";
        
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameEndingIndot), expected);
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameEndingIndots1), expected);
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameEndingIndots2), expected);
                
        String dotsOnly = "....";
        assertTrue(FilenameSanitizer.sanitizePrettily(dotsOnly).startsWith(FilenameSanitizer.FILENAME_EMPTY));
    }

    // FILENAME WITH LEADING HYPHENS / WHITESPACES 
    @Test
    void givenLeadingHyphens_whenSanitizedPrettily_ThenStrippedOut() {      
        String filenameStartingWithHyphen       = "-A filename starting with hypen";
        String filenameStartingWithHyphens1     = "----A filename starting with hypen";
        String filenameStartingWithHyphens2     = " --. -- A filename starting with hypen . ";
        String filenameStartingWithHyphens3     = ".---- A filename starting with hypen.";
        String expected                         = "A filename starting with hypen";
                
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameStartingWithHyphen), expected);
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameStartingWithHyphens1), expected);
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameStartingWithHyphens2), expected);
        assertEquals(FilenameSanitizer.sanitizePrettily(filenameStartingWithHyphens3), expected);
        
        String hyphensOnly = "----";
        assertTrue(FilenameSanitizer.sanitizePrettily(hyphensOnly).startsWith(FilenameSanitizer.FILENAME_EMPTY));
    }    
    
}
