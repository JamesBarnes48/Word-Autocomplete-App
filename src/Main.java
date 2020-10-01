import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        TreeMap<String, Integer> lotrDictionary;
        try
        {
            //initialise and populate dictionary
            String fileName = "lotr.csv";
            System.out.println("Creating dictionary from text file: " + fileName);
            ArrayList<String> list = DictionaryMaker.readDocument(fileName);
            lotrDictionary = DictionaryMaker.FormDictionary(list);

            //create array of queries
            String queryDoc = "lotrQueries.csv";
            System.out.println("Fetching queries from: " + queryDoc);
            ArrayList<String> queries = DictionaryMaker.readDocument(queryDoc);

            //for each prefix query, query a trie for it and receive output
            for(int i = 0; i < queries.size(); i++)
            {
                System.out.println("Query " + (i+1) + ":");
                String query = queries.get(i);
                System.out.println("Auto-completing for " + query + "...");
                AutoCompletionTrie.getQueryResults(query, lotrDictionary);
            }
        }
        //catches IOExceptions from reading and writing with csv files
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

    }
}
