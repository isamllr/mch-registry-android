package com.mch.registry.css.test;

import android.test.AndroidTestCase;

import app.registry.mch.mchpregnancyguide.data.Recommendation;
import app.registry.mch.mchpregnancyguide.data.RecommendationDataHandler;

/**
 * Created by Isa on 23.07.2014.
 */
public class RecommendationsDataTest extends AndroidTestCase {

        public void setUp(){
            //RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        }

        public void testAddEntry(){
            String testText = "Testtest";
            String testDate = "2014-01-01";
            int testDay = 29;

            RecommendationDataHandler rdh = new RecommendationDataHandler(getContext(), null, null,1);
            Recommendation recommendation = new Recommendation(testText, testDate, testDay);
            rdh.addRecommendation(recommendation);

            //TODO: Complete tests
            //rdh.getRecommendation(); getEntry

            assertEquals(testText, true);
            assertEquals(testText, true);
            assertEquals(testText, true);
        }

        public void tearDown() throws Exception{
            super.tearDown();
        }
}
