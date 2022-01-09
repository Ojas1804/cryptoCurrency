package cryptoCurrency;

import java.security.PublicKey;

public class Output
{
	private float value;
	private PublicKey receiver;
	private String parentHash;
	
	
	
	public Output(float value, PublicKey key, String parentHash)
	{
		this.value = value;
		this.receiver = key;
		this.setParentHash(parentHash);
	}


	public float getValue()
	{
		return value;
	}


	public void setValue(float value)
	{
		this.value = value;
	}


	public PublicKey getReceiver()
	{
		return receiver;
	}


	public void setReceiver(PublicKey receiver)
	{
		this.receiver = receiver;
	}


	public String getParentHash()
	{
		return this.parentHash;
	}


	public void setParentHash(String parentHash)
	{
		this.parentHash = parentHash;
	}
}