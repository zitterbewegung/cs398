import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.math.BigInteger;

public class FibonacciImpl extends UnicastRemoteObject implements Fibonacci {
	public FibonacciImpl() throws RemoteException{
		super();
	}
	public BigInteger getFibonacci(int n) throws RemoteException {
		return this.getFibonacci(new BigInteger(Long.toString(n)));
	}
	public BigInteger getFibonacci(BigInteger n) throws RemoteException{
		System.out.println("Calculating the " + n + "th Fibonacci number");
		BigInteger zero = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		if(n.equals(zero)) return one;
		if(n.equals(one)) return one;
		
		BigInteger i = one;
		BigInteger low = one;
		BigInteger high = one;
		while(i.compareTo(n) == -1){
			BigInteger temp = high;
			high = high.add(low);
			low = temp;
			i = i.add(one);
		}
		return high;
	}
}
