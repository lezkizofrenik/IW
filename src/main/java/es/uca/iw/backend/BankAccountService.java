package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private BankAccountRepository repo;

    @Autowired
    private BankAccountService(BankAccountRepository repo){
        this.repo = repo;
    }
    public List<BankAccount> listar(){
        return repo.findAll();
    }

    public BankAccount searchById(int id){
        return repo.findById(id);
    }
    public synchronized BankAccount guardarBankAccount(BankAccount entrada) {
        return repo.save(entrada);
    }
    public void borrarBankAccount(BankAccount entidad) {
        repo.delete(entidad);
    }
}
