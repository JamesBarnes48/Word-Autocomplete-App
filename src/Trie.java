import java.io.FileNotFoundException;
import java.util.*;

public class Trie {

    public void main()
    {
        //----reading lotr.csv and populating dictionary
        TreeMap<String, Integer> lotrDictionary;
        try
        {
            ArrayList<String> list = DictionaryMaker.readDocument("lotr.csv");
            lotrDictionary = DictionaryMaker.FormDictionary(list);
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        //create blank root trie and add some words
        Trie trie = new Trie();
        trie.add("flex");
        trie.add("fick");
        trie.add("float");
        //returns false as not complete word in trie
        System.out.println(trie.contains("fl"));
        //returns true as complete word in trie
        System.out.println(trie.contains("flex"));
        System.out.println(trie.outputBreadthFirstSearch());
        System.out.println(trie.outputDepthFirstSearch());
        //creating subtrie from trie
        Trie subtrie = trie.getSubTrie("fl");
        //adds successfully as matches prefix
        subtrie.add("floor");;
        //is not added as doesn't match prefix
        subtrie.add("ford");
        //displays all words in subtrie to console
        ArrayList<String> words = subtrie.getAllWords();
        System.out.println(words);
    }

    //root node in trie
    private TrieNode root;
    //prefix the trie is rooted at
    private String prefix;
    //final TrieNode in a word set after a successful result in containsString
    private TrieNode finalNode;

    //creates a new trie consisting of just a blank root node and empty prefix
    public Trie()
    {
        this.root = new TrieNode();
        this.prefix = "";
    }

    //creates a new trie rooted at the prefix inpPrefix
    public Trie(String inpPrefix)
    {
        this.root = new TrieNode();
        this.prefix = inpPrefix;
    }

    //add a new word to the trie
    public boolean add(String entry)
    {
        TrieNode temp = root;

        //scan through each letter of entry
        for(int i= 0; i < entry.length(); i++)
        {
            char curChar = entry.charAt(i);

            //if the trie prefix is the word to be added, mark root node as complete word
            if(prefix == entry)
            {
                root.setCompleteWord(true);
            }

            try
            {
                //if the current letter is part of the prefix then skip it
                if(curChar == prefix.charAt(i) && i < prefix.length())
                {
                    continue;
                }
                //if the current letter should be part of the prefix but isn't then return false
                if(curChar != prefix.charAt(i) && i < prefix.length())
                {
                    return false;
                }
            }
            catch(StringIndexOutOfBoundsException ex) {}
            try
            {
                //next node to scan is the one holding the current char, if it exists
                TrieNode next = temp.getChild(curChar);
                //traverse to next node on next iteration if next is valid
                temp = next;
            }
            /*in case of NullPointerException and there's no existing
            node holding current char, create a new one and make it next node
            */
            catch(NullPointerException ex)
            {
                temp.setChild(curChar);
                TrieNode next = temp.getChild(curChar);
                //traverse to newly created node on next iteration
                temp = next;
            }
        }
        //set flag on the last letter added
        temp.setCompleteWord(true);
        //return true if full word has been added
        return true;
    }

    /*returns true if the whole word exists in the trie
    /the bulk of code for this method is in containsString to take
    advantage of polymorphism
     */
    public boolean contains(String word)
    {
        //check if word exists in trie
        if(containsString(word))
        {
            /*checking if final node in word makes a complete word
            /finalNode will be set when containsString returns true
            so this will always hold the correct finalNode
             */
            return finalNode.getCompleteWord();
        }
        else
        {
            return false;
        }
    }

    /*checks to see if a given word exists in the trie without being a complete word
    throws NullPointerException if word not found in trie and returns false
    /code has been embedded in this separate method so it can be reused for
    two different methods: contains and getSubTrie
    /also sets finalNode to be used in contains
     */
    private boolean containsString(String text)
    {
        //start at root
        TrieNode temp = root;
        //counter keeps track of current position in word
        int index = 0;

        //----checking to see if word matches prefix----
        for(; index < prefix.length(); index++)
        {
            char curChar = text.charAt(index);
            char prefixChar = prefix.charAt(index);
            //if the word doesnt match the prefix then return false
            if(curChar != prefixChar)
            {
                return false;
            }
        }

        /*----checking to see if word exists in trie----
        continue from previous position in word
         */
        for(; index < text.length(); index++)
        {
            char curChar = text.charAt(index);
            try
            {
                //next node to scan is the offspring containing curChar
                TrieNode next = temp.getChild(curChar);
                //traverse to next node on next iteration if next is valid
                temp = next;
            }
            //if a letter in the word has no existing node then return false
            catch(NullPointerException ex)
            {
                return false;
            }
        }
        //set class attribute finalNode
        finalNode = temp;
        //returns true if all characters are found to match
        return true;
    }

    /*performs a breadth first search on the trie
    /returns a string of all nodes in the order they were visited
     */
    public String outputBreadthFirstSearch()
    {
        //FIFO queue holding all visited nodes
        LinkedList<TrieNode> queue = new LinkedList<TrieNode>();
        //StringBuilder which will form the output string
        StringBuilder output = new StringBuilder();

        //start search at root
        queue.add(this.root);
        //add prefix to output immediately
        output.append(prefix);

        while(!queue.isEmpty())
        {
            //remove first element from queue and store as curNode
            TrieNode curNode = queue.poll();
            //scans through all possible children of curNode
            for(int i = 0; i < 26; i++)
            {
                //if retrieving current element in children array throws exception, continue iterating
                try
                {
                    //if current element is not null, add to queue of visited elements and add output
                    TrieNode curChild = curNode.getChildren()[i];
                    queue.add(curChild);
                    output.append(curChild.getCharacter());
                }
                catch(NullPointerException ex) {}
            }
        }
        //return complete output string
        return output.toString();
    }

    /*performs a recursive depth first search on the trie
    /returns a string of all nodes in the order they were visited
    /the actual search is performed in a separate method: DFTraversal
     */
    public String outputDepthFirstSearch()
    {
        //StringBuilder which will form the output string
        StringBuilder output = new StringBuilder();
        //add prefix to output immediately
        output.append(prefix);

        //perform DFS
        DFTraversal(output, root);

        //return complete output string
        return output.toString();
    }

    /*the bulk of the code for performing a depth first search is here
    /this is the method that is called recursively
     */
    private void DFTraversal(StringBuilder out, TrieNode curNode)
    {
        if(curNode.getChildCount() > 0)
        {
            /*scans through all possible children of curNode
            /if there are no children found, no recursive call is made and begins
            to unwind until a node is reached which has children
             */
            for(int i = 0; i < 26; i++)
            {
                //if retrieving current element in children array throws exception, continue iterating
                try
                {
                    //if current element is not null, add to output string
                    TrieNode child = curNode.getChildren()[i];
                    out.append(child.getCharacter());

                    //recursively call method on child node
                    DFTraversal(out, child);
                }
                catch(NullPointerException ex) {}
            }
        }
    }

    /*returns a trie rooted at the given prefix if it exists in current trie
    /if prefix doesnt exist return null
     */
    public Trie getSubTrie(String prefix)
    {
        if(this.containsString(prefix))
        {
            Trie newTrie = new Trie(prefix);
            ArrayList<String> allWords = this.getAllWords();
            //add each word in old trie to new trie
            for(int i = 0; i < allWords.size(); i++)
            {
                newTrie.add(allWords.get(i));
            }
            //return populated subtrie
            return newTrie;
        }
        else
        {
            return null;
        }
    }

    /*returns a list containing all complete words in the trie
    finding the words is done recursively using findWords
     */
    public ArrayList<String> getAllWords()
    {
        //StringBuilder to form a single word
        StringBuilder curWord = new StringBuilder();
        //ArrayList to contain all words in trie
        ArrayList<String> out = new ArrayList<>();

        //if prefix completes a word by itself, add immediately to output
        if(root.getCompleteWord())
        {
            out.add(prefix);
        }

        //immediately add prefix to curWord
        curWord.append(prefix);

        //find all words in trie and populate out with them
        findWords(out, curWord, root);

        //return populated output arraylist
        return out;
    }

    //called recursively to locate all words in tree and add to out arraylist
    private void findWords(ArrayList<String> out, StringBuilder curWord, TrieNode curNode)
    {
        if(curNode.getChildCount() > 0)
        {
            /*scans through all possible children of curNode
            /if there are no children found, no recursive call is made and begins
            to unwind until a node is reached which has children
             */
            for(int i = 0; i < 26; i++)
            {
                //if retrieving current element in children array throws exception, continue iterating
                try
                {
                    //add child character to curWord
                    TrieNode child = curNode.getChildren()[i];
                    curWord.append(child.getCharacter());

                    //if child completes a word, add the word it completes to out and reset curWord to prefix
                    if(child.getCompleteWord())
                    {
                        out.add(curWord.toString());
                    }

                    //recursively call method on child node
                    findWords(out, curWord, child);

                    /*as recursion unwinds back up the trie, remove each node's
                    character from the current word as it goes
                     */
                    curWord.deleteCharAt(curWord.length() - 1);
                }
                catch(NullPointerException ex) {}
            }
        }
    }
}
