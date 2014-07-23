package app.registry.mch.mchpregnancyguide.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Isa on 18.07.2014.
 */
public class Visit {
    private int _id;
    private String _visitText;
    private String _receivedDate;
    private String _visitDate;

    public Visit() {
    }

    public Visit(int _id, String _visitText, String _receivedDate, String _visitDate) {
        this._visitText = _visitText;
        this._receivedDate = _receivedDate;
        this._id = _id;
        this._visitDate = _visitDate;
    }

    public Visit(String _visitText, String _receivedDate, String _visitDate) {
        this._visitText = _visitText;
        this._receivedDate = _receivedDate;
        this._visitDate = _visitDate;
    }

    public String get_receivedDate() {
        return _receivedDate;
    }

    public void set_receivedDate(String _receivedDate) {
        this._receivedDate = _receivedDate;
    }

    public String get_visitText() {
        return _visitText;
    }

    public void set_visitText(String _visitText) {
        this._visitText = _visitText;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public String get_visitDate() {
        return _visitDate;
    }

    public void set_visitDate(String _visitText) {

        this._visitDate = this.getDateFromVisit(_visitText);
    }

    private static String getDateFromVisit(String desc) {
        int count=0;
        String match = "";
        Matcher m = Pattern.compile("(0[1-9]|1[012])[- ..](0[1-9]|[12][0-9]|3[01])[- ..](19|20)\\d\\d").matcher(desc);
        m.find();
        match = m.group();
        return match;
    }
}
