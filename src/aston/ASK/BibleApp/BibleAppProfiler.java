package aston.ASK.BibleApp;

import aston.ASK.BibleApp.Model.BibleParser2;
import aston.ASK.BibleApp.Model.WordMap;

public class BibleAppProfiler
{
    @interface Profile {
        
    }

    private final static String[] files = "KJBible/1Chronicles.txt KJBible/1Corinthians.txt KJBible/1John.txt KJBible/1Kings.txt KJBible/1Peter.txt KJBible/1Samuel.txt KJBible/1Thessalonians.txt KJBible/1Timothy.txt KJBible/2Chronicles.txt KJBible/2Corinthians.txt KJBible/2John.txt KJBible/2Kings.txt KJBible/2Peter.txt KJBible/2Samuel.txt KJBible/2Thessalonians.txt KJBible/2Timothy.txt KJBible/3John.txt KJBible/Acts.txt KJBible/Amos.txt KJBible/Colossians.txt KJBible/Daniel.txt KJBible/Deuteronomy.txt KJBible/Ecclesiastes.txt KJBible/Ephesians.txt KJBible/Esther.txt KJBible/Exodus.txt KJBible/Ezekiel.txt KJBible/Ezra.txt KJBible/Galatians.txt KJBible/Genesis.txt KJBible/Habakkuk.txt KJBible/Haggai.txt KJBible/Hebrews.txt KJBible/Hosea.txt KJBible/Isaiah.txt KJBible/James.txt KJBible/Jeremiah.txt KJBible/Job.txt KJBible/Joel.txt KJBible/John.txt KJBible/Jonah.txt KJBible/Joshua.txt KJBible/Jude.txt KJBible/Judges.txt KJBible/Lamentations.txt KJBible/Leviticus.txt KJBible/Luke.txt KJBible/Malachi.txt KJBible/Mark.txt KJBible/Matthew.txt KJBible/Micah.txt KJBible/Nahum.txt KJBible/Nehemiah.txt KJBible/Numbers.txt KJBible/Obadiah.txt KJBible/Philemon.txt KJBible/Philippians.txt KJBible/Proverbs.txt KJBible/Psalms.txt KJBible/Revelation.txt KJBible/Romans.txt KJBible/Ruth.txt KJBible/SongofSolomon.txt KJBible/Titus.txt KJBible/Zechariah.txt KJBible/Zephaniah.txt".split(" "); 
    
    private BibleAppProfiler()
    {
        // TODO Auto-generated constructor stub
    }
    
    @Profile
    public static void fileParsingProfiler()
    {        
        final int CYCLES = 100;
        
        long start = System.nanoTime();
        for(int i=0; i < CYCLES; i++)
        {
            if((i%10)==0)
                System.out.println();
            if((i % 5)==0)
                System.out.print(String.format("Cycle #%d ",i));
            
            @SuppressWarnings("unused")
            WordMap wm = BibleParser2.parseFiles(files);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d parses took: %d nanoseconds, parsing %d files each time", CYCLES, end - start, files.length));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average parse time: %f seconds (%f files/second)", parseTime, 1/parseTime));
        }
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * files.length * 1000_000_000d);
            System.out.println(String.format("  Average parse time per file: %f/second", parseTime));
        }
    }
    
//    @Profile
//    public static void fileParsingProfilerMT()
//    {        
//        final int CYCLES = 100;
//        
//        long start = System.nanoTime();
//        for(int i=0; i < CYCLES; i++)
//        {
//            System.out.print("Cycle #"+i+" ");
//            @SuppressWarnings("unused")
//            WordMap wm = BibleParser2.parseFilesMT(files);
//        }
//        long end = System.nanoTime();
//        
//        System.out.println();
//        
//        System.out.println(String.format("%d parses took: %d nanoseconds, parsing %d files each time", CYCLES, end - start, files.length));
//        
//        {
//            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
//            System.out.println(String.format("  Average parse time: %f seconds (%f files/second)", parseTime, 1/parseTime));
//        }
//        
//        {
//            double parseTime = Double.valueOf(end - start) / (CYCLES * files.length * 1000_000_000d);
//            System.out.println(String.format("  Average parse time per file: %f/second", parseTime));
//        }
//    }
    
    @Profile
    public static void searchProfiler()
    {
        final int CYCLES = 100;
        String WORD = "and";
        
        WordMap wm = BibleParser2.parseFiles(files);
        
        long start = System.nanoTime();
        for(int i=0; i < CYCLES; i++)
        {
            System.out.print("Cycle #"+i+" ");
            wm.get(WORD).getAppearances();
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d searches took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average search time: %f seconds (%f searches/second)", parseTime, 1/parseTime));
        }
        
//        {
//            double parseTime = Double.valueOf(end - start) / (CYCLES * files.length * 1000_000_000d);
//            System.out.println(String.format("  Average parse time per file: %f/second (%f/second)", parseTime, 1/parseTime));
//        }
    }

    public static void main(String[] args)
    {
//        Method[] methods = BibleAppProfiler.class.getMethods();
//        
//        for(Method m : methods)
//        {
//            Annotation a = m.getAnnotation(Profile.class);
//            
//            if(a instanceof Profile)
//            {
//                System.out.println(String.format("Profiling \"%s\"", m.getName());
//                
//                
//                try
//                {
//                    m.invoke(null);
//                }
//                catch (IllegalAccessException | IllegalArgumentException
//                       | InvocationTargetException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
        
        System.out.println("Profile file parsing");
        fileParsingProfiler();
        
//        System.out.println("Profile file parsing (Multithreaded)");
//        fileParsingProfilerMT();
        
        System.out.println("Profile searching");
        searchProfiler();
    }

}
