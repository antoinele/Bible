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
    public final Book book;
    public final int chapter; 
    
    /**
     * 
     */
    public Chapter(Book book, int chapter)
    {
        this.book = book;
        this.chapter = chapter;
        
        verses = new LinkedList<Verse>();
    }

    public final Verse addVerse(String text, int verse)
    {
        Verse v = new Verse(this, verse, text);
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
