package codebar.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import codebar.compiler.language.*;

public class ClientThread extends Thread
{
	Socket s;
	File dir;
	int n;
	public ClientThread(Socket s,int n)
	{
		this.s=s;
		this.n=n;
		//staging directory: it act as a buffer until file is moved into target directory
		//System.out.println(dir.getAbsolutePath());
	}
	
	public void run()
	{
		//this will create parent directory also another method is dir.mkdir()
		try{
			BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter writer=new PrintWriter(s.getOutputStream(),true);
			String username=in.readLine();//username of user
			String probcode=in.readLine();//unique problem code
			dir=new File("/home/prateek/stage/"+username+"/"+probcode+"/"+n);//staging directory: it act as a buffer until file is moved into target directory
			System.out.println(dir.getAbsolutePath());
			dir.mkdirs();//this will create parent directory also another method is dir.mkdir()
			String filename=in.readLine();//solution filename with extension
			String lang=in.readLine();//programming language
			int timelimit=Integer.parseInt(in.readLine());//time limit of question
			String solution=in.readLine().replace("$_n_$","\n");//solution of client 
			String input=in.readLine().replace("$_n_$","\n");//standard input file 
			PrintWriter out=new PrintWriter(new FileOutputStream("/home/prateek/stage/"+username+"/"+probcode+"/"+n+"/input.txt"));
			out.println(input);//input is written into input file
			out.close();
			
			Method l=null;//abstract class reference is created
			if(lang.equals("c"))
				l=new C(filename,solution,timelimit,dir.getAbsolutePath());
			else if(lang.equals("cpp"))
				l=new Cpp(filename,solution,timelimit,dir.getAbsolutePath());
			else if(lang.equals("py"))
				l=new Python(filename,solution,timelimit,dir.getAbsolutePath());
			else if(lang.equals("java"))
				l=new Java(filename,solution,timelimit,dir.getAbsolutePath());
			l.compile();
			String errors=compile_time();
			if(!errors.equals(""))
			{
				writer.println("0");//status for compilation error
				writer.println(errors+"$");
				writer.println(dir.getAbsolutePath()+"/"+filename);
			}
			else
			{
				l.execute();
				if(l.timelimit)
				{
					System.out.println(l.timelimit);
					writer.println("1");//status for time limit exceeded
					writer.println(dir.getAbsolutePath()+"/"+filename);
				}
				else 
				{
					writer.println("2");//status for correctly execution
					writer.println(run_time());
					writer.println(dir.getAbsolutePath()+"/"+filename);
				}
			}
			//check may be reason for error
			writer.close();
			in.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String compile_time()
	{
		String read="";
		StringBuilder error=new StringBuilder();
		/*
		 * it is used because of append and insert method which is not available in String class
		 */ 
		 try{
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(dir.getAbsolutePath() + "/err.txt")));
   		 	while((read = br.readLine())!=null)
		 		error.append(read + "\n");//simple logic
   		 	br.close();
		 }catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return error.toString().trim();
	}
	
	public String run_time()
	{
		String read="";
		StringBuilder result=new StringBuilder();
		/*
		 * it is used because of append and insert method which is not available in String class
		 */ 
		 try{
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(dir.getAbsolutePath()+"/output.txt")));
			 while((read = br.readLine())!=null)
		 		result.append(read+"\n");
			 br.close();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 //System.out.println(result.toString().trim());
		return result.toString().trim();
	}
	
}
