package ir.ounegh.vardast;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static final ArrayList<Category> CATS = new ArrayList<>();
    public static Location LOCATION;
    private TextView mTextMessage;
    public  static String SECAT="";
    public  static ArrayList<Mlocation>MLOCATIONS=new ArrayList<>();
    private FragmentManager fragmentManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    if(CATS.size()<1){
                        return false;
                    }
                    CatsFragment ctfr=new CatsFragment();
                    chaneFrag(ctfr);
                    return true;
                case R.id.navigation_dashboard:
             HelpFragment hp= new HelpFragment();
             chaneFrag(hp);
                 //   mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                //    mTextMessage.setText(R.string.title_notifications);
                    AboutFragment lf=new AboutFragment();
                    chaneFrag(lf);
                    return true;
            }
            return false;
        }
    };
   static double longitude=0;
  static   double latitude=0;
  ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        fragmentManager=getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        latitude=getIntent().getExtras().getDouble("latitude",36.0);
        longitude=getIntent().getExtras().getDouble("longitude",54.04);
       // Toast.makeText(this,latitude+"E"+longitude+"N",Toast.LENGTH_SHORT).show();
       pb=findViewById(R.id.loading_spinner);
       getCats();
    }


    public static Location getLocation(){

        Location l=new Location("gps");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        return l;
    }
    public  void getList(String cat){}


    private void chaneFrag(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();
    }


    public void getCats(){
        Object selectedcat = null;
        Call<List<Category>> ccall=
                VrdClient.getClient().create(VrdApi.class).getCats();
        ccall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                CATS.clear();
                for(int i=0;i<response.body().size();i++){
                   CATS.add(response.body().get(i));

                }
                Toast.makeText(MainActivity.this,response.body().toString(),Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                CatsFragment ctfr=new CatsFragment();
                chaneFrag(ctfr);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"error"+call.toString(),Toast.LENGTH_LONG).show();
            }
        });



    }





}
