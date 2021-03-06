package org.heeyoung.multimemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.heeyoung.multimemo.R;

public class MemoListItemView extends LinearLayout {

	private ImageView itemPhoto;

	private TextView itemDate;

	private TextView itemText;


    Bitmap bitmap;

	public MemoListItemView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.memo_listitem, this, true);

		itemPhoto = (ImageView) findViewById(R.id.itemPhoto);

		itemDate = (TextView) findViewById(R.id.itemDate);

		itemText = (TextView) findViewById(R.id.itemText);

	}

	public void setContents(int index, String data) {
		if (index == 0) {
			itemDate.setText(data);
		} else if (index == 1) {
			itemText.setText(data);
		}

		else if (index == 2) {
			if (data == null || data.equals("-1") || data.equals("")) {
			} else {
			}
		}

			else if (index == 3) {
			if (data == null || data.equals("-1") || data.equals("")) {
				itemPhoto.setImageResource(R.drawable.person);
			} else {
                if (bitmap != null) {
                    bitmap.recycle();
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + data, options);
				Log.wtf("wtf", data+"!");

                itemPhoto.setImageBitmap(bitmap);

            }
		} else {
			throw new IllegalArgumentException();
		}
	}



}
