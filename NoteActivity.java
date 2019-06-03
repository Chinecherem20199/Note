package nigeriandailies.com.ng.noteapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;

public class NoteActivity extends AppCompatActivity {

    //state of the activity
    private boolean mIsViewingOrUpdating;
    private long mNoteCreationTime;
    private EditText mEtTitle;
    private  EditText mEtContent;

    private String mNoteFileName;
    private  Note mLoadedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEtTitle = findViewById(R.id.note_title_edit);
        mEtContent = findViewById(R.id.note_content);

        mNoteFileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);


        //check if view/edit note bundle is set, otherwise user wants to create new note
        if (mNoteFileName != null && !mNoteFileName.isEmpty() && mNoteFileName.endsWith(Utilities.EXTRAS_NOTE_FILENAME)){
            mLoadedNote = Utilities.getNoteByName(this, mNoteFileName);

            if (mLoadedNote != null){

                //update the widgets from the loaded note
                mEtTitle.setText(mLoadedNote.getmTitle());
                mEtContent.setText(mLoadedNote.getmContent());
                mNoteCreationTime = mLoadedNote.getmDateTime();
                mIsViewingOrUpdating = true;

            }else {
                //user wants to create a new note
                mNoteCreationTime = System.currentTimeMillis();
                mIsViewingOrUpdating = false;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//         instantiate menu XML files into Menu objects
        //load menu based on the state we are in (new, view/update/delete)
        if(mIsViewingOrUpdating) { //user is viewing or updating a note
            getMenuInflater().inflate(R.menu.menu_note_new, menu);
        } else { //user wants to create a new note
            getMenuInflater().inflate(R.menu.menu_note_add, menu);
        }

     return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()){
            case  R.id.action_note_save:
            case R.id.action_update_note:
//                call the function save note
                saveNote();
                break;

            case R.id.action_note_delete:
                deleteNote();
                break;
            case R.id.action_cancel: //cancel the note
                actionCancel();
                break;
        }

        return true;
    }

    /**
     * Back button press is same as cancel action...so should be handled in the same manner!
     */
    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void actionCancel() {
        if(!checkNoteAltred()) { //if note is not altered by user (user only viewed the note/or did not write anything)
            finish(); //just exit the activity and go back to MainActivity
        } else { //we want to remind user to decide about saving the changes or not, by showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("discard changes...")
                    .setMessage("are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //just go back to main activity
                        }
                    })
                    .setNegativeButton("NO", null); //null = stay in the activity!
            dialogCancel.show();
        }
    }

    private boolean checkNoteAltred() {
        if(mIsViewingOrUpdating) { //if in view/update mode
            return mLoadedNote != null && (!mEtTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getmTitle())
                    || !mEtContent.getText().toString().equalsIgnoreCase(mLoadedNote.getmContent()));
        } else { //if in new note mode
            return !mEtTitle.getText().toString().isEmpty() || !mEtContent.getText().toString().isEmpty();
        }
    }


    //     create a function to save note
    private void saveNote(){
        //get the content of widgets to make a note object
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();

        //see if user has entered anything :D lol
        if(title.isEmpty()) { //title
            Toast.makeText(NoteActivity.this, "please enter a title!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.isEmpty()) { //content
            Toast.makeText(NoteActivity.this, "please enter a content for your note!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        //set the creation time, if new note, now, otherwise the loaded note's creation time
        if(mLoadedNote != null) {
            mNoteCreationTime = mLoadedNote.getmDateTime();
        } else {
            mNoteCreationTime = System.currentTimeMillis();
        }

        //finally save the note!
        if(Utilities.saveNote(this, new Note(mNoteCreationTime, title, content))) { //success!
            //tell user the note was saved!
            Toast.makeText(this, "note has been saved", Toast.LENGTH_SHORT).show();
        } else { //failed to save the note! but this should not really happen :P :D :|
            Toast.makeText(this, "can not save the note. make sure you have enough space " +
                    "on your device", Toast.LENGTH_SHORT).show();
        }

        finish(); //exit the activity, should return us to MainActivity
    }



    private void deleteNote(){
        //ask user if he really wants to delete the note!
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("delete note")
                .setMessage("really delete the note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && Utilities.deleteNote(getApplicationContext(), mNoteFileName)) {
                            Toast.makeText(NoteActivity.this, mLoadedNote.getmTitle() + " is deleted"
                                    , Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NoteActivity.this, "can not delete the note '" + mLoadedNote.getmTitle() + "'"
                                    , Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NO", null); //do nothing on clicking NO button :P

        dialogDelete.show();
    }
}

