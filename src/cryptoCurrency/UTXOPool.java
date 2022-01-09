package cryptoCurrency;

import java.util.ArrayList;

public class UTXOPool
{
	private static ArrayList<UTXO> utxoPool;
	
	
	public static void addUtxo(UTXO utxo)
	{
		UTXOPool.utxoPool.add(utxo);
	}
	
	
	public static void removeUtxo(UTXO utxo)
	{
		UTXOPool.utxoPool.remove(utxo);
	}
	
	
	public static UTXO getUtxo(String prevTxnHash)
	{
		for(UTXO u : utxoPool)
		{
			if(prevTxnHash.equals(u.getParentTxnHash()))
			{
				return u;
			}
		}
		return null;
	}
}
