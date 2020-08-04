package Main;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ali on 04/08/2020.
 */
public class Test {
    public static void main(String[] args){
        // Creating a List of Integers
        List<Integer> myList
                = Arrays.asList(1, 2, 3, 4, 5);

        // Using Lists.partition() method to divide
        // the original list into sublists of the same
        // size, which are just views of the original list.
        // The final list may be smaller.
        List<List<Integer> > lists
                = Lists.partition(myList, 5);

        // Displaying the sublists
        for (List<Integer> sublist: lists)
            System.out.println(sublist);
    }


}

