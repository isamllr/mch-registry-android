package app.registry.mch.mchpregnancyguide.data;

/**
 * Created by Isa on 18.07.2014.
 */
public class Recommendation {

    private int _id;
    private String _recommendationText;
    private int _recommendationDay;

    public Recommendation() {
    }

    public Recommendation(int id, String recommendationText, int recommendationDay) {
        this._id = id;
        this._recommendationText = recommendationText;
        this._recommendationDay = recommendationDay;
    }

    public Recommendation(String recommendationText, int recommendationDay) {
        this._recommendationText = recommendationText;
        this._recommendationDay = recommendationDay;
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
}
