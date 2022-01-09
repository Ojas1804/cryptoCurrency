package cryptoCurrency;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Scanner;


public class User 
{
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private String password;
	private ArrayList<UTXO> myUTXOs = new ArrayList<>();
	private float balance;
	
	
	public User()
	{
		generateKeyPair();
		password();
		this.balance = 0;
	}
	
	
	private void generateKeyPair() 
	{
		try 
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1"); // using ECC algorithm to
																			  // generate keys
			
			keyGen.initialize(ecSpec, random);
        	KeyPair keyPair = keyGen.generateKeyPair();
        	
        	this.privateKey = keyPair.getPrivate();
        	this.publicKey = keyPair.getPublic();
		}
		
		catch(Exception e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	
	// to access private key
	public void password()
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a new password : ");
		String temp = in.nextLine();
		System.out.println("Enter the password again : ");
		String temp2 = in.nextLine();
		
		if(temp.equals(temp2))
		{
			this.password = temp;
			System.out.println("Password created successfully!!");
		}
		
		else
		{
			System.out.println("Password doesn't match. Try Again!!");
			password();
		}
		in.close();
	}
	
	
	// verify password
	public boolean verifyPassword(String pswd)
	{
		if(this.password.equals(pswd))
		{
			return true;
		}
		
		return false;
	}
	
	
	// get public key
	public PublicKey getPublicKey()
	{
		return this.publicKey;
	}
	
	
	public PrivateKey getPrivateKey()
	{
		System.out.println("Enter the password : ");
		Scanner in = new Scanner(System.in);
		
		String temp = in.nextLine();
		in.close();
		
		boolean isPswd = verifyPassword(temp);
		if(isPswd)
		{
			return this.privateKey;
		}
		return null;
	}
	
	
//	private float calculateBalance(ArrayList<UTXO> myUTXOs)
//	{
//		float balance = 0;
//		for(UTXO u : myUTXOs)
//		{
//			balance += u.getValue();
//		}
//		return balance;
//	}
	
	
	public ArrayList<Input> newPayment(float value, PublicKey recipient)
	{
		float temp = 0;
		ArrayList<Input> inputs = new ArrayList<>();
		if(value > balance)
		{
			System.out.println("Not enough balance");
			return null;
		}
		
		else
		{
			for(UTXO u : myUTXOs)
			{
				temp += u.getValue();
				inputs.add(new Input(u.getParentTxnHash()));
				if(temp > value)
				{
					break;
				}
			}
		} // generate signature
		
		Transaction newTransaction = new Transaction(inputs, publicKey, recipient, value);
		newTransaction.generateSignature(privateKey);
		return inputs;
	} // still have to remove the UTXOs from myUTXOs
	
	
	
	public void receiveCoin(ArrayList<Output> outputs, byte[] signature)
	{
		for(Output o : outputs)
		{
			balance += o.getValue();
			myUTXOs.add(new UTXO(o.getValue(), o.getReceiver(), signature, o.getParentHash()));
		}
	}
}
