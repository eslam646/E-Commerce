package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.my_ecommerce.Aapter.ProductAdapter;
import com.example.my_ecommerce.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SearchProductsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT =1000 ;
    private Button searchBtn,ScannerBtn;
    private EditText search_inputText;
    private RecyclerView recycler;
    private String searchInput;
    private ProductAdapter productAdapter;
    private SearchView searchView;
    //   private ProductAdapter.ViewHolder productAdapter;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");
    private ArrayList<Product> list;

    ImageButton VoiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        searchView =(SearchView) findViewById(R.id.searchView);
        ScannerBtn=(Button)findViewById(R.id.scanner_btn);

        VoiceBtn=findViewById(R.id.search_voice);

        recycler=(RecyclerView)findViewById(R.id.search_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();
        productAdapter=new ProductAdapter(getApplicationContext(),list);

        VoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi speak something");

                try {
                    startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e)
                {
                    Toast.makeText(SearchProductsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
                productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Product product= list.get(position);
                        Intent intent=new Intent(getApplicationContext(),ProductDetailsActivity.class);

                        intent.putExtra("pid",product.getP_Id());
                        startActivity(intent);
                    }
                });
      /*  productAdapter= new ProductAdapter.ViewHolder();
        productAdapter*/
                recycler.setAdapter(productAdapter);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {

                                Product product = dataSnapshot.getValue(Product.class);
                                list.add(product);

                        }

                        productAdapter.setdata(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        productAdapter.getFilter().filter(newText);
                        System.out.println("rafeek");
                        return false;
                    }
                });
                ScannerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SearchProductsActivity.this,CodeScannerActivity.class));
                        finish();
                    }
                });
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_SPEECH_INPUT:
            {
                if(resultCode == RESULT_OK && data!=null)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    productAdapter.getFilter().filter(result.get(0));
                }
                break;
            }
        }
    }
            /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.product_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}