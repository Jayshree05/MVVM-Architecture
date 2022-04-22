package com.example.mvvmarchitechre.view.list;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmarchitechre.R;
import com.example.mvvmarchitechre.base.BaseFragment;
import com.example.mvvmarchitechre.db.AppDatabase;
import com.example.mvvmarchitechre.model.Repo;
import com.example.mvvmarchitechre.viewmodel.ListViewModel;
import com.example.mvvmarchitechre.viewmodel.ViewModelFactory;
import com.example.mvvmarchitechre.utility.session.SessionManager;


import javax.inject.Inject;

import butterknife.BindView;


public class RepoListFragment extends BaseFragment implements RepoSelectedListener {

    @Override
    protected int layoutRes() {
        return R.layout.screen_list;
    }

    @BindView(R.id.recyclerView)
    RecyclerView listView;
    @BindView(R.id.tv_error)
    TextView errorTextView;
    @BindView(R.id.loading_view)
    View loadingView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_password)
    TextView tvPassword;

    @Inject
    ViewModelFactory viewModelFactory;
    private ListViewModel viewModel;
    String userEmail, userPassword;
    AppDatabase db;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel.class);

        listView.addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
        listView.setAdapter(new RepoListAdapter(viewModel, this, this));
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        userEmail = SessionManager.get().getUserEmail();
        userPassword = SessionManager.get().getUserPassword();

        tvName.setText(userEmail);
        tvPassword.setText(userPassword);

        db = AppDatabase.getInstance(getContext());
       Log.d("User list size", String.valueOf(db.userDao().getAll().size()));

        observableViewModel();
    }

    @Override
    public void onRepoSelected(Repo repo) {
        Log.d("Selected Repo",repo.name);
    }

    private void observableViewModel() {
        viewModel.getRepos().observe(getBaseActivity(), repos -> {
            if (repos != null) listView.setVisibility(View.VISIBLE);
        });

        viewModel.getError().observe(getBaseActivity(), isError -> {
            if (isError != null) if (isError) {
                errorTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                errorTextView.setText("An Error Occurred While Loading Data!");
            } else {
                errorTextView.setVisibility(View.GONE);
                errorTextView.setText(null);
            }
        });

        viewModel.getLoading().observe(getBaseActivity(), isLoading -> {
            if (isLoading != null) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    errorTextView.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }
}
