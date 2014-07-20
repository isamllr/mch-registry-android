package app.registry.mch.mchpregnancyguide.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Visit {
    private int _id;
    private String _visitText;
    private String _visitDay;
    private String _visitFacility;

    public Visit() {
    }

    public Visit(int _id, String _visitText, String _visitDay, String _visitFacility) {
        this._id = _id;
        this._visitText = _visitText;
        this._visitDay = _visitDay;
        this._visitFacility = _visitFacility;
    }

    public Visit(String _visitText, String _visitDay, String _visitFacility) {
        this._visitText = _visitText;
        this._visitDay = _visitDay;
        this._visitFacility = _visitFacility;
    }

    public String get_visitText() {
        return _visitText;
    }

    public void set_visitText(String _visitText) {
        this._visitText = _visitText;
    }

    public String get_visitDay() {
        return _visitDay;
    }

    public void set_visitDay(String _visitDay) {
        this._visitDay = _visitDay;
    }

    public String get_visitFacility() {
        return _visitFacility;
    }

    public void set_visitFacility(String _visitFacility) {
        this._visitFacility = _visitFacility;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }
}
