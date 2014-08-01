package com.mch.registry.ccs.app.recommendation;

import com.mch.registry.ccs.data.Recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationContent {

	public static List<Recommendation> ITEMS = new ArrayList<Recommendation>();
	public static Map<Integer, Recommendation> ITEM_MAP = new HashMap<Integer, Recommendation>();

	static {
		addItem(new Recommendation(1, "Rec 1"));
		addItem(new Recommendation(2, "Rec 2"));
		addItem(new Recommendation(3, "Rec 3"));
	}

	private static void addItem(Recommendation item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.get_recommendationDay(), item);
	}

}
