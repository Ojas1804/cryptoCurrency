package cryptoCurrency;

import java.util.ArrayList;

public class BlockChain
{
	private static ArrayList<Block> blockchain;
	private static final int difficulty = 2;
	public static final float coinBaseValue = 25;
	
	
	// Adding the first block of the blockchain
	public static void genesisBlock(User recipient, Miner m1)
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
		m1.mineBlock(getDifficulty(), genesis);
		blockchain.add(genesis);
		
		System.out.println("First block added...");
	}
	
	
	public static String getPrevBlockHash()
	{
		Block b = blockchain.get(blockchain.size() - 1);
		return b.getHash();
	}


	// difficulty of problem that miner has to solve to get add new 
	// block to the blockchain.
	public static int getDifficulty()
	{
		return difficulty;
	}
	
	
	// adding new block in the blockchain
	public static boolean addBlock(Block b)
	{
		blockchain.add(b);
		return true;
	}
	
	
	// generate coinbase transaction: reward transaction given to miner
	public static Transaction generateCoinbase(Miner m1)
	{
		Transaction coinbase = null;
		coinbase = new Transaction(null, null, m1.getPublicKey(), BlockChain.coinBaseValue);
		coinbase.processTransaction();
		return coinbase;
	}
}
