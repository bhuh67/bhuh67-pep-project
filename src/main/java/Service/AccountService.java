package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
   
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    //make a new account
    public Account addAccount(Account account){
        if(account.getUsername() != "" && account.getPassword().length() > 4){
            if(accountDAO.getAccountByUsername(account.getUsername()) != null){
                System.out.println("Account already exists");
                return null;
            }
            return accountDAO.addAccount(account);
        }
        else{
            //System.out.println(account.getUsername() + account.getPassword());
            return null;

        }    
    }
    //

    public Account loginAccount(Account account) {
        Account foundAccount = accountDAO.getAccountByUsername(account.getUsername());
        if(!foundAccount.getPassword().equals(account.getPassword())){
            return null;
        }
        return foundAccount;
    }    

    public Account identifyAccount(int id) {
        return accountDAO.getAccountById(id);
    }

}
