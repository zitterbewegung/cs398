/*
 *
 * Page 622-623 Java Network Programming
 */
import java.rmi.*;
import java.net.*;
import java.math.BigInteger;
public class FibonacciClient{
	public static void main(String args[])
	{
		if(args.length == 0 || !args[0].startsWith("rmi:")){
			System.err.println( 
			"Usage: java FibonacciCline rmi://host.domain:port/fibonacci number");
		}
		try{
			Object o = Naming.lookup(args[0]);
			Fibonacci calculator = (Fibonacci) o;
			for(int i = 1; i< args.length; i++){
				try{
					BigInteger index = new BigInteger(args[i]);
					BigInteger f = calculator.getFibonacci(index);
					System.out.println("The " + args[i] + "the Fibonacci numbers is " + f);
					
				} catch(NumberFormatException e){
					System.err.println(args[i] + "is not an integer");
				}
			}
		} catch(MalformedURLException ex){
			System.err.println(args[0] + "is not a valid URL" );
		} catch(RemoteException ex){
			System.err.println("Remote object threw exception" + args[0]);
		} catch (NotBoundException ex){
			System.err.println("" + args[0] );
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
