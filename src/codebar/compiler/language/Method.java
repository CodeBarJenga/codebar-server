package codebar.compiler.language;

public abstract class Method
{
	public boolean timelimit = false;
	public abstract void compile();
	public abstract void execute();
}
