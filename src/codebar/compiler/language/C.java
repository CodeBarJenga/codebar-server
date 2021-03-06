package codebar.compiler.language;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import codebar.compiler.*;
public class C extends Method
{
	String filename,solution,dir;
	int timelimit;
	public C(String filename,String solution,int timelimit,String dir)
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
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir+"/"+filename)));
		bw.write(solution);//created a stream now writing contents on it
		bw.close();
		/*
		 * created compile.sh file so that we can write linux command in it and we can run this file by creating java runtime
		 * environment
		 * we can write into any file by just creating stream
		 * write function writes array of characters; 
		 * 
		 */
		bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir+"/compile.sh")));
		bw.write("cd \"" + dir +"\"\n");
		bw.write("gcc -lm " + filename + " 2>err.txt");//may be -lm is used for conio header file
		bw.close();
		/*
		 * 2>err.txt means if any error is occurred it will be redirected to err.txt.it will be used to check compile time and
		 * runtime error
		 */
		Runtime r = Runtime.getRuntime();//java runtime environment
		Process p = r.exec("chmod +x " + dir + "/compile.sh");//must have execute permission thts why +x
		/*
		 * method is used to run system command and it also create sandbox type of environment
		 */
		p.waitFor();//this method causes this process to stop until process p is terminated
		p = r.exec(dir + "/compile.sh"); // execute the compiler script
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
		try{
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir+"/run.sh")));
			bw.write("cd \""+dir+"\"\n");
			bw.write("chroot .\n");// to change root directory used for setting up a test environment
			/*
			 * a process that is run in such a modified environment cannot access files outside the root directory
			 */
			bw.write("timeout"+" "+(1.09)+" ./a.out <input.txt>output.txt");
			bw.close();	
			Runtime r=Runtime.getRuntime();
			Process p=r.exec("chmod +x "+dir+ "/run.sh");
			p.waitFor();
			p = r.exec(dir + "/run.sh"); // execute the compiler script
			Timelimit shell = new Timelimit(this, p, this.timelimit*1000);
			shell.start();
			p.waitFor();
		}	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
