import com.bankingtransactions.models.Account;
import com.bankingtransactions.dao.AccountDao;

public class Test {
	public static void main(String[] args) {
//		DBUtil.createDB();
		Account account = new Account(0.0, "1234", "ACTIVE", "Srijagangula"," 987455422", "Srija@gmail.com");
		AccountDao accountDao = new AccountDao();
		accountDao.insert(account);
	}
}
