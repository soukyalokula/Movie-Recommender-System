

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.text.Normalizer;



public class Hw5b
{
        public static void main(String[] args) throws IOException
        {
		int choice = 0;
                Hw5a obj = new Hw5a();		//object of Hw5a class
		Hw5b obj1 = new Hw5b();		//object of Hw5b class
	        ArrayList<ArrayList<Integer>> ReviewerRating = new ArrayList<ArrayList<Integer>>();     //to store the ratings of each reviewer	
                ArrayList<String> moviesArr = new ArrayList<>();		//to store the movie names
		ArrayList<Integer> itemArr = new ArrayList<>();			//to store the movie numbers
                obj1.readMovieName(moviesArr,itemArr);				//calling the method of Hw5b class to read the movie list
		obj.processMovieRating();					//to create the serializable file
                ReviewerRating = obj1.readMovieRating(ReviewerRating);		//to read from the serialized file
		
		do{
			choice = obj1.getChoice(moviesArr);
			obj1.computePearsons(ReviewerRating,moviesArr,itemArr,choice);
		}while((choice>0)||(choice<moviesArr.size()));
        }

	/*****************************************************************************
        Function:getChoice
        Arguments: ArrayList of Strings
        Return Type: int
        Description: Obtains the input from user
        *****************************************************************************/


	public int getChoice(ArrayList<String> movies)
	{
		int choice=0;
        	Scanner userInput= new Scanner(System.in);
                do{
                	System.out.printf("\n%s\n","Movie number:");
                        String temp=userInput.nextLine();
                       	if((userInput.equals("q"))||(userInput.equals("quit")))		//exits the program if user enters q/quit 
				System.exit(0);	//exits the program
                        else 
			{
				try{
                             		choice=Integer.parseInt(temp);	
                       		}
			
                        	catch(NumberFormatException e ){
                       			System.out.println("Invalid entry, try again.\n");
			       		continue;						
                        	}
			}
                        if((choice<=0) || (choice > movies.size()))			//entered choice has to be between 0 and 1682
                        	System.out.printf("%s %d %s\n","Out of range: Number should be less than",movies.size(),".");
              	}while(choice<=0 || choice>movies.size());
                System.out.printf("%s %s %s %d\n\n\n","Movie Name: ",movies.get(choice-1),"Movie Number: ",choice);
                return choice;
	}
	

	/*****************************************************************************
        Function:readMovieName
        Arguments: ArrayList of Strings and ArrayList of Integers
        Return Type: Void
        Description: Reading the file movie-names2.txt
        *****************************************************************************/

	public void readMovieName(ArrayList<String> moviesArr,ArrayList<Integer> arrList) throws IOException
	{
		Hw5a objA = new Hw5a();
                objA.processMovieName(moviesArr,arrList);

		Scanner inputFile = null;
		try{
			inputFile = new Scanner(Paths.get("movie-names2.txt"));		//reading the file movie-names2.txt
		}	
		catch(IOException io)
		{
			System.out.println("Error opening file. Terminating..");
			System.exit(1);
		}
		
	}
	
	/*****************************************************************************
        Function:readMovieRating
        Arguments: 2D ArrayList of Integers 
        Return Type: 2D ArrayList of Integers
        Description: Reading the file movie-matrix.ser
        *****************************************************************************/
	
	public ArrayList<ArrayList<Integer>> readMovieRating(ArrayList<ArrayList<Integer>> reviewerRating)
	{
		try{
                        //use buffering to read from the serialized object
                        InputStream file1 = new FileInputStream("movie-matrix.ser");
                        InputStream buffer1 = new BufferedInputStream(file1);
                        ObjectInput input1 = new ObjectInputStream (buffer1);
                        try{
                                //deserialize the List
                                //ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
                                reviewerRating = (ArrayList<ArrayList<Integer>>)input1.readObject();		
                                //System.out.println(reviewerRating.get(0));
				//reviewerRating = reviewerRating;

                        }
                        finally{
                                input1.close();
                        }
                }
                catch(ClassNotFoundException ex){
                        System.out.println("Improper in reading");
                        System.exit(1);
                }
                catch(IOException ex){
                        System.out.println("Improper in reading io");
                        System.exit(1);
                }
		return reviewerRating;
	}

