package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionProcessingService {
    
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository;
    
    public TransactionProcessingService(UserRepository userRepository, 
                                        TransactionRecordRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    
   @Transactional
public void processTransaction(Transaction txn) {
    //  Look up both users in the database
    UserRecord sender = userRepository.findById(txn.getSenderId());
    UserRecord recipient = userRepository.findById(txn.getRecipientId());

    //  Validate: Do both exist? And does sender have enough money?
    if (sender != null && recipient != null && sender.getBalance() >= txn.getAmount()) {
        
        //  Update Balances
        sender.setBalance(sender.getBalance() - txn.getAmount());
        recipient.setBalance(recipient.getBalance() + txn.getAmount());

        //  Record the transaction
        TransactionRecord record = new TransactionRecord();
        record.setSender(sender);
        record.setRecipient(recipient);
        record.setAmount(txn.getAmount());
        System.out.println("DATA: " + sender);
        System.out.println("DATA: " + recipient);        
        //  Save changes
        userRepository.save(sender);
        userRepository.save(recipient);
        transactionRepository.save(record);
    }
}
}
