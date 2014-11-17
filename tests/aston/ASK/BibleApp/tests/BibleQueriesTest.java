package aston.ASK.BibleApp.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aston.ASK.BibleApp.Model.BibleParser2;
import aston.ASK.BibleApp.Model.WordMap;

public class BibleQueriesTest
{

    WordMap wm;
    
    @Before
    public void setUp() throws Exception
    {
        String[] files = "KJBible/1John.txt KJBible/1Kings.txt KJBible/Psalms.txt KJBible/Revelation.txt".split(" "); 
        
        wm = BibleParser2.parseFiles(files);
    }

    @Test
    public void testCountWordAppearances()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetVersesWithWord()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetVersesStringInt()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetVersesStringIntIntInt()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetVerse()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetShortBookName()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetLongBookName()
    {
        fail("Not yet implemented"); // TODO
    }

}