	/*****************************************************************************
        Function:computePearsons
        Arguments: 2D ArrayList of Integers,ArrayList of Strings,ArrayList of Integers
        Return Type: Void
        Description: Calculates the pearson value R
        *****************************************************************************/

	
	public void computePearsons(ArrayList<ArrayList<Integer>> reviewerRating, ArrayList<String> movieNames,ArrayList<Integer> arrList,int choice)
	{
		ArrayList<Integer> comparisonMovie = new ArrayList<Integer>(reviewerRating.get(choice-1));	//Ratings for the movie to be compared
		//System.out.println(comparisonMovie);
		ArrayList<Double> pearsonR = new ArrayList<>();		//to store the r values
		for(int i = 0;i<movieNames.size();i++)
		{
			ArrayList<Integer> t1 = new ArrayList<>();
			ArrayList<Integer> t2 = new ArrayList<>();
			if(comparisonMovie.size()<10)
				break;
			ArrayList<Integer> targetMovie = new ArrayList<Integer>(reviewerRating.get(i));
			for(int j=0;((j<comparisonMovie.size())&&(j<targetMovie.size()));j++)
			{
				int rating1,rating2;
				rating1 = comparisonMovie.get(j);
				rating2 = targetMovie.get(j);
				if((rating1!=0) && (rating2!=0))
				{
					t1.add(rating1);
					t2.add(rating2);
				}
			}
			if(t1.size()<10)			//if the reviewers are less than 10,make r value as -2 and continue
			{	
				pearsonR.add(-2.0);		
				continue;
			}
			//calculating the average and std deviation
			double avg1 = averageRating(t1);
			double avg2 = averageRating(t2);
			double stdDev1 = stdDeviation(t1,avg1);
			double stdDev2 = stdDeviation(t2,avg2);
			double sumProduct = 0;
			double rValue =0;
			if((stdDev1<=0) || (stdDev2<=0) || ((t1.size()-1)<=0))
			{
				rValue = -2.0;		
			
			}
			else
			{
				for(int k=0;((k<t1.size()) && (k<t2.size()));k++)
				{
					
					sumProduct += ((t1.get(k))-avg1)*((t2.get(k))-avg2);		//sum of products 
					rValue = (sumProduct/((t1.size()-1)*stdDev1*stdDev2));	//dividing the sum by 1 less than number of reviewersand adding to the r ArrayList
				}
			}	
			pearsonR.add(rValue);			//adding the r value to the arrayList
		}
		//System.out.println(pearsonR.size());
		//System.out.println(movieNames.size());
		displayMovies(pearsonR,movieNames,arrList);	//call the display function
	}
	
	/*****************************************************************************
        Function:averageRating
        Arguments: ArrayList of Integers
        Return Type: Double
        Description: Calculates the average 
        *****************************************************************************/

	public double averageRating(ArrayList<Integer> arrList)
	{
		double sum = 0;	
		for(int i=0;i<arrList.size();i++)
		{
			sum = sum + arrList.get(i);
			
		}
		return sum/(arrList.size());
	}
	
	/*****************************************************************************
        Function:stdDeviation
	Arguments: ArrayList of Integers and a double
	Return Type: Double
	Description: Calculates the standard deviation
	*****************************************************************************/

	public double stdDeviation(ArrayList<Integer> arrList, double avg)
	{
		double sumDifference = 0;
		double stdDev = 0;
		for(int i=0;i<arrList.size();i++)
		{
			sumDifference = sumDifference + ((arrList.get(i)-(avg)) * (arrList.get(i)-(avg)));
				
		}
		stdDev = Math.sqrt(sumDifference/(arrList.size()-1));
		return stdDev;
	}

	/*****************************************************************************
	Function: displayMovies()
	Arguments: ArrayList of doubles, ArrayList of Strings, ArrayList of Integers
	Return Type: Void
	Description: This method displays the top 20 similar movies
	*******************************************************************************/
	
	public void displayMovies(ArrayList<Double> pearsonR, ArrayList<String> movieNames, ArrayList<Integer> arrList)
	{
		ArrayList<Double> rSorted = new ArrayList<>(pearsonR);
		
		int count1 = 0;
		double r1,r2;
		int count2 = 0;
		String movieName;
		Collections.sort(rSorted,Collections.reverseOrder());		//sorting in descending order
		//System.out.println(rSorted.size());
		System.out.printf("%5s %5s %5s %10s\n","    ","R","No.","Name"); 	//printing the header
		while(count1 < 20)
		{
			for(int i=0;i<rSorted.size();i++)
			{
				r1 = rSorted.get(i);
				for(int j=0;j<rSorted.size();j++)
					if(rSorted.get(j)!=(-2)) 	//ignore the ones with a value of -2
						count2++;	
				if(count2<20)
				{
					System.out.println("Insufficient comparison movies");
					break;
				}
				
				for(int k = 0;((k<pearsonR.size()) && (count1<20));k++)
				{
					r2 = pearsonR.get(k);
					if(r1==r2)	
					{
						count1++;
						movieName = movieNames.get(k);
						
						System.out.printf("%-5d %5f %5d %10s\n",count1,r1,arrList.get(k),movieName); 
					}
				}
			}
		}
	}
	
}

	
