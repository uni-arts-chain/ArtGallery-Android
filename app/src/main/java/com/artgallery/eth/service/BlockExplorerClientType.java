package com.artgallery.eth.service;


import com.artgallery.eth.domain.ETHWallet;
import com.artgallery.eth.entity.Transaction;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
    Observable<Transaction[]> fetchTransactions(ETHWallet forAddress, String forToken, String coin);
}
