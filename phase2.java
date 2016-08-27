import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
/**
 * Title : Phase 2
 * Description: This phase has two arguments sent in the command prompt as input.txt being the first and output.txt
 * being the second. The input file can be modified to have many URL as possible or image files.
 * The output.txt will store the information of each file and tell you the location of each URL that it saved from input 
 * in the same directory along with its name. If the file is an image file then it will grab the image and save it in the
 * same directory.
 * 
 * @author Shehar Yar CS370 
 *
 */



public class phase2 {
	URL url = null;
	File outputImageFile = null;
	public static BufferedImage image = null;

	private static String webpage = null;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";

    
    /**
     * ****GetURlInputStream function used from CS 370 Software Engineering
     * Name :getURLInputStream
     * Description: This function opens the connection of a URL and reads the HTMl code
     * @param sURL
     * @return the input stream which reads from the URL webpage.
     * @throws Exception
     */
    public static InputStream getURLInputStream(String sURL) throws Exception {
    URLConnection oConnection = (new URL(sURL)).openConnection();
    oConnection.setRequestProperty("User-Agent", USER_AGENT);
    return oConnection.getInputStream();
    }
    
    /**
     * *****function used from CS 370 Software Engineering
     * Name: read
     * Description: Reads the URL content and buffers from each line of the web
     * oracle definition: "Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines."
     * @param url
     * @return  bufferedreader object from the content it read from
     * @throws Exception
     */
    public static BufferedReader read(String url) throws Exception {
        //InputStream content = (InputStream)uc.getInputStream();
//    BufferedReader in = new BufferedReader (new InputStreamReader
//(content));
        InputStream content = (InputStream)getURLInputStream(url);
        return new BufferedReader (new InputStreamReader(content));
    } // read

    /**
     * *****function used from CS 370 Software Engineering
     * Name: read2
     * Description: reads URL string from paramater and return new BufferedStream from URL open stream.
     * @param url
     * @return  bufferedreader object from the content it read from
     * @throws Exception
     */
    public static BufferedReader read2(String url) throws Exception {
            return new BufferedReader(
                    new InputStreamReader(
                            new URL(url).openStream()));
    } // read

    /**
     * Name: saveImage
     * Description: grabs image URl then specify file name in parameter.
     * grabs the image and saves it to same directory program stored on.
     * @param imageUrl
     * @param destinationFile
     * @throws IOException
     */
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	/**
	 * *****function used from CS 370 Software Engineering
	 * Name : printURLinfo
	 * Description: grabs the URL and it gets its information
	 * @param uc
	 * @return This returns the content type, length, last modified, expiration, content encoding status.
	 * @throws IOException
	 */
	public static String printURLinfo(URLConnection uc) throws IOException {
        // Display the URL address, and information about it.
		String urlContent = " ";
        System.out.println(uc.getURL().toExternalForm() + ":");
        System.out.println("  Content Type: " + uc.getContentType());
        System.out.println("  Content Length: " + uc.getContentLength());
        System.out.println("  Last Modified: " + new Date(uc.getLastModified()));
        System.out.println("  Expiration: " + uc.getExpiration());
        System.out.println("  Content Encoding: " + uc.getContentEncoding());
        
        urlContent += uc.getURL().toExternalForm() + ":\n" + "  Content Type: " 
        + uc.getContentType() + "\n" + "  Content Length: " + uc.getContentLength() 
        + "\n" + "  Last Modified: " + new Date(uc.getLastModified()) + "\n" 
        + "  Expiration: " + uc.getExpiration() + "\n" + "  Content Encoding: " + uc.getContentEncoding();
        return urlContent;
    } // printURLinfo
    
    // Create a URL from the specified address, open a connection to it,
    // and then display information about the URL.
    public static void main(String[] args) throws MalformedURLException, IOException
    {
    	// i and j are for multiple file names 
    	int i=0;
    	int j=0;
    	// lines read from URL
    	int numberOfLines =0;
    	
    	//file name string
    	String fileName = "file";
    	
    	//File type send prom the command argument
    	File textFile = new File(args[0]);
		@SuppressWarnings("resource")
		Scanner fileScan = new Scanner(textFile);
		//int loop = fileScan.nextInt();
		//file name specified.
		String filename = "./output.txt";
		String urldata;
		
		
		try{

		    //Create object of FileReader
		    FileReader inputFile = new FileReader(args[0]);

		    //Instantiate the BufferedReader Class
		    BufferedReader bufferReader = new BufferedReader(inputFile);

		    //Variable to hold the one line data
		    String line;

		    // Read file line by line and print on the console
		    while ((line = bufferReader.readLine()) != null)   {
		    	
		    	// if the file is a  jpg file then it saves the image.
		    	if(line.substring(line.length()-3, line.length()).equals("jpg"))
		    	{
		    		//file name increment so no overwritten file
		    		j++;
		    		String imageUrl = line;
		    		String destinationFile = "Image"+j+ ".jpg";
		    		// image file bing saved by saveImgae function.
		    		saveImage(imageUrl, destinationFile);
		    		
		    		//grabs URL and opens its connection.
		    		URL url = new URL(line);
					URLConnection connection = url.openConnection();
					//prints URL information
					urldata = printURLinfo(connection);
					WriteFile data = new WriteFile(filename, true);
					data.writeToFile(urldata);
					//lets user know that the file is viewable.
					data.writeToFile("*******This is viewable as image File saved under *********" + "image" + j + ".jpg");
		    	}
		    	//if its a HTML file Then grabs infromation and returns its output in a output text.
		    	else
		    	{
			    	i++;
			    	PrintWriter writer = new PrintWriter(fileName + i + ".html", "UTF-8");
			    	URL url = new URL(line);
					URLConnection connection = url.openConnection();
					urldata = printURLinfo(connection);
					WriteFile data = new WriteFile(filename, true);
					data.writeToFile(urldata);
					
					BufferedReader reader = read(line);
					String line1 = reader.readLine();
	
					while (line1 != null) {
						line1 = reader.readLine();
						writer.println(line1);
						numberOfLines++;
					} // while
					writer.close();
					data.writeToFile("Number of lines Read: " + numberOfLines  + " and This file is saved under: " + fileName + i + ".txt");
					
		    	}
		    	numberOfLines=0;	
		    }
		    //Close the buffer reader
		    bufferReader.close();
		    }catch(Exception e){
		            System.out.println("Error while reading file line by line:" 
		            + e.getMessage());                      
		    }
	System.out.println("Check main directory"); 	
    } // main

 

}
