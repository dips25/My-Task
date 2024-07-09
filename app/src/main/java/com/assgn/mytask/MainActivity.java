package com.assgn.mytask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.assgn.mytask.fragments.AddFragment;
import com.assgn.mytask.fragments.NotesFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    Button google;

    private GoogleSignInClient mGoogleSignInClient;
    NotesFragment notesFragment;
    AddFragment addFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        google = (Button) findViewById(R.id.google);

        notesFragment = new NotesFragment();
        addFragment = new AddFragment();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        google.setOnClickListener((v)->{

            googleLogin();
        });


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");







    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getSharedPreferences("userdetails" , MODE_PRIVATE)
                .getBoolean("isLoggedIn" , false)) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame , notesFragment)
                    .addToBackStack("NotesFragment")
                    .commit();
            google.setVisibility(View.GONE);

        }
    }

    private void googleLogin() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(MainActivity.this, "Exp:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        getSharedPreferences("userdetails" , MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLoggedIn" , true)
                                .commit();

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_frame , notesFragment)
                                .addToBackStack("NotesFragment")
                                .commit();

                        google.setVisibility(View.GONE);

                        //checkUser(user.getUid() , "Google");
                        // Update UI with the signed-in user's information
                    } else {
                        // Sign-in failure
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main , menu);
        MenuItem item = menu.findItem(R.id.add);
        MenuItem item1 = menu.findItem(R.id.delete);

        item.setVisible(false);
        item1.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame , new AddFragment())
                    .addToBackStack("AddFragment")
                    .commit();
        } else if (item.getItemId() == R.id.delete) {

            return false;



        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount()>1) {

            getSupportFragmentManager().popBackStack();
        } else {

            finish();
        }



//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.main_frame , notesFragment)
//                .addToBackStack("NotesFragment")
//                .commit();

    }
}