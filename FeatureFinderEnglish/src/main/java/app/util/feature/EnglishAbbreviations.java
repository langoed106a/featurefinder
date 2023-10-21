package app.util.feature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.context.WebApplicationContext;


/**
 * A concise dictionary of common abbreviations (e.g. Mr., Mrs., etc.) in English.
 * Useful in sentence splitter and word tokenizer.
 *
 * @author Haifeng Li
 */
public class EnglishAbbreviations {
    private Set<String> dictionary;
    private WebApplicationContext applicationContext;

    public EnglishAbbreviations(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
         /**
          * A list of abbreviations.
         */
        dictionary = this.dictionary();
    }

    public Set<String> dictionary() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(applicationContext.getResource("classpath:/wordlists/abbreviations.txt").getInputStream()))) {
            return input.lines().map(String::trim).filter(line -> !line.isEmpty()).collect(Collectors.toSet());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Collections.emptySet();
    }

    /**
     * Returns true if this abbreviation dictionary contains the specified element.
     */
    public boolean contains(String s) {
        return dictionary.contains(s);
    }

    /**
     * Returns the number of elements in this abbreviation dictionary.
     */
    public int size() {
        return dictionary.size();
    }

    /**
     * Returns an iterator over the elements in this abbreviation dictionary.
     */
    public Iterator<String> iterator() {
        return dictionary.iterator();
    }
}