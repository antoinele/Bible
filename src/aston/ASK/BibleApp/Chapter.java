/**
 * 
 */
package aston.ASK.BibleApp;

import java.util.LinkedList;

/**
 * @author antoine
 *
 */
public final class Chapter
{
    private LinkedList<Verse> verses;
    private final Book book;
    
    /**
     * 
     */
    public Chapter(Book book)
    {
        this.book = book;
    }

    public final Verse addVerse(String text)
    {
        Verse v = new Verse(this, text);
        verses.add(v);
        
        return v;
    }
    
    private int cachedHashcode = 0;
    private Verse[] cachedVerses;
    
    public final Verse[] getVerses()
    {
        if(cachedHashcode == verses.hashCode())
        {
            return cachedVerses;
        }
        else
        {
            cachedVerses = verses.toArray(new Verse[verses.size()]);
            cachedHashcode = verses.hashCode();
            
            return cachedVerses;
        }
    }
}
