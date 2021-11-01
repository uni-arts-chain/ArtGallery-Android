package com.artgallery.eth.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.artgallery.base.YunApplication;
import com.artgallery.eth.interact.FetchTokensInteract;
import com.artgallery.eth.interact.FetchWalletInteract;
import com.artgallery.eth.repository.EthereumNetworkRepository;
import com.artgallery.eth.repository.RepositoryFactory;

public class TokensViewModelFactory implements ViewModelProvider.Factory {

    private final FetchTokensInteract fetchTokensInteract;
    private final EthereumNetworkRepository ethereumNetworkRepository;

    public TokensViewModelFactory() {

        RepositoryFactory rf = YunApplication.repositoryFactory();
        fetchTokensInteract = new FetchTokensInteract(rf.tokenRepository);
        ethereumNetworkRepository = rf.ethereumNetworkRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TokensViewModel(
                ethereumNetworkRepository,
                new FetchWalletInteract(),
                fetchTokensInteract
                );
    }
}
