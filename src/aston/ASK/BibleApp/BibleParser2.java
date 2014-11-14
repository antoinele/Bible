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
    private static final class ParseLineWork {
        public final Chapter chapter;
        public final String line;
        
        public ParseLineWork(Chapter chapter, String line)
        {
            this.chapter = chapter;
            this.line = line;
        }
    }
    
    /**
     * Based off of example from: http://www.drdobbs.com/parallel/java-concurrency-queue-processing-part-1/232700457
     * @author antoine
     *
     */
    private static class ThreadedParseLine extends Thread {
        private final Queue<ParseLineWork> queue;
        private final BibleParser2 bp;
        
        public ThreadedParseLine(BibleParser2 bp, int threadN, Queue<ParseLineWork> queue)
        {
            setName("parseline-thread-" + threadN);
            this.bp = bp;
            this.queue = queue;
        }
        
        @Override
        public void run() {
            while(true)
            {
                ParseLineWork work;
                
                try {
                    synchronized(queue)
                    {
                        while(queue.isEmpty())
                        {
                            queue.wait();
                        }
                         
                        work = queue.remove();
                    }
                    
                    parseLine(bp, work.chapter, work.line);
                    
                }
                catch(InterruptedException e)
                {
                    break;
                }
            }
        }
    }
    
    public static boolean multithreaded;
    
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
    
    static public final WordMap parseFiles(String[] files, final boolean multithreaded)
    {
        Queue<ParseLineWork> workQueue = null;
        ThreadedParseLine[] workers = null;
        
        BibleParser2.multithreaded = multithreaded;
        
        BibleParser2 bp = new BibleParser2();
        
        if(multithreaded)
        {
            workQueue = new LinkedBlockingQueue<ParseLineWork>();
            
            int cores = Runtime.getRuntime().availableProcessors();
//            int cores = 1;
            
            workers = new ThreadedParseLine[cores];
            
            for(int i=0; i<cores; i++)
            {
                workers[i] = new ThreadedParseLine(bp, i, workQueue);
                workers[i].start();
            }
        }
        
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
                        if(multithreaded)
                        {
                            synchronized(workQueue)
                            {
                                workQueue.add(new ParseLineWork(chapter, line));
                                workQueue.notify();
                            }
                        }
                        else
                        {
                            parseLine(bp, chapter, line);
                        }
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
        
        if(multithreaded)
        {
            for(int i=0; i<workers.length; i++)
            {
                workers[i].interrupt();
            }
        }
        
        return bp.wm;
    }
    
}
