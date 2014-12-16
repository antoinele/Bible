package aston.ASK.BibleApp.Model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Stores the chapters of a book
 * @author Antoine
 *
 */
public final class Book
{
    static HashMap<String, Book> books = new HashMap<String, Book>();
    
    private LinkedList<Chapter> chapters;
    public final String title;
    public final String file;
    
    public Book(String title, String file)
    {
        this.title = title;
        this.file = file;
        
        chapters = new LinkedList<Chapter>();
        
        books.put(title, this);
    }

    /**
     * Get a book by name
     * @param title
     * @return
     */
    public static final Book getBook(String title)
    {
        return books.get(title);
    }

    /**
     * Returns the last chapter of the book
     * @return
     */
    public final Chapter lastChapter()
    {
        return chapters.pollLast();
    }
    
    public final boolean equals(Book b)
    {
        return (b.title == title);
    }
    
    /**
     * Add a chapter to a book
     * @param chapter
     * @return
     */
    public final Chapter newChapter(int chapter)
    {
        Chapter c = new Chapter(this, chapter);
        chapters.add(c);
        
        return c;
    }
    
    public String getFullName()
    {
    	return title;
    }
    
    public String getShortName()
    {
    	File f = new File(file);
    	String name = f.getName();
    	return name.substring(0, name.lastIndexOf('.'));
    }
    
    private int cachedHashcode = 0;
    private Chapter[] cachedChapters;
    
    /**
     * Returns a list of chapters from the book, it is cached for optimisation.
     * @return
     */
    public final Chapter[] getChapters()
    {
    	//Check if the chapters list has changed
        if(cachedHashcode == chapters.hashCode())
        {
            return cachedChapters;
        }
        else
        {
            cachedChapters = chapters.toArray(new Chapter[chapters.size()]);
            cachedHashcode = chapters.hashCode();
            
            return cachedChapters;
        }
    }
}
