# Legendary Utils

[![Build Status](https://travis-ci.org/andrea-ligios/legendary-utils.svg?branch=master)](https://travis-ci.org/andrea-ligios/legendary-utils)
[![Coverage Status](https://coveralls.io/repos/github/andrea-ligios/legendary-utils/badge.svg?branch=master)](https://coveralls.io/github/andrea-ligios/legendary-utils?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> *Epic tools the world needed*

Requirements: Java8+ and SLF4J-API. No other dependencies by-design.

Extensively Unit-Tested with JUnit 5.

## FilenameSanitizer

[FilenameSanitizer](src/main/java/com/andrealigios/legendaryutils/FilenameSanitizer.java) performs the sanitization of a filename in order to make it safe to be used for a file creation on every modern File System. 

Unallowed chars are replaced with a safe token (an underscore) according to every modern File System naming convention.

[READ MORE](DOC_FilenameSanitizer.md).

## WordsCapitalizer

[WordsCapitalizer](src/main/java/com/andrealigios/legendaryutils/WordsCapitalizer.java) performs the full capitalization of a String in the right way (lowering when needed, taking into account Locales and Surrogate Pairs) and also allowing the user to specify custom delimiters and get complex behaviours. Perfect when handling last names, street names, etc. 

[READ MORE](DOC_WordsCapitalizer.md).