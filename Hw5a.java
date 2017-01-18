
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

public class Hw5a
{
	/***************************************************************************
	Function: processMovieName
	Arguments: ArrayList of Strings, ArrayList of Integers
	Return Type: Void
	Description: Reads the movie-names file and write formatted data to a new file
	****************************************************************************/

	public void processMovieName(ArrayList<String> moviesArr,ArrayList<Integer> movieNum) throws IOException
	{	
		ArrayList<String> itemArr = new ArrayList<>();
		BufferedReader inputFile = null;
		//reading the file movie-names.txt
		try{
			FileInputStream fis = new FileInputStream(new File("/home/turing/t90rkf1/d470/dhw/hw5-movies/movie-names.txt"));
			InputStreamReader ur = new InputStreamReader(fis, "UTF-8");
			inputFile = new BufferedReader(ur);
			
		}
		//catching IOException
		catch(IOException elementException)
		{
			System.out.println("I/O error opening file. Terminating.");
			System.exit(1);
		}
		
		try{
			String line;
			while ((line = inputFile.readLine())!=null)
			//while(inputFile.hasNext())
			{
				int delim = line.indexOf("|");
				int item = Integer.parseInt(line.substring(0,delim));	//separating the number and movie name 
     				String movie = line.substring(delim+1).trim();
				//System.out.println(movie);
				movie = Normalizer.normalize(movie, Normalizer.Form.NFD);	
    				movie = movie.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

				String formattedNumber = String.format("%05d", item);		//padding the number with 0's
				movieNum.add(item);
     				itemArr.add(formattedNumber);
     				moviesArr.add(movie);
				//System.out.println();
			}
			//System.out.println(itemArr);
			//System.out.println(moviesArr);
			File outputFile = new File("movie-names2.txt");				//writing to a new file
			FileOutputStream fos = new FileOutputStream(outputFile); 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (int i = 0; i < itemArr.size(); i++) {
			bw.write(itemArr.get(i));
			bw.write(moviesArr.get(i));
			bw.newLine();
			}
		bw.close();
		}
		catch (NoSuchElementException elementException)
      		{
        		System.out.println("File improperly formed. Terminating.");
        		System.exit(1);
      		}
	}
	/*************************************************************************************
	Function: processMovieRating
	Arguments: None
	Return Type: void
	Description: Reads the movie-matrix and stores in a serializable file
	*************************************************************************************/
	
	public void processMovieRating()
	{
		ArrayList<ArrayList<Integer>> ReviewerRating = new ArrayList<ArrayList<Integer>>();;
		ArrayList<Integer> rating= new ArrayList<Integer>();		
		Scanner inputFile=null;
		//Reading the file movie-matrix.txt
                try{
                        inputFile = new Scanner(Paths.get("/home/turing/t90rkf1/d470/dhw/hw5-movies/movie-matrix.txt"));
                }
		
		//catching IoException
                catch(IOException elementException)
                {
                        System.out.println("I/O error opening file. Terminating.");
                        System.exit(1);
                }
		try{
                        while(inputFile.hasNext())
                        {
				rating = new ArrayList<>();
                                String line = inputFile.nextLine();     //Reading each line
				String[] tokens = line.split(";");		//splitting the data based on delimitor ;
				for(int i=0;i<tokens.length;i++)
				{
					int j = 0;
					if(tokens[i].isEmpty())
					j=0;
					else
					j = Integer.parseInt(tokens[i]);
					rating.add(j);
				}
				ReviewerRating.add(rating);		//addng the ratings to an ArrayList
			}

		}
		catch (NoSuchElementException elementException)
                {
                        System.out.println("File improperly formed. Terminating.");
                        System.exit(1);
               }
		
		try{
      			//use buffering to serialize object
			
			FileOutputStream file = new FileOutputStream("movie-matrix.ser");
                        OutputStream buffer = new BufferedOutputStream(file);
                        ObjectOutput output = new ObjectOutputStream(buffer);
                        try{
		
                                output.writeObject(ReviewerRating);		//writing the arrayList object
                        }
                        finally{
                                output.close();
                        }
    		}  
    		catch(IOException ex){
			System.out.println("IO exception in file. Terminating");
			System.exit(1);
    		}
		catch (NoSuchElementException elementException)
                {
                        System.out.println("File improperly formed. Terminating.");
                        System.exit(1);
                }

		
	}			
	
 		

}	
