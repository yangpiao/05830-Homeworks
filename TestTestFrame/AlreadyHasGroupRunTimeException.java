
public class AlreadyHasGroupRunTimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AlreadyHasGroupRunTimeException (String message) 
	{
		super(message);
	}
	public AlreadyHasGroupRunTimeException () 
	{
		super();
	}
}
