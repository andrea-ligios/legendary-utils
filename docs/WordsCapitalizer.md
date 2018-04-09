## WordsCapitalizer

[WordsCapitalizer](../src/main/java/com/andrealigios/legendaryutils/WordsCapitalizer.java) performs a smart words capitalization, respecting Unicode and UTF-16 Surrogate Pairs, and a custom Locale if provided.
<p> 
Custom delimiters can be specified in order to instruct the engine how to handle each one of those, 
by capitalizing only before, only after, or before and after the marker, to handle cases like O'Brian when parsing last names, for example.
 
Originally posted on November 30, 2012, on [StackOverflow](https://stackoverflow.com/a/13649579/1654265):

It performs a <strong>full capitalization</strong>, meaning that uppercase characters will be set to lowercase if they're not supposed to be capital. 


to-be-continued


Related: [Original StackOverflow Answer](https://stackoverflow.com/a/13649579/1654265)
Related: [Infamous Turkish Locale Bug 1](http://mattryall.net/blog/2009/02/the-infamous-turkish-locale-bug)
Related: [Infamous Turkish Locale Bug 2](https://garygregory.wordpress.com/2015/11/03/java-lowercase-conversion-turkey/)
