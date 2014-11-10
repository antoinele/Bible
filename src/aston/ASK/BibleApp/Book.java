package aston.ASK.BibleApp;

import java.util.HashMap;
import java.util.LinkedList;

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

    public static final Book getBook(String title)
    {
        return books.get(title);
    }
    
//    public final String getTitle()
//    {
//        return title;
//    }
//    
//    public final String getFile()
//    {
//        return file;
//    }
//    
//    public final void setChapters(int n)
//    {
//        chaptersN = n;
//    }
    
    public final Chapter lastChapter()
    {
        return chapters.pollLast();
    }
    
    public final boolean equals(Book b)
    {
        return (b.title == title);
    }
    
    public final Chapter newChapter(int chapter)
    {
        Chapter c = new Chapter(this, chapter);
        chapters.add(c);
        
        return c;
    }
    
    private int cachedHashcode = 0;
    private Chapter[] cachedChapters;
    
    public final Chapter[] getChapters()
    {
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
