package nigeriandailies.com.ng.noteapp;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Note  implements Serializable {
    private Long mDateTime;
    private String mTitle;
    private String mContent;

//    create constructor for mDateTime, mTitle, mContent

    public Note(Long mDateTime, String mTitle, String mContent) {
        this.mDateTime = mDateTime;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }


//    set setters and getters for mDateTime, mTitle, mContent


    public Long getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Long mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    //    getters that will returns time in string format not in the milliseconds
    public String getDateTimeFormatted(Context context){
//        simple date format

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy, hh:mm:ss"
                , context.getResources().getConfiguration().locale);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(mDateTime));
    }
}
