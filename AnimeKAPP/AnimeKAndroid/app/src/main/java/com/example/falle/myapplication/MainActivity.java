package com.example.falle.myapplication;





import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_Favoritos.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener{



    private ViewPager mViewPager;
    private RecyclerView recyclerView;
    private AdapterAnime adapterAnime;
    private ArrayList<Anime> animeArrayList;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private SearchView searchView;
    private MenuItem searchItem;
    private AdapterManga adapterManga;
    private ArrayList<Manga> mangaArrayList;
    String anime;
    int page=0;
    PullRefreshLayout layout;
    private FirebaseAuth mAuth;
    NavigationView navigationView;
    TextView Text;
    TextView correo;
    String nombre;
    String correos;
    String myUri;
    String userName;
    String email;
    String profile_picture;

    String cover;
    String en_jp;
    String ja_jp ;
    String poster ;
    String synopsis;
    int id;
    String episode ;
    String fechaEstreno ;
    String fechaFin;
    GoogleApiClient googleApiClient;
    String unique;

     private static final int uniqueID= 4556;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycling_view_search);
        recyclerView.setHasFixedSize(true);
        layout = findViewById(R.id.swipeRefreshLayout);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        animeArrayList = new ArrayList<>();
        adapterAnime = new AdapterAnime(this, animeArrayList);
        mangaArrayList = new ArrayList<>();
        adapterManga = new AdapterManga(this, mangaArrayList);
        requestQueue = Volley.newRequestQueue(this);

        mAuth=FirebaseAuth.getInstance();
        Log.i("User", mAuth+"");






        refresh();
        recyclerView.setAdapter(adapterAnime);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] TO = {"thejoncelito07.jg@gmail.com"};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, TO);

                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "" );
                startActivity(Intent.createChooser(intent, "Send mail..."));



            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        correos = intent.getStringExtra("correo");
        myUri = intent.getStringExtra("photo");
        unique = intent.getStringExtra("uniqueID");



        View header = navigationView.getHeaderView(0);
        ImageView imagen = header.findViewById(R.id.imageView);
        Text = (TextView) header.findViewById(R.id.nav_text);
        correo = (TextView) header.findViewById(R.id.textView);
        Text.setText(nombre);
        correo.setText(correos);
        Picasso.with(MainActivity.this).load(myUri).into(imagen);

        if(unique!=null){

            email = intent.getStringExtra("email");
            nombre =intent.getStringExtra("userName");
            profile_picture = intent.getStringExtra("profile_picture");
            Text.setText(nombre);
            correo.setText(email);
            Picasso.with(MainActivity.this).load(profile_picture).into(imagen);

        }









    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                anime=query;
                searchAnime(anime,page);
                return true;



            }

            @Override
            public boolean onQueryTextChange(String newText) {
               return false;

            }
        });

        return true;
    }
    private void searchAnime(String anime, int page){
        String url = "https://kitsu.io/api/edge/anime?filter[text]=" + anime +" &page%5Blimit%5D=20&page%5Boffset%5D=" + page;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonObject) {
                        try {
                            JSONArray jsonArray = JsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                en_jp = data.getJSONObject("attributes").getJSONObject("titles").getString("en_jp");
                                ja_jp = data.getJSONObject("attributes").getJSONObject("titles").getString("ja_jp");
                                poster = data.getJSONObject("attributes").getJSONObject("posterImage").getString("large");
                                synopsis = data.getJSONObject("attributes").getString("synopsis");
                                JSONObject coverImage = data.getJSONObject("attributes");
                                if( coverImage.isNull("coverImage")){
                                    cover=null;
                                }else{
                                    cover = coverImage.getJSONObject("coverImage").getString("large");
                                }
                                int id = Integer.parseInt(data.getString("id"));
                                String episode = String.valueOf(data.getJSONObject("attributes").getString("episodeCount"));
                                String fechaEstreno = data.getJSONObject("attributes").getString("startDate");
                                String fechaFin = data.getJSONObject("attributes").getString("endDate");

                                Anime anime = new Anime(id, en_jp, ja_jp, poster, synopsis, cover, episode, fechaEstreno, fechaFin, nombre);
                                animeArrayList.add(anime);
                                Collections.reverse(animeArrayList);
                            }
                            adapterAnime = new AdapterAnime(MainActivity.this,animeArrayList);
                            recyclerView.setAdapter(adapterAnime);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }






    private void refresh(){
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRefresh() {
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        searchAnime(anime, page+=20);

                        layout.setRefreshing(false);


                        super.onPostExecute(s);
                    }

                }.execute();
            }
        });
    }

    public void showDialogMenu(){
        Log.d("showDialogMenu()", "Method was called.");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String [] generos ={ "Comedy", "Fantasy", "Romance" ,"Action" ,"School","Life","Tragedy", "Adventure", "Shoujo Ai", "Daily Life", "Science Fiction" ,"Yaoi",
                "Sports", "Japan", "Earth","Thriller","Historical", "Present"," Mystery" ,"Asia" ,"Harem", "Magic", "Kids", " Horror",  "Mecha", "Music", " Psychological" ,"Super Power", "Shounen Ai" ,"Martial" , "Arts Demon ", "Military ",
                "Plot Continuity", "Motorsport", "Fantasy World" ,"Parody" ,"Violence" ,"Space" , "Future"," Contemporary Fantasy"," Past" };
        builder.setTitle("Genders");
        builder.setItems(generos, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Fragment fragment = null;
                boolean FragmentTransaction =false;
                for(int i=0; i<generos.length; i++){
                    if(item ==i) {

                        Toast.makeText(MainActivity.this, generos[i],
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        fragment = new Category();
                        FragmentTransaction=true;
                        Bundle bundle = new Bundle();
                        bundle.putString("category", generos[i]);
                        bundle.putString("name", nombre);
                        fragment.setArguments(bundle);
                        recyclerView.setVisibility(View.INVISIBLE);
                        searchView.setVisibility(View.INVISIBLE);
                        searchItem.setVisible(false);
                        if (FragmentTransaction) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();


                        }



                    }
                }



            }
        });

        builder.show();
    }
    public void cerrarSesion(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you Sure?");
        final String [] sesion ={ "Yes", "No"};

        builder.setItems(sesion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(MainActivity.this, "Session Close, See you later!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, InicioSesion.class);
                            startActivity(intent);
                        }
                    });

                }

            }
        });
        builder.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            cerrarSesion();


            return true;
        }else if(id==R.id.action_category){
            showDialogMenu();
        }else if(id==R.id.action_user_configuration){
            if(unique!=null) {
                Intent i = getIntent();
                Intent intent = new Intent(MainActivity.this, UserConfiguration.class);
                email = i.getStringExtra("email");
                nombre =i.getStringExtra("userName");
                profile_picture = intent.getStringExtra("profile_picture");
                intent.putExtra("email", email);
                intent.putExtra("userName", nombre);
                intent.putExtra("profile_picture", profile_picture);
                intent.putExtra("Unique", "Unique");
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "You are signed by Google Account, you can change your profile", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }
    public void onDrawerOpened() {
        getActionBar();
        getFragmentManager().popBackStackImmediate();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment=null;
       boolean FragmentTransaction = false;
        recyclerView.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.INVISIBLE);
        searchItem.setVisible(false);



        if (id == R.id.nav_favorites) {

            fragment = new Fragment_Favoritos();
            FragmentTransaction = true;
            Bundle b = new Bundle();
            b.putString("name", nombre);
            fragment.setArguments(b);

        }else if(id== R.id.nav_store){
            fragment = new Store();
            FragmentTransaction=true;


        } else if (id == R.id.nav_trending) {
            fragment = new TrendingSeries();
            FragmentTransaction=true;
            Bundle b = new Bundle();
            b.putString("name", nombre);
            fragment.setArguments(b);



        } else if (id == R.id.animes) {
            fragment = new ListAnime();
            FragmentTransaction= true;
            Bundle b = new Bundle();
            b.putString("name", nombre);
            fragment.setArguments(b);

        } else if (id == R.id.search_anime) {


            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("photo", myUri);
            intent.putExtra("correo", correos);
            startActivity(intent);


        } else if (id == R.id.nav_trending_manga) {
            fragment = new TrendingManga();
            FragmentTransaction=true;
            Bundle b = new Bundle();
            b.putString("name", nombre);
            fragment.setArguments(b);



        } else if (id == R.id.mangas) {
            fragment = new ListManga();
            FragmentTransaction=true;

        } else if (id == R.id.search_manga) {
            searchView.setVisibility(View.VISIBLE);
            searchItem.setVisible(true);
            Intent intent = new Intent(MainActivity.this, MangasSearch.class);
            startActivity(intent);



        }

        if (FragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

            }




        onBackPressed();
        return true;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Sign in FAILED!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, InicioSesion.class);
        startActivity(intent);

    }
}

