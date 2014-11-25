/**
 * 
 */
package aston.ASK.BibleApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aston.ASK.BibleApp.Model.*;

/**
 * @author antoine and Karol
 *
 */
public class BibleApp
{   
    private boolean run = true;
    
    private final class Option
    {
        public final String methodName;
        public final String text;
        
        public Option(String methodName, String text)
        {
            this.methodName = methodName;
            this.text = text;
        }
    }
    
    private String readString(String question) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print(question + " ");
        
        String response = br.readLine();
        return response;
    }
    
    private int readNumber(String question) throws IOException
    {
        while(true)
        {
            String opt = readString(question);
            
            try
            {
                int number = Integer.parseInt(opt);
                
                return number;
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid option");
                continue;
            }
        }
    }
    
    private void createMenu(Option[] options, String menuname) throws IOException
    {
        System.out.println(menuname);
        for(int i=0; i<options.length; i++)
        {
            System.out.println(String.format(" %d) %s", i+1, options[i].text));
        }

        while(true)
        {
            int option = readNumber("Select option:") - 1;
            if(option < 0 || option > options.length)
            {
                System.out.println("Invalid option");
                continue;
            }
            else
            {
                try
                {
                    this.getClass().getDeclaredMethod(options[option].methodName, new Class[0]).invoke(this);
                    break;
                }
                catch (IllegalAccessException | IllegalArgumentException
                       | InvocationTargetException | NoSuchMethodException
                       | SecurityException e)
                {
                    e.printStackTrace();
                    System.out.println("Broken option. Programmer's fault");
                    continue;
                }
            }
        }
    }
    
    WordMap wordmap;
    /**
     * 
     */
    private BibleApp(WordMap wm)
    {
        wordmap = wm;
    }

    @SuppressWarnings("unused")
    private void countOccurences() throws IOException
    {
        String word = readString("Word to count:");
        
        BibleQueries bq = new BibleQueries(wordmap);
        
        int appearances = bq.countWordAppearances(word);
        
        System.out.println(String.format("The word \"%s\" appears %d times", word, appearances));
    }
    
    @SuppressWarnings("unused")
    private void showVersesWithWord() throws IOException
    {
        String word = readString("Word to search for:");
        
        BibleQueries bq = new BibleQueries(wordmap);
        
        WordRecord.WordLocation[] uniqueLocations = bq.getVersesWithWord(word);
        
        System.out.println("Verses:");
        
        for(WordRecord.WordLocation wl : uniqueLocations)
        {
            System.out.print(String.format("[%s %d:%d] - ", wl.book.title, wl.chapter, wl.verse));
            System.out.println(wl.toString());
        }
    }
    
    @SuppressWarnings("unused")
    private void getLocationsOfVersesWithWord() throws IOException
    {
        final String newline = System.getProperty("line.separator");
        String word = readString("Word to search for:");
        
        BibleQueries bq = new BibleQueries(wordmap);
        
        WordRecord.WordLocation[] uniqueLocations = bq.getVersesWithWord(word);
        
        System.out.println(String.format("Locations where \"%s\" appears:", word));
        
        for(int i=0; i<uniqueLocations.length; i++)
        {
            WordRecord.WordLocation wl = uniqueLocations[i];
            
            if(i > 0)
                System.out.print(", ");
            
            if((i%4)==0)
                System.out.println();
            
            System.out.print(String.format("[%s %d:%d]", wl.book.title, wl.chapter, wl.verse));
        }
    }
    
    @SuppressWarnings("unused")
    private void showChapterInBook() throws IOException
    {
        BibleQueries bq = new BibleQueries(wordmap);
        
        Pattern p = Pattern.compile("([a-zA-Z ,\\.]+) (\\d+)");
        
        Verse[] verses;
        
        while(true)
        {
            String location = readString("Enter location (Title Chapter):");
        
            Matcher m = p.matcher(location);
            
            if(!m.matches())
                continue;
            
            String title = m.group(1);
            int chapter = Integer.parseInt(m.group(2));
            if(chapter <= 0)
                continue;
            
            Book book = Book.getBook(title);
            if(book == null)
                continue;
            
            Chapter[] chapters = book.getChapters();
            if(chapter > chapters.length)
                continue;
            
            verses = bq.getVerses(title, chapter);
            break;
        }
        
        System.out.println("Verses:");
        for(Verse v : verses)
        {
            System.out.println(v.verse + " " + v.text);
        }
    }
    
    @SuppressWarnings("unused")
    private void showVersesInBook() throws IOException
    {
        BibleQueries bq = new BibleQueries(wordmap);
        
        Pattern p = Pattern.compile("([a-zA-Z ,\\.]+) (\\d+):(\\d+)(?:\\-(\\d+))?");
        
        Verse[] verses;
        
        int startVerse, endVerse;
        
        /**
         * Try to get book name, chapter and one or two verse numbers
         */
        while(true)
        {
            String location = readString("Enter location (Title Chapter:Verse[-Verse]):");
        
            Matcher m = p.matcher(location);
            
            if(!m.matches())
                continue;
            
            String title   = m.group(1);
            int chapter;
            try
            {
                chapter    = Integer.parseInt(m.group(2));
                startVerse = Integer.parseInt(m.group(3));
                endVerse   = 0;
                
                if(m.group(4) != null)
                {
                    endVerse = Integer.parseInt(m.group(4));
                }
                else
                {
                    endVerse = startVerse;
                }
            }
            catch(NumberFormatException e)
            {
                System.err.println(e.getMessage());
                continue;
            }
            
            if(chapter <= 0 || startVerse <= 0 || endVerse <= 0)
                continue;
            
            verses = bq.getVerses(title, chapter, startVerse, endVerse);
            
            if(verses == null)
                continue;
            
            break;
        }
        
        System.out.println("Verses:");
        for(Verse v : verses)
        {
            System.out.println(v.verse + " " + v.text);
        }
    }
    
    @SuppressWarnings("unused")
    private void exitProgram()
    {
        run = false;
        
        System.out.println("Goodbye!");
    }
    
    private void menu()
    {
        final Option[] options = new Option[] {
                                         new Option("countOccurences", "Count occurences of word"),
                                         new Option("showVersesWithWord", "Show verses where word appears"),
                                         new Option("getLocationsOfVersesWithWord", "Location of verses where word appears"),
                                         new Option("showChapterInBook", "Show a chapter"),
                                         new Option("showVersesInBook", "Show a range of verses"),
                                         new Option("exitProgram", "Exit")
        };
        
        try
        {
            while(run)
            {
                createMenu(options, "Main menu");
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void start()
    {
        menu();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
//        boolean mt = false;
//        if(args[0].equals("--mt"))
//        {
//            mt = true;
//            args = Arrays.copyOfRange(args, 1, args.length);
//        }
        
        WordMap wm = BibleParser2.parseFiles(args);
        BibleApp ba = new BibleApp(wm);
        ba.start();
    }

}
