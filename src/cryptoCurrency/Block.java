package cryptoCurrency;

import java.util.ArrayList;

public class Block
{
	private long nonce;
	private String hash;
	private String prevHash;
	private ArrayList<Transaction> transactions;
	private String dateTime;
	private String merkleRoot;
	
	
	public Block(String prevHash)
	{
		this.prevHash = prevHash;
		this.dateTime = Util.getTime();
		this.merkleRoot = Util.calculateMerkleRoot(transactions);
	}
	
	
	public String calculateHash()
	{
		String calculatedhash = Util.applySHA256(prevHash + dateTime + Long.toString(nonce) + merkleRoot);
		return calculatedhash;
	}


	public long getNonce()
	{
		return nonce;
	}


	public void setNonce(long nonce)
	{
		this.nonce = nonce;
	}


	public String getPrevHash()
	{
		return prevHash;
	}


	public void setPrevHash(String prevHash)
	{
		this.prevHash = prevHash;
	}



	public void setTransactions(ArrayList<Transaction> transactions)
	{
		this.transactions = transactions;
	}


	public String getDateTime()
	{
		return dateTime;
	}


	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}


	public String getMerkleRoot()
	{
		return merkleRoot;
	}


	public void setMerkleRoot(String merkleRoot)
	{
		this.merkleRoot = merkleRoot;
	}
	
	
	public String getHash()
	{
		return this.hash;
	}
}
