import java.io.*;
import java.util.*;

public class DictionaryMaker {

    public DictionaryMaker()
    {
    }

    //main method demonstrating functionality of this class
    public void main()
    {
        try
        {
            //create list of words from file
            ArrayList<String> list = readDocument("lotr.csv");
            //create dictionary from list of words
            TreeMap<String, Integer> dictionary = FormDictionary(list);
            //save dictionary to csv file
            saveToFile("dictionaryMakerTest.csv", dictionary);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //scans document specified by filename, puts each word into listOfWords and returns it
    public static ArrayList<String> readDocument(String filename) throws FileNotFoundException
    {
        //initialise ArrayList of words
        ArrayList<String> listOfWords = new ArrayList<>();

        //initialise Scanner and set delimiter ready to scan file
        Scanner reader = new Scanner(new File(filename));
        reader.useDelimiter("[,\r\n]+");

        //populate listOfWords with every word in the file
        while(reader.hasNext())
        {
            listOfWords.add(reader.next());
        }
        return listOfWords;
    }

    /*uses listOfWords generated in readDocument to populate dictionary
    and count frequency of each word to act as the Integer value
     */
    public static TreeMap<String, Integer> FormDictionary(ArrayList<String> listOfWords)
    {
        String currentWord;
        TreeMap<String, Integer> dictionary = new TreeMap<String, Integer>();

        //iterate through full list of words
        for(int i = 0; i < listOfWords.size(); i++)
        {
            currentWord = listOfWords.get(i);

            //if dictionary contains current word, add one on to the frequency
            if(dictionary.containsKey(currentWord))
            {
                int newValue = dictionary.get(currentWord) + 1;
                dictionary.replace(currentWord, newValue);
            }
            //put new word onto end of dictionary with 1 instance
            else
            {
                dictionary.put(currentWord, 1);
            }
        }
        return dictionary;
    }

    //writes the dictionary to a csv file
    public static void saveToFile(String filename, TreeMap<String, Integer> dictionary) throws IOException
    {
        //initialise FileWriter object to write to a file
        FileWriter writer = new FileWriter(filename);

        //iterate through dictionary and get key and value for each entry
        for(Map.Entry<String, Integer> curEntry : dictionary.entrySet())
        {
            String key = curEntry.getKey();
            int value = curEntry.getValue();

            //create a string containing key and value delimited by commas and write to file
            StringBuilder entryStr = new StringBuilder();
            entryStr.append(key);
            entryStr.append(',');
            entryStr.append(value);
            entryStr.append(',');
            writer.write(entryStr.toString());
        }

        writer.flush();
        writer.close();
    }

    /*----------example code from blackboard--------------*/
    public static void saveCollectionToFile(Collection<?> c,String file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(Object w: c){
            printWriter.println(w.toString());
        }
        printWriter.close();
    }
}
