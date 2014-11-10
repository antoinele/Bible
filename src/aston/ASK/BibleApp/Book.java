package aston.ASK.BibleApp;

import java.util.LinkedList;

public final class Book
{
    private LinkedList<Chapter> chapters;
    private final String title;
    private final String file;
    private int chaptersN;
    
    public Book(String title, String file)
    {
        this.title = title;
        this.file = file;
    }

    public final String getTitle()
    {
        return title;
    }
    
    public final String getFile()
    {
        return file;
    }
//    
//    public final void setChapters(int n)
//    {
//        chaptersN = n;
//    }
    
    public final Chapter[] getChapters()
    {
        return chapters.toArray(new Chapter[chapters.size()]);
    }
    
    public final Chapter lastChapter()
    {
        return chapters.pollLast();
    }
    
    public final boolean equals(Book b)
    {
        return (b.title == title);
    }
    
    public final Chapter newChapter()
    {
        Chapter c = new Chapter(this);
        chapters.add(c);
        
        return c;
    }
}
