package aston.ASK.BibleApp.Model;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Works under the assumption that all words given to it are lowercase.
 * This is for performance reasons as ensuring the input is lowercase adds seconds to the parsing time.
 * @author Antoine
 *
 */
public final class WordMap
{
    final private Map<String, WordRecord> map;
    
    public WordMap()
    {
        /**
         * Used this command:
         * $ cat KJBible/* | tr '[:upper:]' '[:lower:]' | sed 's/[0-9]//g' | sed 's/\r//g' | sed -e 's/\s\+/\n/g' | sed 's/[^a-z\n]//g' | tr -cd '\11\12\15\40-\176' | sort | uniq | wc -l
12731
         * to determine the number of unique words in the bible
         */
        
        map = new HashMap<String, WordRecord>(13000);
    }
    
    public final WordRecord get(String word)
    {
        return map.get(word);
    }
    
    public final WordRecord put(String word, WordRecord record)
    {
        map.put(word, record);
        
        return record;
    }

    public final int size()
    {
        return map.size();
    }

    /**
     * Record the location of a word
     * @param book
     * @param chapter
     * @param verse
     * @param word Must be lowercase (or the same case for all words)
     */
    public final void countWord(Book book, int chapter, int verse, String word)
    {
        WordRecord wr = this.get(word);
        
        if(wr == null)
        {
            wr = new WordRecord(word);
            this.put(word, wr);
        }
        
        wr.record(book, chapter, verse);
    }
}
