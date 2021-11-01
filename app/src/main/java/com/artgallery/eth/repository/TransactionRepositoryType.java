package com.artgallery.eth.repository;


import com.artgallery.eth.domain.ETHWallet;
import com.artgallery.eth.entity.Transaction;


import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface TransactionRepositoryType {
    Observable<Transaction[]> fetchTransaction(ETHWallet walletAddr, String token, String coin);

    Maybe<Transaction> findTransaction(ETHWallet walletAddr, String transactionHash, String coin);

}
