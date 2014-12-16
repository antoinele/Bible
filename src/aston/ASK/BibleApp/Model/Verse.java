/**
 * 
 */
package aston.ASK.BibleApp.Model;

/**
 * @author Karol
 */

public final class Verse
{
    public final Chapter chapter;
    public final String text;
    public final int verse;
    
    /**
     * 
     */
    public Verse(Chapter chapter, int verse, String text)
    {
        this.chapter = chapter;
        this.verse = verse;
        this.text = text;
    }

    public final boolean equals(Verse verse)
    {
        return (text.equals(verse.text) && this.verse == verse.verse);
    }
}
