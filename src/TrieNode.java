import com.sun.istack.internal.NotNull;

public class TrieNode {

    private char character;
    private TrieNode[] children = new TrieNode[26];
    private boolean isCompleteWord;

    //constructor for creating non-root node
    public TrieNode(Character inpChar)
    {
        this.character = inpChar;
    }

    //default constructor for creating root node
    public TrieNode() {}

    //create a new child node for character inpChar
    public void setChild(char inpChar)
    {
        //convert character ASCII value to it's index in the alphabet
        int charIndex = inpChar - 97;

        //create new child node at index with inpChar
        children[charIndex] = new TrieNode(inpChar);
    }

    //retrieves child node whose character matches k
    public TrieNode getChild(char k) throws NullPointerException
    {
        //iterate through children array
        for(int i = 0; i < 26; i++)
        {
            //if children[i] is null, catch the exception thrown when trying to access it
            try
            {
                if(children[i].getCharacter() == k)
                {
                    return children[i];
                }
            }
            catch(Exception ex) {}
        }
        //throw exception if matching child not found
        throw new NullPointerException();
    }

    //returns how many children this node has
    public int getChildCount()
    {
        int total = 0;

        //take running total of how many children are in children array
        for(int i = 0; i < 26; i++)
        {
            //if children[i] is null, catch the exception thrown when trying to access it
            try
            {
                if(children[i] != null)
                {
                    total++;
                }
            }
            catch(Exception ex) {}
        }
        return total;
    }

    //returns character held by node
    public char getCharacter()
    {
        return character;
    }

    //returns children array
    public TrieNode[] getChildren()
    {
        return children;
    }

    //returns value of isCompleteWord
    public boolean getCompleteWord()
    {
        return isCompleteWord;
    }

    //sets isCompleteWord based on arg
    public void setCompleteWord(boolean arg)
    {
        this.isCompleteWord = arg;
    }
}
