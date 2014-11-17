package aston.ASK.BibleApp.Model;

import java.util.LinkedList;

public final class WordRecord
{

    public final class WordLocation
    {
        public final WordRecord wr;
        public final Book book;
        public final int chapter;
        public final int verse;
        public int count; // number of times a word appears in a verse
        
        private WordLocation(WordRecord wr, Book book, int chapter, int verse)
        {
            this.wr = wr;
            this.book = book;
            this.chapter = chapter;
            this.verse = verse;
            this.count = 1;
        }
        
        final public boolean equals(WordLocation wl)
        {
            return (wl.wr == wr && wl.book.equals(this.book) && wl.chapter == this.chapter && wl.verse == this.verse);
        }
        
        final public String toString()
        {
            return book.getChapters()[chapter-1].getVerses()[verse-1].text;
        }
    }
    
    private final String word;
    private final LinkedList<WordLocation> appearances;
    
    public WordRecord(String word)
    {
        this.word = word.toLowerCase();
        this.appearances = new LinkedList<WordLocation>();
    }
    
    public final void record(Book book, int chapter, int verse)
    {
        final WordLocation lastLocation = appearances.peekLast();
        
        if(lastLocation != null && lastLocation.book.equals(book) && lastLocation.chapter == chapter && lastLocation.verse == verse)
        {
            lastLocation.count++;
        }
        else
        {
            WordLocation wl = new WordLocation(this, book, chapter, verse);
            appearances.add(wl);
        }
    }
    
    public final String getWord()
    {
        return word;
    }
    
    public final WordLocation[] getAppearances()
    {
        return appearances.toArray(new WordLocation[appearances.size()]); 
    }
    
    public final int appearanceCount()
    {
        int count = 0;
        
        for(WordLocation wl : appearances)
        {
            count += wl.count;
        }
        
        return count;
    }

    @Override
    public String toString()
    {
        return String.format("%s: %d", word, appearances.size());
    }
    
}
