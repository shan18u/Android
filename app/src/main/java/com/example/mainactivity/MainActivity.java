package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Toast;

// Import Dexter Library add the implementation under dependencies
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        int position = 0;

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

                // Steps on how to handle permission methods
                .withListener(new PermissionListener() {

                    // Permission Accepted
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                     //   Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();

                        ArrayList<File> mySongs = fetchsongs(Environment.getExternalStorageDirectory());
                        String [] items = new String[mySongs.size()];
                        for (int i = 0; i<mySongs.size();i++){
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");
                        }
                        ArrayAdapter <String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", mySongs);
                                intent.putExtra("currentSongs", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);


                            }
                        });

                    }

                    // Permission Denied
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }
                    // Permission Repeat
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        // Runtime Permission
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
                }



            // Method FetchSongs: Get all the songs from the directory or External Storage
            public ArrayList <File> fetchsongs(File file) {

                ArrayList arrayList = new ArrayList();
                File [] songs = file.listFiles();            // Will List all the files then add it to the Arraylist
                if (songs!= null) {
                    for(File myFile: songs){
                        if(!myFile.isHidden() && myFile.isDirectory()){    // If myFile is Directory and it is not hidden
                            arrayList.addAll(fetchsongs(myFile));          // Then run the function again, add all the files inside the arraylist
                        }
                        else {
                            if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")) {   // if my file is .mp3 && myfiles that do not starts with "."
                                arrayList.add(myFile);                                                      // Add to arrayList 

                            }
                        }
                    }

                }
                return arrayList;
            }

}