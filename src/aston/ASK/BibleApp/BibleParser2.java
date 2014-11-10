/**
 * 
 */
package aston.ASK.BibleApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author antoine
 *
 */
public final class BibleParser2
{
    private Book book;
    private final WordMap wm;
    private BufferedReader br;
    private int currentChapter = 0;
    private int currentVerse = 0;

    /**
     * 
     */
    private BibleParser2()
    {
        wm = new WordMap();
    }
    
    static public WordMap parseFile(String file)
    {
        return parseFiles(new String[] {file});
    }
    
    static public WordMap parseFiles(String[] files)
    {
        BibleParser2 bp = new BibleParser2();
        
        for(String file : files)
        {
            try
            {
                bp.br = new BufferedReader(new FileReader(file));
                
                String title = bp.br.readLine();
                bp.book = new Book(title, file);
                
                String line;
                while((line = bp.br.readLine()) != null)
                {
                    if(line.length() == 0)
                    {
                        continue; //Skip blank lines
                    }
                    
                    if(line.startsWith("CHAPTER"))
                    {
                        bp.currentChapter = Integer.parseInt(line.split(" ")[1]);
                    }
                    else
                    {
                        String[] lineBits = line.split(" ");
                        
                        try
                        {
                            bp.currentVerse = Integer.parseInt(lineBits[0]);
                        }
                        catch(NumberFormatException e)
                        {
                            //This happens a lot, seems to be a bit of optional text which isn't very significant
//                            System.err.println("Invalid Line?");
//                            System.err.println("Line: " + line);
                            continue;
                        }
                        
                        for(int i=1; i < lineBits.length; i++)
                        {
                            bp.wm.countWord(bp.book, bp.currentChapter, bp.currentVerse, lineBits[i]);
                        }
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
