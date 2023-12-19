package helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haelbito.sdmetadatareader.R;

public class CardAdder {


    public static void addCard(String titleText, String contentText, LinearLayout frame, LayoutInflater inflater, View view){

        // Make sure to check if the view is still in the hierarchy
        if (frame != null) {
            View cardView = inflater.inflate(R.layout.card_view, frame, false); // Inflate with 'frame' as the parent

            TextView title = cardView.findViewById(R.id.textViewTitle);
            TextView content = cardView.findViewById(R.id.textViewContent);

            title.setText(titleText);
            content.setText(contentText);

            frame.addView(cardView);
        }
    }
}
