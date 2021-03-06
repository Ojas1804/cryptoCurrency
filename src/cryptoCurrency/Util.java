package cryptoCurrency;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;

public class Util 
{
	public static byte[] signTxn(PrivateKey privateKey, String txnData)
	{
		Signature ecdsa;
		byte[] signedTxn;
		
		try
		{
			ecdsa = Signature.getInstance("ECDSA", "BC");
			ecdsa.initSign(privateKey);
			byte[] strByte = txnData.getBytes();
			ecdsa.update(strByte);
			byte[] realSig = ecdsa.sign();
			signedTxn = realSig;
		}
		
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		return signedTxn;
	}
	
	
	public static boolean verifySignature(PublicKey publicKey, String txnData, byte[] signature)
	{
		try
		{
			Signature ecdsa = Signature.getInstance("ECDSA", "BC");
			ecdsa.initVerify(publicKey);
			ecdsa.update(txnData.getBytes());
			return ecdsa.verify(signature);
		}
		
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	public static String toString(Key key) 
	{
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	
	public static String applySHA256(String input)
	{
		try 
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        
			//Applies sha256 to our input, 
			byte[] hash = md.digest(input.getBytes("UTF-8"));
	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexadecimal
			for (int i = 0; i < hash.length; i++) 
			{
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	public static String getTime()
	{
		LocalDateTime dateTime = LocalDateTime.now();
		String txnTime = dateTime.toString();
		return txnTime;
	}
	
	
	public static String calculateMerkleRoot(ArrayList<Transaction> transactions)
	{
		String root = null;
		ArrayList<String> txnHash = new ArrayList<>();
		for(Transaction t : transactions)
		{
			txnHash.add(t.getTxnHash());
		}
		
		while(txnHash.size() > 1)
		{
			txnHash.add(applySHA256(txnHash.get(0) + txnHash.get(1)));
			txnHash.remove(0);
			txnHash.remove(1);
		}
		
		root = txnHash.get(0);
		return root;
	}
}
