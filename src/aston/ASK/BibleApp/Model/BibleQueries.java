package aston.ASK.BibleApp.Model;

import java.util.ArrayList;

/**
 * The BibleQueries class has functions to perform searches on a WordMap
 * @author Antoine and Simon
 *
 */
public class BibleQueries
{
    private final WordMap wordmap;
    
    public BibleQueries(WordMap wordmap)
    {
        this.wordmap = wordmap;
    }
    
    /**
     * Extracts only distinct locations from a list of locations, assuming they're all of the same word.
     * Assumes that the input is pre sorted (which reading from a file it will be)
     * @param wr
     * @return Distinct locations
     */
    private final static WordRecord.WordLocation[] getUniqueLocations(WordRecord wr)
    {
        WordRecord.WordLocation[] locations = wr.getAppearances();
        
        ArrayList<WordRecord.WordLocation> uniqueLocations = 
                new ArrayList<WordRecord.WordLocation>(locations.length);
                            // The ArrayList's size can be up to the number of appearances, but no more.
                            // The ArrayList should be at least that size to avoid the performance hit of it resizing.
        
        /**
         * The most obvious way of finding the unique locations is by brute force ( O(n^2) ). 
         * Because the input is pre sorted, it is possible to merely compare the current and previous items in the list
         * to determine whether they are unique or not.
         */
        
        WordRecord.WordLocation lastLocation = null; // O(1) complexity, assumes input is already sorted, which parsing
                                                     // the bible from beginning to end would be.
        
        for(WordRecord.WordLocation l : locations)
        {
            if(lastLocation != null && lastLocation.equals(l))
            {
                continue;
            }
            else
            {
                uniqueLocations.add(l);
                lastLocation = l;
            }
        }
        
        return uniqueLocations.toArray(new WordRecord.WordLocation[uniqueLocations.size()]);
    }
    
    /**
     * Counts the number of occurrences of a word in the bible
     * @param word
     * @return
     */
    public final int countWordAppearances(String word)
    {
        WordRecord wr = wordmap.get(word.toLowerCase());
        int appearances = wr.getAppearances().length;
        
        return appearances;
    }

    /**
     * Returns the location of the verses where a word appears
     * @param word
     * @return
     */
    public final WordRecord.WordLocation[] getVersesWithWord(String word)
    {
        WordRecord wr = wordmap.get(word.toLowerCase());
        
        WordRecord.WordLocation[] wl = getUniqueLocations(wr);
        
        return wl;
    }
    
    /**
     * Returns the verses in a specified chapter
     * @param bookTitle
     * @param chapter
     * @return
     */
    public final Verse[] getVerses(String bookTitle, int chapter)
    {
        Book book = Book.getBook(bookTitle);
        if(book == null)
            return null;
        
        Chapter[] chapters = book.getChapters();
        if(chapter > chapters.length)
            return null;
        
        return chapters[chapter-1].getVerses();
    }
    
    /**
     * Returns the verses in a specified range from a chapter
     * @param bookTitle
     * @param chapter
     * @param startVerse
     * @param endVerse
     * @return
     */
    public final Verse[] getVerses(String bookTitle, int chapter, int startVerse, int endVerse)
    {
        Verse[] verses = getVerses(bookTitle, chapter);
        
        if(verses == null)
            return null;
        
        if(startVerse > verses.length || endVerse < startVerse)
            return null;
        
        Verse[] res = new Verse[endVerse - startVerse];
        
        for(int i=startVerse-1,j=0; i<endVerse; i++,j++)
        {
            res[j] = verses[i];
        }
        
        return res;
    }
    
    /**
     * Returns a specified verse from a location
     * @param bookTitle
     * @param chapter
     * @param verse
     * @return
     */
    public final Verse getVerse(String bookTitle, int chapter, int verse)
    {
        Verse[] verses = getVerses(bookTitle, chapter);
        if(verses == null)
            return null;
        
        if(verse > verses.length)
            return null;
        
        return verses[verse-1];
    }
    
    /**
     * Gets the short name of a book from the full name
     * @param bookName
     * @return
     */
    public static final String getShortBookName(String bookName) //TODO: Currently unimplemented
    {
        return bookName;
    }
    
    /**
     * Gets the full name of a book from the short name
     * @param bookName
     * @return
     */
    public static final String getLongBookName(String bookName) //TODO: Currently unimplemented
    {
        return bookName;
    }
    
}
