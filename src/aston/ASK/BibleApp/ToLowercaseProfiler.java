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
    final static int CYCLES = 2_000_000;
    final static String[] strings = new String[CYCLES / 1000];
    
    /**
     * 
     */
    private ToLowercaseProfiler()
    {
        // TODO Auto-generated constructor stub
    }

    static final String AB = "[].?!,\"'0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
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
    
    private static final String fastLowercaseStripArray(String string)
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
    
    private static final String fastLowercaseStripArray2(String string)
    {
        char[] ca = string.toCharArray();
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
    
    private static final String fastLowercaseStripArray3(String string)
    {
        final int slen = string.length();
        char[] newca = new char[slen];
        
        int j = 0;
        
        for(int i=0; i<slen; i++)
        {
            final char c = string.charAt(i);
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
    
    private static final String fastLowercaseStripArray4(String string)
    {
        char[] ca = string.toCharArray();
        StringBuffer sb = new StringBuffer(ca.length);
        
        for(int i=0; i<ca.length; i++)
        {
            final char c = ca[i];
            if( (c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a') )
            {
                sb.append((char) (c | (1 << 5))); //The 6th bit of an ASCII character determines whether it is upper or lowercase. Here we force it to zero
            }
            else if( c == ' ' || (c <= '9' && c >= '0') ) 
            {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    private static final String stripNonAlphaNumeric(String string)
    {
        final int slen = string.length();
        StringBuffer sb = new StringBuffer(slen);
        
        for(int i=0; i<slen; i++)
        {
            char c=string.charAt(i);
            if( (c <= 'z' && c >= 'a') || c == ' ' )
            {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    private static void testFastLowercaseStrip()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            BibleParser2.fastLowercaseStrip(strings[i]);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds.", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
        
    private static void testJavaLowercaseStripArray()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            fastLowercaseStripArray(strings[i % (CYCLES / 1000)]);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    private static void testJavaLowercaseStripArray2()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            fastLowercaseStripArray2(strings[i % (CYCLES / 1000)]);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    private static void testJavaLowercaseStripArray3()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            fastLowercaseStripArray3(strings[i % (CYCLES / 1000)]);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    private static void testJavaLowercaseStripArray4()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            fastLowercaseStripArray4(strings[i % (CYCLES / 1000)]);
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }
    
    private static void testJavaLowercaseStrip()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            String s = strings[i].toLowerCase();
            s.replaceAll("^[a-z0-9\\s]", "");
        }
        long end = System.nanoTime();
        
        System.out.println();
        
        System.out.println(String.format("%d lowercasings took: %d nanoseconds", CYCLES, end - start));
        
        {
            double parseTime = Double.valueOf(end - start) / (CYCLES * 1000_000_000d);
            System.out.println(String.format("  Average lowercase time: %f seconds (%f lowercases/second)", parseTime, 1/parseTime));
        }
    }

    private static void testJavaLowercaseStrip2()
    {
        long start = System.nanoTime();
        for(int i=0; i<CYCLES; i++)
        {
            String s = strings[i].toLowerCase();
            stripNonAlphaNumeric(s);
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
        for(int i=0;i<CYCLES / 1000;i++)
        {
            strings[i] = randomString(20);
        }
        
//        System.out.print("Profiling fast lowercase");
//        testFastLowercaseStrip();
        
        System.out.print("Profiling fast lowercase (original array version)");
        testJavaLowercaseStripArray();
        
        System.out.print("Profiling fast lowercase 2 (original array version - reduced array lookups)");
        testJavaLowercaseStripArray2();
        
        System.out.print("Profiling fast lowercase 3 (original array version - charAt)");
        testJavaLowercaseStripArray3();
        
        System.out.print("Profiling fast lowercase 4 (same as 2 but with StringBuffers instead)");
        testJavaLowercaseStripArray4();
        
//        System.out.print("Profiling java lowercase");
//        testJavaLowercaseStrip();
//        
//        System.out.print("Profiling java lowercase - manual strip");
//        testJavaLowercaseStrip2();
    }

}
