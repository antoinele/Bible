/**
 * 
 */
package aston.ASK.BibleApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author antoine
 *
 */
public final class BibleParser2
{
    /**
     * Based off of example from: http://www.drdobbs.com/parallel/java-concurrency-queue-processing-part-1/232700457
     * @author antoine
     *
     */
    private static class ThreadedFileRead extends Thread {
        private final Queue<String> queue;
        private final BufferedReader br;
        
        public ThreadedFileRead(BufferedReader br, int threadN, Queue<String> queue)
        {
            setName("parseline-thread-" + threadN);
            this.br = br;
            this.queue = queue;
        }
        
        @Override
        public void run() {
            String line;
            try
            {
                while((line = br.readLine()) != null)
                {
                    if(line.length() == 0)
                    {
                        continue; //Skip blank lines
                    }
                    
                    synchronized(queue)
                    {
                        queue.add(line.substring(0)); //I hope this copies a string
                        queue.notify();
                    }
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            synchronized(queue)
            {
                queue.add("-STOP-");
                queue.notify();
            }
        }
    }
    
    private Book book;
    private final WordMap wm;
    private BufferedReader br;
//    private int currentChapter = 0;
//    private int currentVerse = 0;

    /**
     * 
     */
    private BibleParser2()
    {
        wm = new WordMap();
    }
    
    /**
     * Extracts and parses words indidually by using indexOf and substring instead of string.split().
     * Causes a massive speed improvement over the previous method
     * @param bp
     * @param chapter
     * @param line
     */
    private final static void parseLine(BibleParser2 bp, Chapter chapter, String line)
    {
        int currentVerse;
//        String[] lineBits = line.toLowerCase().split(" ");
        line = line.toLowerCase();
        
        final int verseEnd = line.indexOf(' ');
        
        if(verseEnd < 0) return;
        
        try
        {
            currentVerse = Integer.parseInt(line.substring(0, verseEnd));
        }
        catch(NumberFormatException e)
        {
            //This happens a lot, seems to be a bit of optional text which isn't very significant
//            System.err.println("Invalid Line?");
//            System.err.println("Line: " + line);
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
    
    static public final WordMap parseFiles(String[] files)
    {   
        BibleParser2 bp = new BibleParser2();
        
        for(String file : files)
        {
            try
            {
                bp.br = new BufferedReader(new FileReader(file), 32768);
                
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
//                        String[] lineBits = line.toLowerCase().split(" ");
//                        
//                        try
//                        {
//                            bp.currentVerse = Integer.parseInt(lineBits[0]);
//                        }
//                        catch(NumberFormatException e)
//                        {
//                            //This happens a lot, seems to be a bit of optional text which isn't very significant
////                            System.err.println("Invalid Line?");
////                            System.err.println("Line: " + line);
//                            continue;
//                        }
//                        
//                        chapter.addVerse(line.replaceFirst("^[0-9]+ ", ""), bp.currentVerse);
//                        
//                        for(int i=1; i < lineBits.length; i++)
//                        {
//                            bp.wm.countWord(bp.book, bp.currentChapter, bp.currentVerse, lineBits[i]);
//                        }
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
    
    static public final WordMap parseFilesMT(String[] files)
    {
        BibleParser2 bp = new BibleParser2();
        
        for(String file : files)
        {
            Queue<String> lineBuffer;
            ThreadedFileRead tfr = null;
        
            lineBuffer = new LinkedBlockingQueue<String>();
            
            try
            {
                bp.br = new BufferedReader(new FileReader(file), 32768);
                

                
                String title = bp.br.readLine();
                bp.book = new Book(title, file);
                
                Chapter chapter = null;
                
                tfr = new ThreadedFileRead(bp.br, 1, lineBuffer);
                tfr.start();
                
                String line;
                while(true)
                {
                    synchronized(lineBuffer)
                    {
                        while(lineBuffer.isEmpty())
                        {
                            try
                            {
                                lineBuffer.wait();
                            }
                            catch (InterruptedException e)
                            {
                                break;
                            }
                        }
                        
                        line = lineBuffer.remove();
                        if("-STOP-".equals(line))
                        {
                            break;
                        }
                        
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
                }
                
                tfr.interrupt();
                
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
