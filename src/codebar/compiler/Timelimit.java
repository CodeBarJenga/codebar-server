package codebar.compiler;
import codebar.compiler.language.*;
public class Timelimit extends Thread
{
	Method mt;
	Process p;
	int time;
	public Timelimit(Method mt,Process p,int time)
	{
		this.mt=mt;
		this.p=p;
		this.time=time;
	}
	public void run()
	{
		try{
			sleep(time);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			p.exitValue();//it will return value when subprocess is terminated 0 means normal termination
			mt.timelimit=false;
		}catch(IllegalThreadStateException e)
		{
			mt.timelimit=true;
			p.destroy();
		}
	}
}
