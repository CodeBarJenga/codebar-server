package codebar.compiler.language;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import codebar.compiler.*;
public class Python extends Method
{
	String filename,solution,dir;
	int timelimit;
	public Python(String filename,String solution,int timelimit,String dir)
	{
		this.filename=filename;
		this.solution=solution;
		this.timelimit=timelimit;
		this.dir=dir;
	}
	public void compile()
	{
	  try
	  {
		  System.out.println("python");
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir+"/"+filename)));
		bw.write(solution);//created a stream now writing contents on it
		bw.close();
		/*
		 * created run.sh file so that we can write linux command in it and we can run this file by creating java runtime
		 * environment
		 * we can write into any file by just creating stream
		 * write function writes array of characters; 
		 * 
		 */
		bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir+"/run.sh")));
		bw.write("cd \"" + dir +"\"\n");
		bw.write("chroot .\n");
		bw.write("python " + filename + " < input.txt > output.txt 2>err.txt");//may be -lm is used for conio header file
		bw.close();
		/*
		 * 2>err.txt means if any error is occurred it will be redirected to err.txt.it will be used to check compile time and
		 * runtime error
		 */
		Runtime r = Runtime.getRuntime();//java runtime environment
		Process p = r.exec("chmod +x " + dir + "/run.sh");//must have execute permission thts why +x
		/*
		 * method is used to run system command and it also create sandbox type of environment
		 */
		p.waitFor();//this method causes this process to stop until process p is terminated
		p = r.exec(dir + "/run.sh");
		Timelimit shell = new Timelimit(this, p, 3000);
		shell.start();// execute the compiler script
		p.waitFor();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void execute()
	{
		// as python is an interpreted language no need to exceute
	}
}