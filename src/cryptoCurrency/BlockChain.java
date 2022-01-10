package cryptoCurrency;

import java.util.ArrayList;

public class BlockChain
{
	private static ArrayList<Block> blockchain;
	private static int difficulty = 2;
	
	
	public static void genesisBlock(User recipient)
	{
		User temp = new User("temp");
		Transaction genesisTransaction = new Transaction(null, temp.getPublicKey(), recipient.getPublicKey(), 100);
		genesisTransaction.generateSignature(temp.getPrivateKey());
		genesisTransaction.txnHash = "0";
		genesisTransaction.getOutputs().add(new Output(100, recipient.getPublicKey(), genesisTransaction.txnHash));
		
		UTXOPool.addUtxo(new UTXO(100, recipient.getPublicKey(), genesisTransaction.getSignature(), null));
		Block genesis = new Block(null);
		ArrayList<Transaction> t = new ArrayList<>();
		t.add(genesisTransaction);
		genesis.setTransactions(t);
		Miner.mineBlock(getDifficulty(), genesis);
		blockchain.add(genesis);
		
		System.out.println("First block added...");
	}
	
	
	public static String getPrevBlockHash()
	{
		Block b = blockchain.get(blockchain.size() - 1);
		return b.getHash();
	}


	public static int getDifficulty()
	{
		return difficulty;
	}
	
	
	public static boolean addBlock(Block b)
	{
		blockchain.add(b);
		return true;
	}
}
