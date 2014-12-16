/**
 * 
 */
package aston.ASK.BibleApp.Model;

import java.util.LinkedList;

/**
 * @author Antoine
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

    /**
     * Add a verse to the chapter
     * @param text
     * @param verse
     * @return
     */
    public final Verse addVerse(String text, int verse)
    {
        Verse v = new Verse(this, verse, text);
        verses.add(v);
        
        return v;
    }
    
    /*
     * These variables are used to cache the output of the getVerses function
     */
    private int cachedHashcode = 0;
    private Verse[] cachedVerses;
    
    /**
     * Returns a list of verses from the chapter, it is cached for optimisation.
     * @return
     */
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
