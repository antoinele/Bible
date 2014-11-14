/**
 * 
 */
package aston.ASK.BibleApp;

import java.util.Random;

/**
 * @author antoine
 *
 */
public final class ToLowercaseProfiler
{
    final static int CYCLES = 1000000;
    final static String[] strings = new String[CYCLES];
    
    /**
     * 
     */
    private ToLowercaseProfiler()
    {
        // TODO Auto-generated constructor stub
    }

    static final String AB = ".?!,0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
    static Random rnd = new Random();
    
    static String randomString( int len ) 
    {
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       return sb.toString();
    }
    

    
//    private static Random rnd = new Random();
//    
//    private static String randomString(int length)
//    {
//        StringBuilder sb = new StringBuilder(length);
//        
//        final int range = 'z' - 'a';
//        
//        for(int i=0; i<length; i++)
//        {
//            int c = rnd.nextInt(range);
//            if(rnd.nextBoolean())
//            {
//                sb.append(Character.valueOf((char) (c + 'A')));
//            }
//            else
//            {
//                sb.append(Character.valueOf((char) (c + 'a')));
//            }
//        }
//        
//        return sb.toString();
//    }
    
//    private static final String fastLowercase(String string)
//    {
//        char[] ca = string.toCharArray();
//        
//        for(int i=0; i<ca.length; i++)
//        {
//            ca[i] = (char) (ca[i] & ~0x20); //The 6th bit of an ASCII character determines whether it is upper or lowercase. Here we force it to zero
//        }
//        
//        return new String(ca);
//    }
    
    private static final String fastLowercaseStrip(String string)
    {
        char[] ca = string.toCharArray();
        char[] newca = new char[string.length()];
        
        int j = 0;
        
        for(int i=0; i<ca.length; i++)
        {
            if( (ca[i] <= 'Z' && ca[i] >= 'A') || (ca[i] <= 'z' && ca[i] >= 'a') )
            {
                newca[j] = (char) (ca[i] | (1 << 5)); //The 6th bit of an ASCII character determines whether it is upper or lowercase. Here we force it to zero
                j++;
            }
            else if( ca[i] == ' ' || (ca[i] <= '9' && ca[i] >= '0') ) 
            {
                newca[j] = ca[i];
                j++;
            }
        }
        
        return new String(newca, 0, j);
    }
    
    private static void testFastLowercase()
    {
        final String[] out = new String[CYCLES];
        
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            out[i] = fastLowercaseStrip(strings[i]);
        }
        long end = System.nanoTime();
        
        int invalid = 0;
        
        for(int i=0; i<CYCLES; i++)
        {
            if( (i % 1000) == 0 )
            {
                System.out.println(String.format("%s vs %s", out[i], strings[i]));
            }
            if(!out[i].equalsIgnoreCase(strings[i].replaceAll("[^A-Za-z0-9 ]", "")))
            {
                invalid++;
            }
        }
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds. %d were invalid.", CYCLES, end - start, invalid));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    private static void testJavaLowercase()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            strings[i].toLowerCase();
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        for(int i=0;i<CYCLES;i++)
        {
            strings[i] = randomString(20);
        }
        
        System.out.println(~(1 << 5));
        
        System.out.println(String.format("%c, %c", 'A', (char)('A' | (1 << 5))));
        
        System.out.print("Profiling fast lowercase");
        testFastLowercase();
        
        System.out.print("Profiling java lowercase");
        testJavaLowercase();
    }

}
