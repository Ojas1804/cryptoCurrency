package cryptoCurrency;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
	public static void main(String[] args)
	{
		System.out.println("Creating new user data...");
		User u = new User();
		newUserInstructions(u);
		
		System.out.println("Registering new Miner...");
		Miner m1 = new Miner();
		
		BlockChain.genesisBlock(u, m1);
		Scanner input = new Scanner(System.in);
		
		ArrayList<User> users = new ArrayList<>();
		users.add(u);
		users.add(m1);
		
		boolean end = false;
		while(!end)
		{
			System.out.println("Options : ");
			System.out.println("1. CREATE NEW ACCOUNT");
			System.out.println("2. MAKE A TRANSACTION");
			System.out.println("3. CHECK BALANCE");
			System.out.println("4. EXIT APPLICATION");
			int choice1;
			System.out.print("Enter your choice : ");
			String temp = input.next();
			choice1 = Integer.valueOf(temp);
			newLine();
			
			switch(choice1)
			{
				case 1:
				{
					User newUser = new User();
					users.add(newUser);
					newUserInstructions(newUser);
					newLine();
					break;
				}
				
				case 2:
				{
					System.out.print("Enter your public key : "); // sender
					String key = input.nextLine();
					User temp1 = null;
					boolean found = false;
					for(User user : users) // checking if entered public key exists or not
					{
						PublicKey pbKey = user.getPublicKey();
						String pb = Util.toString(pbKey);
						if(pb.equals(key))
						{
							temp1 = user;
							found = true;
							break;
						}
					}
					if(!found)
					{
						System.out.println("Public Key not found...");
						break;
					}
					
					System.out.println("Enter transaction value : "); // amount being sent
					String strValue = input.nextLine();
					float value = Float.parseFloat(strValue);
					
					System.out.print("Enter the recipient's public key : "); // recipient
					String strRecipient = input.nextLine();
					
					// checking if recipient exists or not
					found = false;
					PublicKey recipient = null;
					User userRec = null;
					for(User user : users)
					{
						PublicKey pbKey = user.getPublicKey();
						String pb = Util.toString(pbKey);
						if(pb.equals(strRecipient))
						{
							recipient = user.getPublicKey();
							userRec = user;
							found = true;
							break;
						}
					}
					if(!found)
					{
						System.out.println("Public Key not found...");
						break;
					}
					
					// password to use private key
					System.out.print("Enter password : ");
					String pswd = input.nextLine();
					
					if(temp1.verifyPassword(pswd))
					{
						Transaction t = temp1.newPayment(value, recipient); // new transaction created
						boolean success = t.processTransaction(); // transaction processed
						if(!success) break;
						ArrayList<Transaction> tList = new ArrayList<>();
						tList.add(t);
						Block b = new Block(BlockChain.getPrevBlockHash()); // new block create
						b.setTransactions(tList);
						
						success = m1.mineBlock(BlockChain.getDifficulty(), b); // block sent to the miner
						
						if(BlockChain.addBlock(b)) System.out.println("Block added to the blockchain...");
						else System.out.println("Error occoured while adding the block");
						
						// adding UTXOs to recipient's wallet
						userRec.receiveCoin(t.getOutputs(), t.getSignature());
					}
					newLine();
					break;
				}
				
				
				case 3:
				{
					System.out.print("Enter your public key : ");
					String key = input.nextLine();
					User temp1 = null;
					boolean found = false;
					for(User user : users)
					{
						PublicKey pbKey = user.getPublicKey();
						String pb = Util.toString(pbKey);
						if(pb.equals(key))
						{
							temp1 = user;
							found = true;
							break;
						}
					}
					if(!found)
					{
						System.out.println("Public Key not found...");
						break;
					}
					
					System.out.print("Enter password : ");
					String pswd = input.nextLine();
					float balance = 0;
					
					if(temp1.verifyPassword(pswd))
					{
						balance = temp1.getBalance();
					}
					
					System.out.println("Your balance is : " + balance);
					newLine();
					break;
				}
				
				case 4:
				{
					end = true;
					break;
				}
				
				default:
				{
					System.out.println("Wrong option number entered...");
				}
				
			}
			
		}
		input.close();
	}
	
	
	private static void newUserInstructions(User u)
	{
		System.out.println("Your Public Key : " + u.getPublicKey());
		System.out.println("Your Private Key : " + u.getPrivateKey());
		System.out.println("You need to remember your public key (copy it). Your private key must"
				+ "remain a secret. You can access your private key only through the password you set."
				+ "Private key is used for signing transactions and hence leak of private key can lead to theft.");
	}
	
	
	private static void newLine()
	{
		System.out.println();
	}
}
