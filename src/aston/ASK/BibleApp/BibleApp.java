/**
 * 
 */
package aston.ASK.BibleApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * @author antoine
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

    @SuppressWarnings("unused")
    private void countOccurences() throws IOException
    {
        String word = readString("Word to count:");
        
        WordRecord wr = wordmap.get(word);
        int appearances = wr.getAppearances().length;
        
        System.out.println(String.format("The word \"%s\" appears %d times", word, appearances));
    }
    
    @SuppressWarnings("unused")
    private void getVersesWithWord() throws IOException
    {
        String word = readString("Word to search for:");
        
        WordRecord wr = wordmap.get(word);
        
        WordRecord.WordLocation[] uniqueLocations = getUniqueLocations(wr);
        
    }
    
    @SuppressWarnings("unused")
    private void getLocationsOfVersesWithWord() throws IOException
    {
        final String newline = System.getProperty("line.separator");
        String word = readString("Word to search for:");
        
        WordRecord wr = wordmap.get(word);
//        WordRecord.WordLocation[] locations = wr.getAppearances();
        WordRecord.WordLocation[] locations = getUniqueLocations(wr); // Getting only the unique locations is probably more useful
        
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<locations.length; i++)
        {
            if(i > 0)
            {
                sb.append(", ");
                if((i % 4) == 0)
                {
                    sb.append(newline);
                }
            }
            
            WordRecord.WordLocation l = locations[i];
            sb.append(String.format("[%s %d:%d]", l.book.getTitle(), l.chapter, l.verse));
        }
        
        System.out.println(String.format("Locations where \"%s\" appears:", word));
        System.out.println(sb.toString());
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
                                         new Option("getVersesWithWord", "Verses where word appears"),
                                         new Option("getLocationsOfVersesWithWord", "Location of verses where word appears"),
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
        WordMap wm = BibleParser2.parseFiles(args);
        BibleApp ba = new BibleApp(wm);
        ba.start();
    }

}
