package nigeriandailies.com.ng.noteapp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {
    public static final String EXTRAS_NOTE_FILENAME = "EXTRAS_NOTE_FILENAME";
    public static final String FILE_EXTENSION = ".bin";


    //   Method to save the note
    public static boolean saveNote(Context context, Note note) {
        String fileName = String.valueOf(note.getmDateTime()) + FILE_EXTENSION;


//        To write a file as a stream of bytes
//        Serialization of files
        FileOutputStream fileOutputStream;

//        To write Java objects to an OutputStream instead of just raw bytes

//        Serialization of object
        ObjectOutputStream objectOutputStream;
        try {
            fileOutputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(note);
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;

        }

        return true;
    }

    //   create a method to Load the note
    public static ArrayList<Note> getAllSavedNote(Context context) {
        ArrayList<Note> notes = new ArrayList<>();

        File filesDir = context.getFilesDir();
        ArrayList<String> noteFiles = new ArrayList<>();

        for (String file : filesDir.list()) {
            if (file.endsWith(FILE_EXTENSION)) {
                noteFiles.add(file);
            }
        }
//        Deserialization of files and object
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        for (int i = 0; i < noteFiles.size(); i++) {
            try {
                fileInputStream = context.openFileInput(noteFiles.get(i));
                objectInputStream = new ObjectInputStream(fileInputStream);
                notes.add((Note) objectInputStream.readObject());

                fileInputStream.close();
                objectInputStream.close();

            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();

                return null;
            }

        }
        return notes;
    }
//  Method used to get note by name
    public static Note getNoteByName(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists() && !file.isDirectory()) { //check if file actually exist

            Log.v("UTILITIES", "File exist = " + fileName);

            FileInputStream fileInputStream;
            ObjectInputStream objectInputStream;

            try { //load the file
                fileInputStream = context.openFileInput(fileName);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Note note = (Note) objectInputStream.readObject();
                fileInputStream.close();
                objectInputStream.close();

                return note;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }

    public static boolean  deleteNote(Context context, String fillName) {
        File dir = context.getFilesDir();
        File file = new File(dir, fillName);

        if(file.exists() && !file.isDirectory()) {
            return file.delete();
        }

        return false;
        }

    }
