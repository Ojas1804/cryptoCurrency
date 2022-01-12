package cryptoCurrency;

public class Miner extends User
{
	Miner()
	{
		super();
	}
	
	
	public boolean mineBlock(int difficulty, Block b)
	{
		// reward transaction given to miner
		Transaction coinBase = BlockChain.generateCoinbase(this);
		b.getTransactions().add(coinBase);
		b.setMerkleRoot(Util.calculateMerkleRoot(b.getTransactions()));
		
		String target = new String(new char[difficulty]).replace('\0', '0');
		String hash = null;
		long nonce = 0;
		
//		while(!hash.substring( 0, difficulty).equals(target))
		// need to solve this problem to receive reward
		while(!hash.startsWith(target))
		{
			nonce++;
			hash = calculateHash(b.getPrevHash(), b.getDateTime(), nonce, b.getMerkleRoot());
		}
		b.setNonce(nonce);
		b.setHash(hash);
		
		// adding reward coin to wallet
		this.receiveCoin(coinBase.getOutputs(), coinBase.getSignature());
		return true;
	}
	
	
	public static String calculateHash(String prevHash, String dateTime, long nonce, String merkleRoot)
	{
		String calculatedhash = Util.applySHA256(prevHash + dateTime + Long.toString(nonce) + merkleRoot);
		return calculatedhash;
	}
}
