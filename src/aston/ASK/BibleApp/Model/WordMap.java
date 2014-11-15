package aston.ASK.BibleApp.Model;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Works under the assumption that all words given to it are lowercase
 * @author antoine
 *
 */
public final class WordMap
{

    final private Map<String, WordRecord> map;
    
    public WordMap()
    {
        this(false);
    }
    
    public WordMap(boolean multithreaded)
    {
        /**
         * Used this command:
         * $ cat KJBible/* | tr '[:upper:]' '[:lower:]' | sed 's/[0-9]//g' | sed 's/\r//g' | sed -e 's/\s\+/\n/g' | sed 's/[^a-z\n]//g' | tr -cd '\11\12\15\40-\176' | sort | uniq | wc -l
12731
         * to determine the number of unique words in the bible
         */
        
        if(multithreaded)
        {
            map = new Hashtable<String, WordRecord>(13000);
        }
        else
        {
            map = new HashMap<String, WordRecord>(13000);
        }
    }

    //TODO: Reinvestigate the viability of this function; the conversion from char array to String is very slow.
//    /**
//     * Quickly converts uppercase words to lowercase under the assumption that they're alphabetic only
//     * @param string
//     * @return
//     */
//    private static final String fastLowercase(String string)
//    {
//        char[] ca = string.toCharArray();
//        
//        for(int i=0; i<ca.length; i++)
//        {
//            ca[i] = (char) (ca[i] & ~0x20); //The 6th bit of an ASCII character determines whether it is upper or lowercase. Here we force it to zero
//        }
//        
//        return new String(ca);
//    }
    
    public final WordRecord get(String word)
    {
        return map.get(word);
//        return map.get(fastLowercase(word));
    }
    
    public final WordRecord put(String word, WordRecord record)
    {
//        word = word.toLowerCase();
//        word = fastLowercase(word);
        map.put(word, record);
        
        return record; // Not sure if this is how it's supposed to be
    }

    public final int size()
    {
        return map.size();
    }

    public final void countWord(Book book, int chapter, int verse, String word)
    {
//        word = fastLowercase(word);
        WordRecord wr = this.get(word);
        
        if(wr == null)
        {
            wr = new WordRecord(word);
            this.put(word, wr);
        }
        
        wr.record(book, chapter, verse);
    }
    
}
