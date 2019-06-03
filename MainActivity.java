package nigeriandailies.com.ng.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

//    create tag for the fragment
    private  static final String TAG = "MainActivity";

    private ListView listViewNote;

//    call the section pageAdapter for the fragment
//    then create sectionPageAdapter class and extends fragmentPageAdapter
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewNote = findViewById(R.id.lisView);

//        log for the fragment
        Log.d(TAG,"onCreate: Starting.");

//        declare sectionPageAdapter
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPageAdapter);

//        set viewPager
        setUpViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//    create method setUpViewPager, what this method does is to create sectionViewPagerAdapter
    private void setUpViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(),"Home");
        adapter.addFragment(new Calender(), "Calender");

//        set the adapter to viewPager
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_main_new_note:

//       Start NoteActivity in newNote mode
                Intent newNoteActivity = new Intent(this, NoteActivity.class);
                startActivity(newNoteActivity);

//                startActivity(new Intent(MainActivity.this, NoteActivity.class));
                break;
            // //TO show settings activity
            case  R.id.action_settings:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

//    populate the listView


    @Override
    protected void onResume() {
        super.onResume();
        //load saved notes into the listView
        //first, reset the listView
//        set the listView to Adapter
        listViewNote.setAdapter(null);

//        onResume the app get all the note here
        ArrayList<Note> notes = Utilities.getAllSavedNote(this);


        //sort notes from new to old
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                if(lhs.getmDateTime() > rhs.getmDateTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
//       check if we have any notes!
        if(notes != null && notes.size() > 0) { //check if we have any notes!
            final NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            listViewNote.setAdapter(na);

            //set click listener for items in the list, by clicking each item the note should be loaded into NoteActivity
            listViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) listViewNote.getItemAtPosition(position)).getmDateTime()
                            + Utilities.FILE_EXTENSION;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                    startActivity(viewNoteIntent);
                }
            });
        } else { //remind user that we have no notes!
            Toast.makeText(getApplicationContext(), "you have no saved notes!\ncreate some new notes :)"
                    , Toast.LENGTH_SHORT).show();
                }
            }
        }


