package org.heeyoung.multimemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import org.heeyoung.multimemo.R;
import org.heeyoung.multimemo.common.TitleBitmapButton;
import org.heeyoung.multimemo.db.MemoDatabase;

import java.io.File;

/**
 * 멀티메모 메인 액티비티
 *
 * @author Mike
 */
public class MultiMemoActivity extends Activity {
	public static final String TAG = "MultiMemoActivity";

	/**
	 * 메모 리스트뷰
	 */
	ListView mMemoListView;

	/**
	 * 메모 리스트 어댑터
	 */
	MemoListAdapter mMemoListAdapter;

	/**
	 * 메모 갯수
	 */
	int mMemoCount = 0;


	/**
	 * 데이터베이스 인스턴스
	 */
	public static MemoDatabase mDatabase = null;




	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimemo);

        // SD Card checking
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    		Toast.makeText(this, "SD 카드가 없습니다. SD 카드를 넣은 후 다시 실행하십시오.", Toast.LENGTH_LONG).show();
    		return;
    	} else {
    		String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    		if (!BasicInfo.ExternalChecked && externalPath != null) {
    			BasicInfo.ExternalPath = externalPath + File.separator;
    			Log.d(TAG, "ExternalPath : " + BasicInfo.ExternalPath);

    			BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
    			BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;

    			BasicInfo.ExternalChecked = true;
    		}
    	}


        // 메모 리스트
        mMemoListView = (ListView)findViewById(R.id.memoList);
    	mMemoListAdapter = new MemoListAdapter(this);
    	mMemoListView.setAdapter(mMemoListAdapter);
    	mMemoListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				viewMemo(position);
			}
		});


        // 새 메모 버튼 설정
        TitleBitmapButton newMemoBtn = (TitleBitmapButton)findViewById(R.id.newMemoBtn);
    	newMemoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "newMemoBtn clicked.");

				Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
				intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_INSERT);
				startActivityForResult(intent, BasicInfo.REQ_INSERT_ACTIVITY);
			}
		});


        TitleBitmapButton closeBtn = (TitleBitmapButton)findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
				startActivity(intent);
				finish();
			}
		});


		checkDangerousPermissions();
	}

	private void checkDangerousPermissions() {
		String[] permissions = {
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA
		};

		int permissionCheck = PackageManager.PERMISSION_GRANTED;
		for (int i = 0; i < permissions.length; i++) {
			permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
			if (permissionCheck == PackageManager.PERMISSION_DENIED) {
				break;
			}
		}

	}



	protected void onStart() {

        // 데이터베이스 열기
        openDatabase();

        // 메모 데이터 로딩
        loadMemoListData();


		super.onStart();
	}





	/**
     * 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
     */
    public void openDatabase() {
		// open database
    	if (mDatabase != null) {
    		mDatabase.close();
    		mDatabase = null;
    	}

    	mDatabase = MemoDatabase.getInstance(this);
    	boolean isOpen = mDatabase.open();
    	if (isOpen) {
    		Log.d(TAG, "Memo database is open.");
    	} else {
    		Log.d(TAG, "Memo database is not open.");
    	}
    }



    /**
     * 메모 리스트 데이터 로딩
     */
    public int loadMemoListData() {
		String SQL = "select _id, INPUT_DATE, CONTENT_TEXT, ID_PHOTO from MEMO order by INPUT_DATE desc";


		int recordCount = -1;
		if (MultiMemoActivity.mDatabase != null) {
			Cursor outCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);

			recordCount = outCursor.getCount();
			Log.d(TAG, "cursor count : " + recordCount + "\n");

			mMemoListAdapter.clear();
			Resources res = getResources();

			for (int i = 0; i < recordCount; i++) {
				outCursor.moveToNext();

				String memoId = outCursor.getString(0);

				String dateStr = outCursor.getString(1);
				if (dateStr.length() > 10) {
					dateStr = dateStr.substring(0, 10);
				}

				String memoStr = outCursor.getString(2);
				String photoId = outCursor.getString(3);
				String photoUriStr = getPhotoUriStr(photoId);


				mMemoListAdapter.addItem(new MemoListItem(memoId, dateStr, memoStr, photoId, photoUriStr));
			}

			outCursor.close();

			mMemoListAdapter.notifyDataSetChanged();
	   }

	   return recordCount;
    }

	/**
	 * 사진 데이터 URI 가져오기
	 */
	public String getPhotoUriStr(String id_photo) {
		String photoUriStr = null;
		if (id_photo != null && !id_photo.equals("-1")) {
			String SQL = "select URI from " +MemoDatabase.TABLE_PHOTO+ " where _ID = " + id_photo+" ";
			Cursor photoCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
	    	if (photoCursor.moveToNext()) {
	    		photoUriStr = photoCursor.getString(0);
	    	}
	    	photoCursor.close();
		} else if(id_photo == null || id_photo.equals("-1")) {
			photoUriStr = "";
		}

		return photoUriStr;
	}


    private void viewMemo(int position) {
    	MemoListItem item = (MemoListItem)mMemoListAdapter.getItem(position);

    	// 메모 보기 액티비티 띄우기
		Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
		intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);
		intent.putExtra(BasicInfo.KEY_MEMO_ID, item.getId());
		intent.putExtra(BasicInfo.KEY_MEMO_DATE, item.getData(0));
		intent.putExtra(BasicInfo.KEY_MEMO_TEXT, item.getData(1));

		intent.putExtra(BasicInfo.KEY_ID_PHOTO, item.getData(2));
		intent.putExtra(BasicInfo.KEY_URI_PHOTO, item.getData(3));

		startActivityForResult(intent, BasicInfo.REQ_VIEW_ACTIVITY);
    }



    /**
     * 다른 액티비티의 응답 처리
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
			case BasicInfo.REQ_INSERT_ACTIVITY:
				if(resultCode == RESULT_OK) {
					loadMemoListData();
				}

				break;

			case BasicInfo.REQ_VIEW_ACTIVITY:
				loadMemoListData();

				break;

		}
	}



}