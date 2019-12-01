/*
 * 
 * Class contains insertion, deletion,find value
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {
	String path = "D:/eclipse/abc.txt" ;
	FileReader fileReader = null;
	FileWriter fileWriter = null;
	Writer writter = null;
	BufferedReader bufferReader = null; 
	BufferedWriter bufferWriter = null;
	FileWriter fileWrriter = null;
	/*
	 * It will update the file beased on the Time to live ..
	 * Deletes the records if its exits the time to Live
	 */
	synchronized void updateThefile() throws Exception{    
		try{    	
			File afile = new File(path);
			File bfile = new File("D:/eclipse/abcd.txt");
			fileReader = new FileReader(afile);
			fileWrriter = new FileWriter(bfile);
			bufferReader = new BufferedReader(fileReader);
			bufferWriter = new BufferedWriter(fileWrriter);
			String temp;
			while((temp = bufferReader.readLine())!= null){
				String tempTrimLine = temp.trim();
				if(tempTrimLine.contains("time #")){
					int index = temp.indexOf("#");				
					String dated= temp.substring(index,temp.length());
					String aed=dated.replace("IST","");
					Date sdate = new SimpleDateFormat("MMM dd HH:mm:ss yyyy").parse(aed);
					Date date= new Date();
					if(!sdate.before(date)){
						bufferWriter.write(System.lineSeparator()+temp); 
					}
				}
				else{
					bufferWriter.write(System.lineSeparator()+temp);
				}
			}
			bufferWriter.close();
			bufferReader.close();
			afile.delete();
			bfile.renameTo(afile);
		}
		catch(Exception e){
			e.printStackTrace();
			throw(e);
		}   

	}
	/*
	 * Insert the information key and value details into text file
	 */
	synchronized  void insertDetails(String key,String value,int timetolive) throws IOException, CustomException{
		try{
		if(key.length() > 32){
			throw new CustomException("Enter the key with maximum 32 characters");
		}
		fileWriter= new FileWriter(path,true);
		writter = new BufferedWriter(fileWriter);
		if(!searchKey(key)){
			Calendar cal = Calendar.getInstance();
			if(timetolive > 0){
				cal.add(Calendar.SECOND,timetolive);
				writter.append(System.lineSeparator()+key+"="+ ""+value+"time #"+cal.getTime());
			}
			else{
				writter.append(key+"="+ ""+value+System.lineSeparator());
			}
		}
		System.out.println("****The key is added successfully ****");
		writter.close();    	
		}

		catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
		}

	/*
	 * To search existing key is present or not
	 */    
	synchronized  boolean searchKey(String key) throws IOException, CustomException{
		try{
			fileReader = new FileReader(path);
			bufferReader = new BufferedReader(fileReader);
			String temp;
			int index;
			while((temp = bufferReader.readLine()) != null){
				index=temp.indexOf("=");
				if(index>0){
					if((temp.substring(0,index)).equals(key)){
						throw new CustomException("Key already exist!! "); 			
					}    		
				}
			}
		}

		catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
		return false;
	}

	/*
	 * Read the value based on the key given as input
	 */
	String readValue(String key) throws IOException{
		try{
			fileReader = new FileReader(path);
			bufferReader = new BufferedReader(fileReader);
			String temp;
			int index;
			int size;
			String value;
			while((temp = bufferReader.readLine()) != null){
				index=temp.indexOf("=");
				if(index >0 ){
					if((temp.substring(0,index)).equals(key)){  
						size = temp.length();
						value = temp.substring(index,size);
						System.out.println("The Value for the key is "+value);  
						return value;
					}   
				}
			} }

		catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
		return "The key not found";
	}

	/*
	 * Delete opertaion
	 */
	synchronized void deleteKey(String key) throws IOException{		
		try{
			File a = new File(path);
			File b = new File("D:/eclipse/abcd.txt");
			fileReader = new FileReader(a);
			FileWriter fw = new FileWriter(b);
			bufferReader = new BufferedReader(fileReader);
			BufferedWriter bw = new BufferedWriter(fw);
			String temp;
			while((temp = bufferReader.readLine())!= null){
				String tempTrimLine = temp.trim();
				if(!tempTrimLine.contains(key))
					bw.write(System.lineSeparator()+temp); 
			}
			bw.close();
			bufferReader.close();
			a.delete();
			b.renameTo(a);
			System.out.println("The key is deleted succesfully in file");
		}
		catch(Exception e){
			e.printStackTrace();
			throw(e);
		}
	}

	/*
	 * Main method
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub		

		Main mainObj = new Main();
		File av = new File(mainObj.path);
		av.createNewFile();
		int  n=3;
		System.out.println("Choose the Operation \n 1. Insert \n 2. Delete \n 3. Read ");
		Scanner scan = new Scanner(System.in);
		n = scan.nextInt();
		System.out.println("Do you want to choose the path Y or N");
		String choose= scan.next();
		if(choose.equals("Y")){
			System.out.println("Enter the path location - eg: D:\\eclipse\\filename.txt");
			mainObj.path= scan.next();
		}
		mainObj.updateThefile();
		/*
		 * if the fie size exceeds the 16 GB it will Exception
		 */
		if(av.length() >1079741824){
			throw new CustomException("Files exceeds the limit 16 GB please choose other file");
		}
		else {
			System.out.println("File size does not exceeeds the 16 gb");
		}	    	
		switch (n){
		case 1:
			System.out.println("*******Insert Operation");
			System.out.println("Enter the key to insert");
			String tempKey = scan.next();
			System.out.println("Enter the  value to insert");
			String tempValue = scan.next();
			System.out.println("Do you wish to add the Time to Live Y or N");
			String opinion = scan.next();
			int timetolive = 0;
			if(opinion.equals("Y")){
				System.out.println("Enter sec the Time to Live ");
				timetolive = scan.nextInt();
			}
			mainObj.insertDetails(tempKey,tempValue,timetolive);			
			break;						
		case 2:
			System.out.println("Delete operation");
			System.out.println("Enter key to delete ");
			String tempDeleteKey = scan.next();					
			mainObj.deleteKey(tempDeleteKey);		
			break;			
		case 3:
			System.out.println("Read operation");
			System.out.println("Enter the key to find the value ");
			String tempKey1 = scan.next();			
			mainObj.readValue(tempKey1);			
			break;						 						
		}		
	}
}