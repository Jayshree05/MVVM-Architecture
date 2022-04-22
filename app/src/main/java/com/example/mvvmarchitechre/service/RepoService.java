package com.example.mvvmarchitechre.service;

import com.example.mvvmarchitechre.model.Cat;
import com.example.mvvmarchitechre.model.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RepoService {


    @GET("orgs/Google/repos")
    Single<List<Repo>> getRepositories();


    @GET("repos/{owner}/{name}")
    Single<Repo> getRepo(@Path("owner") String owner, @Path("name") String name);


    @GET("search")
    Call<List<Cat>> getImgs(@Query("limit") int limit);


}
