import java.util.List;
import java.util.ArrayList;

/**
 * A class called TreeWordCountMap consisting of a search tree that maintains
 * a listof words and their counts. (TreeWordCountMap will contain two very small
 * nested classes called Node and WordCount.)
 *
 * @author Yingqi Ding, 05-21-2018
 */

public class TreeWordCountMap<E extends Comparable<E>>
{
   public static class WordCount
   {
      private String word;
      private int count;

      public WordCount(String w)
      {
         word = w;
         count = 1;
      }

      public String getWord()
      {
         return word;
      }

      public int getCount()
      {
         return count;
      }
   }

   private static class Node
   {
      private WordCount leftWord;
      private WordCount rightWord;
      private Node leftChild;
      private Node middleChild;
      private Node rightChild;
   }

   private List<WordCount> words;
   private Node root;
   private int size;

   public TreeWordCountMap()
   {
      words = new ArrayList<WordCount>();
      root = null;
   }

   /** If the specified word is already in this map, then its count is
       increased by one. Otherwise, the word is added to this map with a count
       of 1.
    */
   public void increment(String word)
   {
      root = increment(root, word);
   }

   private Node increment(Node node, String word)
   {
      if (node == null)
      {
         node = new Node();
         node.leftWord = new WordCount(word);
         size++;
         return node;
      }

      int cmpLeft = word.compareTo(node.leftWord.getWord());
      if (cmpLeft == 0)
      {
         node.leftWord.count++;
      }
      else if (cmpLeft < 0)
      {
         if (node.rightWord == null)
         {
            node.rightWord = node.leftWord;
            node.leftWord = new WordCount(word);
            size++;
         }
         else
         {
            node.leftChild = increment(node.leftChild, word);
         }
      }
      else if (cmpLeft > 0)
      {
         if (node.rightWord == null)
         {
            node.rightWord = new WordCount(word);
            size++;
         }
         else
         {
            int cmpRight = word.compareTo(node.rightWord.getWord());
            if (cmpRight == 0)
            {
               node.rightWord.count++;
            }
            else if (cmpRight < 0)
            {
               node.middleChild = increment(node.middleChild, word);
            }
            else if (cmpRight > 0)
            {
               node.rightChild = increment(node.rightChild, word);
            }
         }
      }
      return node;
   }

   /** Returns true if word is contained in this map. */
   public boolean contains(String word)
   {
      return contains(root, word);
   }

   private boolean contains(Node node, String word)
   {
      List<WordCount> list = getWordCountsByWord();
      for (int i = 0; i < list.size(); i++)
      {
         if (word.compareTo(list.get(i).getWord()) == 0)
         {
            return true;
         }
      }
      return false;
   }

   /** Returns a list of WordCount objects, one per word in this map,
       sorted alphabetically by word.
    */
   public List<WordCount> getWordCountsByWord()
   {
      return getWordCountsByWord(root);
   }

   private List<WordCount> getWordCountsByWord(Node node)
   {
      if (node != null)
      {
         getWordCountsByWord(node.leftChild);
         words.add(node.leftWord);
         if (node.rightWord != null)
         {
            getWordCountsByWord(node.middleChild);
            words.add(node.rightWord);
            getWordCountsByWord(node.rightChild);
         }
      }
      return words;
   }

   /** Returns a list of WordCount objects, one per word in this map,
       sorted in decreasing order by count.
    */
   public List<WordCount> getWordCountsByCount()
   {
      List<WordCount> list = getWordCountsByWord();
      for (int i = 1; i < list.size(); i++)
      {
         int j = i;
         while (j > 0 && list.get(j).count > list.get(j-1).count)
         {
            WordCount temp = list.get(j-1);
            list.set(j-1, list.get(j));
            list.set(j, temp);
            j--;
         }
      }
      return list;
   }
}