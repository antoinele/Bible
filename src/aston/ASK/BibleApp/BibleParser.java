package aston.ASK.BibleApp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class BibleParser
{
    private Book book;
    private final WordMap wm;
    private BufferedReader br;
//    private int chapters = 0;
    private int currentChapter = 0;
    private int currentVerse = 0;
    
    private BibleParser()
    {
        throw new RuntimeException("This should never be created - It's broken");
//        wm = new WordMap();
    }

    /**
     * Reads positive numbers (>= 0)
     * @return value [0] is the number read, -1 means it didn't read a number. [1] is the last character read
     * @throws IOException 
     */
    private final int[] readNumber() throws IOException
    {
        int n = 0;
        int vn = 0;
        char c;
        
        while(true)
        {
            vn *= 10;
            c = (char)br.read();
            
            if('0' <= c && c <= '9')
            {
                n++;
                c -= '0';
                vn += c;
            }
            else
            {
                break;
            }
        }
        
        return new int[] { (n>0) ? vn : -1, c };
    }
    
    /**
     * Reads beginning of a line, if it reads numbers it will set the verse number and return false, otherwise it will return true
     * @return
     * @throws IOException
     */
    private boolean handleNewLine() throws IOException
    {
        int[] v = readNumber();
        
        if(v[0] > -1)
        {
            currentVerse = v[0];
        }
        
        return (v[0] == -1);
    }
    
    private void handleWord(String word) throws IOException
    {
        if("CHAPTER".equals(word))
        {
            if(br.read() == ' ')
            {
                int[] v = readNumber();
                if(v[0] > -1)
                {
                    currentChapter = v[0];
                }
                else
                {
                    
                }
            }
            String line = br.readLine();
            System.out.println(line);
        }
        else
        {
            wm.countWord(book, currentChapter, currentVerse, word);
        }
    }
    
    public static WordMap parseFile(String file)
    {
        return parseFiles(new String[] {file});
    }
    
    public static WordMap parseFiles(String[] files)
    {
        BibleParser bp = new BibleParser();
        
        for(String file : files)
        {
            String lastWord = "";
            
            try
            {
                bp.br = new BufferedReader(new FileReader(file));
                
                String title = bp.br.readLine();
                bp.book = new Book(title, file);
                
                char c;
                while((c = (char)bp.br.read()) != -1)
                {
                    switch(c)
                    {
                    case '\r':
                        break; //ignore
                    case '\n':
                        if(bp.handleNewLine())
                        {
                            lastWord += c;
                        }
                    case ' ':
                        if(lastWord.length() > 0)
                        {
                            bp.handleWord(lastWord);
                            lastWord = "";
                        }
                        break;
                    default:
                        lastWord += c;
                    }
                }
                
                bp.br.close();
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
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
