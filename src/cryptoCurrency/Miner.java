package cryptoCurrency;

public class Miner
{
	public static boolean mineBlock(int difficulty, Block b)
	{
		String target = new String(new char[difficulty]).replace('\0', '0');
		String hash = null;
		long nonce = 0;
//		while(!hash.substring( 0, difficulty).equals(target))
		while(!hash.startsWith(target))
		{
			nonce++;
			hash = calculateHash(b.getPrevHash(), b.getDateTime(), nonce, b.getMerkleRoot());
		}
		b.setNonce(nonce);
		return true;
	}
	
	
	public static String calculateHash(String prevHash, String dateTime, long nonce, String merkleRoot)
	{
		String calculatedhash = Util.applySHA256(prevHash + dateTime + Long.toString(nonce) + merkleRoot);
		return calculatedhash;
	}
}
