import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ClassCastException;

/**
 * A class called CloudDreamer containing the main program and any support methods
 * it needs. The main program will be in charge of opening a text file, counting all
 * of the words it contains, and producing one of three types of output.
 *
 * @author Yingqi Ding, 05-21-2018
 */

public class CloudDreamer
{
   /** Give corresponding outputs according to command line argument.
    */
   public static void main(String[] args)
   {
      if (args.length < 2)
      {
         System.err.println("Please correct the command line argument.");
         System.exit(0);
      }

      String fileName = args[0];
      File file = new File(fileName);
      String choice = args[1];
      TreeWordCountMap<String> treeMap = new TreeWordCountMap<String>();

      if (file.length() == 0)
      {
         System.err.println("Empty file!");
         System.exit(0);
      }

      load(file, treeMap);
      if (choice.equals("alphabetical"))
      {
         alphabetical(treeMap);
      }
      else if (choice.equals("frequency"))
      {
         frequency(treeMap);
      }
      else if (args.length == 3 && choice.equals("cloud"))
      {
         try
         {
            int n = Integer.parseInt(args[2]);
            cloud(fileName, treeMap, n);
         }
         catch (ClassCastException e)
         {
            System.err.println(e);
            System.exit(1);
         }
      }
      else
      {
         System.err.println("Please correct the command line argument.");
         System.exit(0);
      }
   }

   /** Load the list of stop words from the file stopwords.txt.
    */
   private static List<String> stopWordsList()
   {
      List<String> list = new ArrayList<String>();
      File sw = new File("stopwords.txt");
      Scanner scanner = null;
      try
      {
         scanner = new Scanner(sw);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e);
         System.exit(1);
      }

      while (scanner.hasNext())
      {
         String w = scanner.next();
         list.add(w);
      }
      scanner.close();
      return list;
   }

   /** Load the target file into a TreeWordCountMap.

       @param treeMap a TreeWordCountMap that contains String data type.
    */
   private static void load(File file, TreeWordCountMap<String> treeMap)
   {
      List<String> stop = stopWordsList();
      Scanner scanner = null;

      try
      {
         scanner = new Scanner(file);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e);
         System.exit(1);
      }

      while (scanner.hasNext())
      {
         String word = scanner.next().replaceAll("[^a-zA-Z]", "");
         if (!word.equals("") && !stop.contains(word.toLowerCase()))
         {
            treeMap.increment(word);
         }
      }
      scanner.close();
   }

   /** The output format is one word per line, each line consisting of a word, a colon,
       and the word's count. This list will be sorted alphabetically by word.

       @param treeMap a TreeWordCountMap that contains String data type.
    */
   public static void alphabetical(TreeWordCountMap<String> treeMap)
   {
      List<TreeWordCountMap.WordCount> list = treeMap.getWordCountsByWord();
      for (TreeWordCountMap.WordCount wc: list)
      {
         System.out.println(wc.getWord() + ": " + wc.getCount());
      }
   }

   /** The output format is one word per line, each line consisting of a word, a colon,
       and the word's count. The words will be sorted in decreasing order by count.

       @param treeMap a TreeWordCountMap that contains String data type.
    */
   public static void frequency(TreeWordCountMap<String> treeMap)
   {
      List<TreeWordCountMap.WordCount> list = treeMap.getWordCountsByCount();
      for (TreeWordCountMap.WordCount wc: list)
      {
         System.out.println(wc.getWord() + ": " + wc.getCount());
      }
   }

   /** This method would generate a word cloud based on the n most common non-stopwords
       in pride.txt. (If pride.txt contains fewer than n non-stopwords, then the cloud
       will use all the words.)

       @param filename a String that is a file's name.
       @param treeMap a TreeWordCountMap that contains String data type.
       @param n an integer that determines how many words will be displayed.
    */
   public static void cloud(String fileName, TreeWordCountMap<String> treeMap, int n)
   {
      List<TreeWordCountMap.WordCount> list = treeMap.getWordCountsByCount();
      List<TreeWordCountMap.WordCount> countList = new ArrayList<TreeWordCountMap.WordCount>();
      WordCloudMaker maker = new WordCloudMaker();

      int countIndex = 0;
      int size = list.size();
      while(countIndex < n && size > 0)
      {
         countList.add(list.get(countIndex));
         countIndex++;
         size--;
      }
      System.out.println(maker.getWordCloudHTML("Word Cloud of " + fileName, countList));
   }
}