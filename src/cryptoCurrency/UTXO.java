package cryptoCurrency;

import java.security.PublicKey;

public class UTXO
{
	private float value;
	private PublicKey ownerPk;
	private byte[] signature;
	private String parentTxnHash;
	
	
	
	public UTXO(float value, PublicKey ownerPk, byte[] signature, String parentTxnHash)
	{
		super();
		this.value = value;
		this.ownerPk = ownerPk;
		this.signature = signature;
		this.setParentTxnHash(parentTxnHash);
	}



	public float getValue()
	{
		return value;
	}



	public void setValue(float value)
	{
		this.value = value;
	}



	public PublicKey getOwnerPk()
	{
		return ownerPk;
	}



	public void setOwnerPk(PublicKey ownerPk)
	{
		this.ownerPk = ownerPk;
	}



	public byte[] getSignature()
	{
		return signature;
	}



	public void setSignature(byte[] signature)
	{
		this.signature = signature;
	}



	public String getParentTxnHash()
	{
		return parentTxnHash;
	}



	public void setParentTxnHash(String parentTxnHash)
	{
		this.parentTxnHash = parentTxnHash;
	}
}
