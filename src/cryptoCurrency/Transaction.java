package cryptoCurrency;

// Main will send an input, sender, recipient, value
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Transaction 
{
	private String txnHash;
	private ArrayList<Input> inputs;
	private ArrayList<Output> outputs;
	private String txnTime;
	
	private PublicKey sender, recipient;
	private float txnValue;
	private byte[] signature;
	
	
	public Transaction(ArrayList<Input> inputs, PublicKey sender, PublicKey recipient, float value)
	{
		this.txnValue = value;
		this.sender = sender;
		this.recipient = recipient;
		this.inputs = inputs;
	}
	
	
	public boolean addUtxoToInputs()
	{
		Set<UTXO> uniqueUTXO = new HashSet<>();
		for(Input i : this.inputs)
		{
			UTXO u = UTXOPool.getUtxo(i.getPrevTxnHash());
			if(u.getValue() <= 0)
			{
				System.out.println("Wrong UTXO...");
				return false;
			}
			
			if(u.equals(null)) 
			{
				System.out.println("Input doesn't exist in UTXO pool...");
				return false;
			}
			
			if(uniqueUTXO.contains(u))
			{
				System.out.println("Same UTXO called multiple times...");
				return false;
			}
			uniqueUTXO.add(u);
			i.setUtxo(u);
		}
		return true;
	}
	
	
	public Output generateOutput(float value, PublicKey pbKey)
	{
		Output o = new Output(value, pbKey, this.txnHash);
		return o;
	}
	
	
	
	public boolean processTransaction()
	{
		// verify signature
		if(!verifySignature()) 
		{
			System.out.println("Signature verification failed...");
			return false;
		}
		
		
		// gather txn inputs
		boolean success = addUtxoToInputs();
		if(!success) return false;
		
		
		// check if transaction is valid
		if(calculateInputValue() < this.txnValue)
		{
			System.out.println("Not enough input value to pay " + this.txnValue);
			return false;
		}
		
		
		// generate outputs
		this.txnHash = calculateHash();
		Output toRecipient = generateOutput(this.txnValue, this.recipient);
		Output toSender = generateOutput((calculateInputValue() - this.txnValue), this.sender);
		
		outputs.add(toRecipient);
		outputs.add(toSender);
		
		updateUtxoPool(); // add new UTXOs and remove spent UTXOs.
		
		return true;
	}
	
	
	public void generateSignature(PrivateKey privateKey) 
	{
		LocalDateTime dateTime = LocalDateTime.now();
		this.txnTime = dateTime.toString();
		String data = Util.toString(this.sender) + Util.toString(this.recipient) + Float.toString(this.txnValue) + this.txnTime;
		byte[] temp = null;
		try 
		{
			temp = Util.signTxn(privateKey, data);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		this.signature = temp;
	}
	
	
	public boolean verifySignature()
	{
		String data = Util.toString(this.sender) + Util.toString(this.recipient) + Float.toString(this.txnValue) + this.txnTime;
		return Util.verifySignature(this.sender, data, this.signature);
	}

	
	
	public float calculateInputValue()
	{
		float total = 0;
		for(Input i : this.inputs)
		{
			UTXO u = i.getUtxo();
			total += u.getValue();
		}
		return total;
	}
	
	
	private String calculateHash()
	{
		return Util.applySHA256(Util.toString(sender) + Util.toString(recipient) + Float.toString(txnValue) + this.txnTime);
	}
	
	
	private void updateUtxoPool()
	{
		for(Output o : outputs)
		{
			UTXO u = new UTXO(o.getValue(), o.getReceiver(), this.signature, o.getParentHash());
			UTXOPool.addUtxo(u);
		}
		
		
		for(Input i : inputs)
		{
			UTXO u = i.getUtxo();
			UTXOPool.removeUtxo(u);
		}
	}
}

