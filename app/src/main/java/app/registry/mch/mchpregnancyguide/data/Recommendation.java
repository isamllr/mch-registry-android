package app.registry.mch.mchpregnancyguide.data;



/**
 * Created by Isa on 18.07.2014.
 */
public class Recommendation {

    private int _id;
    private String _recommendationText;
    private int _recommendationDay;
    private String _receivedDate;

    public Recommendation() {
    }

    public Recommendation(int _id, String _recommendationText, String _receivedDate, int _recommendationDay) {
        this._id = _id;
        this._recommendationText = _recommendationText;
        this._receivedDate = _receivedDate;
        this._recommendationDay = _recommendationDay;
    }

    public Recommendation(String _recommendationText, String _receivedDate, int _recommendationDay) {
        this._recommendationText = _recommendationText;
        this._receivedDate = _receivedDate;
        this._recommendationDay = _recommendationDay;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public String get_recommendationText() {
        return _recommendationText;
    }

    public void set_recommendationText(String _recommendationText) {
        this._recommendationText = _recommendationText;
    }

    public int get_recommendationDay() {
        return _recommendationDay;
    }

    public void set_recommendationDay(int _recommendationDay) {
        this._recommendationDay = _recommendationDay;
    }

    public String get_receivedDate() {
        return _receivedDate;
    }

    public void set_receivedDate(String _receivedDate) {
        this._receivedDate = _receivedDate;
    }
}
