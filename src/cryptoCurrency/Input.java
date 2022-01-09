package cryptoCurrency;

public class Input 
{
	private String prevTxnHash; // hash of the block of the txn. being referred to
	private UTXO utxo; // UTXO consumed
	
	public Input(String prevTxnHash)
	{
		this.prevTxnHash = prevTxnHash;
	}
	
	
	public String getPrevTxnHash()
	{
		return this.prevTxnHash;
	}

	
	public UTXO getUtxo()
	{
		return utxo;
	}


	public void setUtxo(UTXO utxo)
	{
		this.utxo = utxo;
	}
}
