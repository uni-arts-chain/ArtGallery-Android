package com.artgallery.eth.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.artgallery.base.YunApplication;
import com.artgallery.eth.interact.AddTokenInteract;
import com.artgallery.eth.interact.FetchWalletInteract;
import com.artgallery.eth.repository.RepositoryFactory;

public class AddTokenViewModelFactory implements ViewModelProvider.Factory {
    private final AddTokenInteract addTokenInteract;
    private final FetchWalletInteract findDefaultWalletInteract;

    public AddTokenViewModelFactory() {
        RepositoryFactory rf = YunApplication.repositoryFactory();

        this.findDefaultWalletInteract = new FetchWalletInteract();
        this.addTokenInteract = new AddTokenInteract(findDefaultWalletInteract, rf.tokenRepository);
        ;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTokenViewModel(addTokenInteract, findDefaultWalletInteract);
    }
}
