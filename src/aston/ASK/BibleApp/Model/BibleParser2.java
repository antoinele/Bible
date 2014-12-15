package aston.ASK.BibleApp.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Antoine
 *
 */
public final class BibleParser2
{
    private static final String fastLowercaseStrip(String string)
    {
        final char[] ca = string.toCharArray();
        char[] newca = new char[string.length()];
        
        int j = 0;
        
        for(int i=0; i<ca.length; i++)
        {
            final char c = ca[i];
            if( (c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a') )
            {
                newca[j] = (char) (c | (1 << 5)); //The 6th bit of an ASCII character determines whether it is upper or lowercase. Here we force it to zero
                j++;
            }
            else if( c == ' ' || (c <= '9' && c >= '0') ) 
            {
                newca[j] = c;
                j++;
            }
        }
        
        return new String(newca, 0, j);
    }
    
    private Book book;
    private final WordMap wm;
    private BufferedReader br;

    /**
     * 
     */
    private BibleParser2()
    {
        wm = new WordMap();
    }
    
    /**
     * Extracts and parses words individually by using indexOf and substring instead of string.split().
     * Causes a massive speed improvement over the previous method
     * @param bp
     * @param chapter
     * @param line
     */
    private final static void parseLine(BibleParser2 bp, Chapter chapter, String line)
    {
        int currentVerse;
        
//        line = line.toLowerCase();
        line = fastLowercaseStrip(line);
        
        final int verseEnd = line.indexOf(' ');
        
        if(verseEnd < 0) return;
        
        try
        {
            currentVerse = Integer.parseInt(line.substring(0, verseEnd));
        }
        catch(NumberFormatException e)
        {
        	// Not a chapter or verse line
            return;
        }
        
        chapter.addVerse(line.substring(verseEnd+1), currentVerse);
        
        
        // Tokenisation algorithm from: http://stackoverflow.com/a/5965814/626946
        int pos = verseEnd+1, end;
        
        while((end = line.indexOf(' ', pos)) >= 0)
        {
            final String word = line.substring(pos, end);
            
            bp.wm.countWord(bp.book, chapter.chapter, currentVerse, word);
            
            pos = end + 1;
        }
    }
    
    static private final int READBUFFERSIZE = 32768; //32768
    
    static public final WordMap parseFiles(String[] files)
    {   
        BibleParser2 bp = new BibleParser2();
        
        for(String file : files)
        {
            try
            {
                bp.br = new BufferedReader(new FileReader(file), READBUFFERSIZE);
                
                String title = bp.br.readLine();
                bp.book = new Book(title, file);
                
                Chapter chapter = null;
                
                String line;
                while((line = bp.br.readLine()) != null)
                {
                    if(line.length() == 0)
                    {
                        continue; //Skip blank lines
                    }
                    
                    if(line.startsWith("CHAPTER") || line.startsWith("PSALM"))
                    {
                        int currentChapter = Integer.parseInt(line.split(" ")[1]);
                        chapter = bp.book.newChapter(currentChapter);
                    }
                    else
                    {
                        parseLine(bp, chapter, line);
                    }
                }
                
                bp.br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                continue;
            }

        }

        return bp.wm;
    }
    
}
