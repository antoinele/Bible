package aston.ASK.BibleApp;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class WordMap implements Map<String, WordRecord>
{

    private TreeMap<String, WordRecord> tree;
    
    public WordMap()
    {
        tree = new TreeMap<String, WordRecord>();
    }

    @Override
    public void clear()
    {
        tree.clear();
    }

    @Override
    public boolean containsKey(Object word)
    {
        return tree.containsKey(((String)word).toLowerCase());
    }

    @Override
    public boolean containsValue(Object arg0)
    {
        return tree.containsValue(arg0);
    }

    @Override
    public Set<java.util.Map.Entry<String, WordRecord>> entrySet()
    {
        return tree.entrySet();
    }

    @Override
    public WordRecord get(Object word)
    {
        return tree.get(((String)word).toLowerCase());
    }

    @Override
    public boolean isEmpty()
    {
        return tree.isEmpty();
    }

    @Override
    public Set<String> keySet()
    {
        return tree.keySet();
    }

    @Override
    public WordRecord put(String word, WordRecord record)
    {
        word = word.toLowerCase();
        tree.put(word, record);
        
        return record; // Not sure if this is how it's supposed to be
    }

    @Override
    public void putAll(Map<? extends String, ? extends WordRecord> arg0)
    {
        tree.putAll(arg0);
    }

    @Override
    public WordRecord remove(Object arg0)
    {
        tree.remove(arg0);
        
        return (WordRecord) arg0;
    }

    @Override
    public int size()
    {
        return tree.size();
    }

    @Override
    public Collection<WordRecord> values()
    {
        return tree.values();
    }

    public final void countWord(Book book, int chapter, int verse, String word)
    {
        word = word.toLowerCase();
        WordRecord wr = this.get(word);
        
        if(wr == null)
        {
            wr = new WordRecord(word);
            this.put(word, wr);
        }
        
        wr.record(book, chapter, verse);
    }
    
}
