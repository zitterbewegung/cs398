import java.rmi.*;
import java.math.BigInteger;

public interface Fibonacci extends Remote{
	public BigInteger getFibonacci(int n) throws RemoteException;
	public BigInteger getFibonacci(BigInteger n) throws RemoteException;
}
