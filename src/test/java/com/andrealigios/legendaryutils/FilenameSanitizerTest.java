package com.andrealigios.legendaryutils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
/**
 * Unit test for FilenameSanitizer.
 */
class FilenameSanitizerTest {

	private final static String FILENAME_ENGLISH = "This is an English document.txt";
	private final static String FILENAME_ITALIAN = "Questo è un documento Italiano.txt";
	private final static String FILENAME_RUSSIAN = "Это российский документ.txt";
	private final static String FILENAME_CHINESE = "这是一个中文文档.txt";
	private final static String FILENAME_HINDI   = "यह एक हिंदी दस्तावेज़ है.txt";
	private final static String FILENAME_ARAB    = "هذه وثيقة عربية.txt";
	
    @Test
    void givenProperMultilingualInput_WhenParsed_ThenOutputUnchanged() {
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_ENGLISH), FILENAME_ENGLISH);
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_ITALIAN), FILENAME_ITALIAN);
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_RUSSIAN), FILENAME_RUSSIAN);
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_CHINESE), FILENAME_CHINESE);
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_HINDI),   FILENAME_HINDI);
    	assertEquals(FilenameSanitizer.sanitize(FILENAME_ARAB),    FILENAME_ARAB);
    }
    
    @Test
    void givenSpecialWindowsCharacters_WhenParsed_ThenReplacedWithUnderscore() {
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
    	
    	assertEquals(FilenameSanitizer.sanitize("A filename with a [|] pipe"), 
												"A filename with a [_] pipe");
    	
    	assertEquals(FilenameSanitizer.sanitize("A filename with a [?] question mark"), 
												"A filename with a [_] question mark");
    	
    	assertEquals(FilenameSanitizer.sanitize("A filename with a [*] asterisk"), 
												"A filename with a [_] asterisk");
    }

    
}
