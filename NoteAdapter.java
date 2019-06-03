package nigeriandailies.com.ng.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter  extends ArrayAdapter<Note> {



    public static final int WRAP_CONTENT_LENGTH = 50;
    public NoteAdapter( Context context, int resource,  ArrayList<Note>notes) {
        super(context, resource, notes);
    }
//     populate the note in the listView using noteAdapter
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note, null);
        }
        Note note = getItem(position);

        if (note != null){
            TextView date = convertView.findViewById(R.id.list_note_date);
            TextView title = convertView.findViewById(R.id.list_note_title);
            TextView content = convertView.findViewById(R.id.list_note_content);


            title.setText(note.getmTitle());
            date.setText(note.getDateTimeFormatted(getContext()));

            //correctly show preview of the content (not more than 50 char or more than one line!)
            int toWrap = WRAP_CONTENT_LENGTH;
            int lineBreakIndex = note.getmContent().indexOf('\n');
            //not an elegant series of if statements...needs to be cleaned up!
            if(note.getmContent().length() > WRAP_CONTENT_LENGTH || lineBreakIndex < WRAP_CONTENT_LENGTH) {
                if(lineBreakIndex < WRAP_CONTENT_LENGTH) {
                    toWrap = lineBreakIndex;
                }
                if(toWrap > 0) {
                    content.setText(note.getmContent().substring(0, toWrap) + "...");
                } else {
                    content.setText(note.getmContent());
                }
            } else { //if less than 50 chars...leave it as is :P
                content.setText(note.getmContent());
            }
        }

        return convertView;
    }
}
