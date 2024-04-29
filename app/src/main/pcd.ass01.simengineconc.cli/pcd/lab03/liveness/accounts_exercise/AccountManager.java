package pcd.lab03.liveness.accounts_exercise;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountManager {
	
	private final Account[] accounts;
	private final Lock[] locks;

	public AccountManager(int nAccounts, int amount){
		accounts = new Account[nAccounts];
		locks = new Lock[nAccounts];
		for (int i = 0; i < accounts.length; i++){
			accounts[i] = new Account(amount);
			locks[i] = new ReentrantLock();
		}
	}
	
	public void transferMoney(int from,	int to, int amount) throws InsufficientBalanceException {
		final Account sender = this.accounts[from];
		final Account receiver = accounts[to];
		try {
			locks[Math.min(from, to)].lockInterruptibly();
			locks[Math.max(from, to)].lockInterruptibly();

			if (sender.getBalance() < amount) {
				throw new InsufficientBalanceException();
			}
			receiver.credit(amount);
			sender.credit(amount);

		} catch (InterruptedException ignored) {
		} finally {
			locks[Math.min(from, to)].unlock();
			locks[Math.max(from, to)].unlock();
		}
	}
	
	public int getNumAccounts() {
		return accounts.length;
	}
}

