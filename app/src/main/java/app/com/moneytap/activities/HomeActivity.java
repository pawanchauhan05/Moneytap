package app.com.moneytap.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.moneytap.R;
import app.com.moneytap.adapters.DataAdapter;
import app.com.moneytap.models.Response;
import app.com.moneytap.retrofit.ApiClient;
import app.com.moneytap.retrofit.RetrofitObserver;
import app.com.moneytap.services.Api;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List dataList = new ArrayList();
    private ProgressDialog progressDialog;
    public RelativeLayout relativeLayoutList, relativeLayoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        getData("", true);
    }

    private void getData(String query, boolean isShow) {
        if (isShow)
            showProgressDialog();

        if(TextUtils.isEmpty(query)) {
            dataAdapter.getDataList().clear();
            dataAdapter.notifyDataSetChanged();
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action", "query");
        queryParams.put("format", "json");
        queryParams.put("formatversion", "2");
        queryParams.put("prop", "pageimages|pageterms");
        queryParams.put("generator", "prefixsearch");
        queryParams.put("redirects", "1");
        queryParams.put("pithumbsize", "100");
        queryParams.put("piprop", "thumbnail");
        queryParams.put("pilimit", "10");
        queryParams.put("wbptterms", "description");
        queryParams.put("gpslimit", "10");
        queryParams.put("gpssearch", query);

        ApiClient
                .getClient()
                .create(Api.class)
                .getData(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetrofitObserver<Response>() {
                    @Override
                    protected void onSuccess(Response object) {
                        if (object != null && object.getQuery() != null)
                            dataAdapter.addData(object.getQuery().getPages());
                        else
                            dataAdapter.notifyDataSetChanged();
                        hideProgressDialog();
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        hideProgressDialog();
                    }
                });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait....");
            progressDialog.show();
            Log.e("Pawan", "showProgressDialog: ");
        } else {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            Log.e("Pawan", "hideProgressDialog: ");
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        relativeLayoutEmpty = findViewById(R.id.relativeLayoutEmpty);
        relativeLayoutList = findViewById(R.id.relativeLayoutList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        dataAdapter = new DataAdapter(dataList, this);
        recyclerView.setAdapter(dataAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);


        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Pawan", "onQueryTextSubmit:query " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("Pawan", "onQueryTextChange: " + s);
                if (!TextUtils.isEmpty(s) && s.trim().length() >= 3) {
                    getData(s, false);
                } else {
                    getData("", false);
                }

                return false;
            }
        });
        return true;

    }
}
