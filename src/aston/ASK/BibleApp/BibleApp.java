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
	/*
	*the program runs until false
	*/
    private boolean run = true;
    
    /**
     *
     * @author Simon
     * Stores an option for the menu.
     */
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
  
    private BibleApp(WordMap wm)
    {
        wordmap = wm;
    }

    /**
     * The following methods below are menu options that are called by the menu() method.
     */
    @SuppressWarnings("unused")
    private void countOccurrences() throws IOException
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
        
        System.out.println();
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
    
    /**
     *  The menu method creates a menu using the above methods as options.
     */
    private void menu()
    {
        final Option[] options = new Option[] {
                                         new Option("countOccurrences", "Count occurrences of word"),
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
    	if(args.length == 0)
    	{
    	    args = "KJBible/1Chronicles.txt KJBible/1Corinthians.txt KJBible/1John.txt KJBible/1Kings.txt KJBible/1Peter.txt KJBible/1Samuel.txt KJBible/1Thessalonians.txt KJBible/1Timothy.txt KJBible/2Chronicles.txt KJBible/2Corinthians.txt KJBible/2John.txt KJBible/2Kings.txt KJBible/2Peter.txt KJBible/2Samuel.txt KJBible/2Thessalonians.txt KJBible/2Timothy.txt KJBible/3John.txt KJBible/Acts.txt KJBible/Amos.txt KJBible/Colossians.txt KJBible/Daniel.txt KJBible/Deuteronomy.txt KJBible/Ecclesiastes.txt KJBible/Ephesians.txt KJBible/Esther.txt KJBible/Exodus.txt KJBible/Ezekiel.txt KJBible/Ezra.txt KJBible/Galatians.txt KJBible/Genesis.txt KJBible/Habakkuk.txt KJBible/Haggai.txt KJBible/Hebrews.txt KJBible/Hosea.txt KJBible/Isaiah.txt KJBible/James.txt KJBible/Jeremiah.txt KJBible/Job.txt KJBible/Joel.txt KJBible/John.txt KJBible/Jonah.txt KJBible/Joshua.txt KJBible/Jude.txt KJBible/Judges.txt KJBible/Lamentations.txt KJBible/Leviticus.txt KJBible/Luke.txt KJBible/Malachi.txt KJBible/Mark.txt KJBible/Matthew.txt KJBible/Micah.txt KJBible/Nahum.txt KJBible/Nehemiah.txt KJBible/Numbers.txt KJBible/Obadiah.txt KJBible/Philemon.txt KJBible/Philippians.txt KJBible/Proverbs.txt KJBible/Psalms.txt KJBible/Revelation.txt KJBible/Romans.txt KJBible/Ruth.txt KJBible/SongofSolomon.txt KJBible/Titus.txt KJBible/Zechariah.txt KJBible/Zephaniah.txt".split(" ");
    	}
    	//loads essential classes and then loads options
        WordMap wm = BibleParser2.parseFiles(args);
        BibleApp ba = new BibleApp(wm);
        ba.start();
    }

}
