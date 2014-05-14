package uos.codingsroom.ddmgroup;

import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Delete_Content_Thread;
import uos.codingsroom.ddmgroup.comm.Delete_Reply_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Content_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Reply_Thread;
import uos.codingsroom.ddmgroup.comm.Insert_Reply_Thread;
import uos.codingsroom.ddmgroup.item.CommentItem;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.MyInfoItem;
import uos.codingsroom.ddmgroup.listview.CommentListAdapter;
import uos.codingsroom.ddmgroup.util.SystemValue;
import uos.codingsroom.ddmgroup.util.UrlImageDownloadTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.GlobalApplication;

public class ContentsActivity extends Activity implements OnClickListener {

	private ListView commentsListView;
	private CommentListAdapter commentsListAdapter;
	private View header;

	private TextView contentsReadCount;
	private TextView contentsReplyCount;
	private TextView contentsGroupName;
	private TextView contentsTitle;
	private TextView contentsName;
	private TextView contentsDate;
	private TextView contentsArticle;
	private NetworkImageView contentsProfile;

	private ImageView contentsImage;

	private ImageView contentsLogo;
	private ImageView contentsMenuButton;

	private LinearLayout menuLayout;
	private RelativeLayout menuHelperLayout;

	private TextView menuBackButton;
	private TextView menuEditButton;
	private TextView menuDeleteButton;

	private EditText commentEdit;
	private TextView commentRegister;

	private AlertDialog replyDialog = null;
	private EditText dialogEditText;
	private InputMethodManager imm;

	private Integer currentContentNum;
	private ContentItem conItem; // 인텐트로 넘어온 글 정보 객체

	private String group_name;
	private boolean mode; // 공지사항, 일반 글 여부

	private ArrayList<CommentItem> comItem = new ArrayList<CommentItem>(); // 댓글 배열
	private int com_cnt = 0; // 댓글 개수
	private int SELECT_REPLY_NUM = 0;	// 선택한 댓글의 위치
	private int ADD_REPLY_NUM = -1;	// 새로 추가한 댓글의 번호
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		Bundle bundle = getIntent().getExtras();
		group_name = bundle.getString("group_name");
		currentContentNum = bundle.getInt("content_num");
		mode = bundle.getBoolean("mode"); // 공지사항 여부

		initializeView();

		Get_Content_Thread mThread = new Get_Content_Thread(this, 24, currentContentNum);
		mThread.start(); // 글 내용 받아오는 스레드

		// setListView();
	}

	public void initializeView() {
		commentsListView = (ListView) findViewById(R.id.listview_contents);
		commentsListAdapter = new CommentListAdapter(this);
		header = header();

		commentsListView.addHeaderView(header, null, false);

		contentsReadCount = (TextView) findViewById(R.id.contents_read_count);
		contentsReplyCount = (TextView) findViewById(R.id.contents_reply_count);
		contentsGroupName = (TextView) findViewById(R.id.contents_group_name);
		contentsTitle = (TextView) findViewById(R.id.contents_title);
		contentsName = (TextView) findViewById(R.id.contents_name);
		contentsDate = (TextView) findViewById(R.id.contents_date);
		contentsArticle = (TextView) findViewById(R.id.contents_article);
		contentsProfile = (NetworkImageView) findViewById(R.id.contents_profile);

		contentsImage = (ImageView) findViewById(R.id.contents_image);
		contentsLogo = (ImageView) findViewById(R.id.contents_logo_img);

		contentsMenuButton = (ImageView) findViewById(R.id.contents_menu_btn);

		menuLayout = (LinearLayout) findViewById(R.id.contents_menu_layout);
		menuHelperLayout = (RelativeLayout) findViewById(R.id.contents_menu_helper_layout);

		menuBackButton = (TextView) findViewById(R.id.contents_return_btn);
		menuBackButton.setOnClickListener(this);
		menuEditButton = (TextView) findViewById(R.id.contents_edit_btn);
		menuEditButton.setOnClickListener(this);
		menuDeleteButton = (TextView) findViewById(R.id.contents_delete_btn);
		menuDeleteButton.setOnClickListener(this);

		commentEdit = (EditText) findViewById(R.id.edittext_comment);
		commentRegister = (TextView) findViewById(R.id.button_comment_register);
		commentRegister.setOnClickListener(this);
	}

	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header_contents, null);
		return header;
	}
	
	// 핸들러에서 보낸 메시지를 토스트로 출력하는 함수
	public void viewMessage(String message){
		Toast.makeText(this,message, Toast.LENGTH_LONG).show();
	}
		
	// 댓글 객체를 세팅하는 함수
	public void setCommentItem(CommentItem mItem) {
		comItem.add(com_cnt++, mItem);
	}

	// 받아온 댓글들을 뷰에 생성하는 함수
	public void setListView() {
		if (conItem.getMemberNum() == MainActivity.getMyInfoItem().getMyMemNum()) {
			contentsMenuButton.setOnClickListener(this);
			menuHelperLayout.setOnClickListener(this);
			contentsLogo.setOnClickListener(this);
		} else {
			contentsMenuButton.setVisibility(View.GONE);
		}

		for (int i = 0; i < comItem.size(); i++) {
			commentsListAdapter.addItem(comItem.get(i));
		}

		commentsListView.setAdapter(commentsListAdapter);
		commentsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SELECT_REPLY_NUM = arg2 - 1; // 댓글의 인덱스
				final MyInfoItem myInfo = MainActivity.getMyInfoItem();

				if(myInfo.getMyMemNum() == comItem.get(SELECT_REPLY_NUM).getMem_num()){	// 댓글 작성자인지 확인 
					replyDialog = createReplyDialog();
					replyDialog.show();
				}
				else{
					return;
				}
			}
		});
	}
	
	// 추가한 댓글의 번호를 얻어오는 함수
	public void setCommentNum(int reply_num){
		ADD_REPLY_NUM = reply_num;
	}
	
	// 댓글 한 개를 뷰에 추가하는 함수
	public void addComment() {
		final MyInfoItem myInfo = MainActivity.getMyInfoItem();
		CommentItem addItem = new CommentItem(ADD_REPLY_NUM, myInfo.getMyMemNum(),
				commentEdit.getEditableText().toString(), "방금 막",
				myInfo.getMyName(), myInfo.getMyProfileUrl()); // 임시
		Log.i("MyTag","add comment >> " + ADD_REPLY_NUM + " // " + myInfo.getMyMemNum());
		commentsListAdapter.addItem(addItem);
		comItem.add(com_cnt++, addItem);

		commentsListView.setAdapter(commentsListAdapter);

		commentsListView.setSelection(commentsListAdapter.getCount() - 1); // 가장 맨 아래에 스크롤이 내려오게 함
		commentEdit.setText("");
	}

	// 댓글 한 개를 뷰에서 삭제하는 함수
	public void deleteComment() {													// 객체
		commentsListAdapter.removeItem(SELECT_REPLY_NUM);
		comItem.remove(SELECT_REPLY_NUM);
		com_cnt--;
		
//		commentsListView.setAdapter(commentsListAdapter);
		commentsListAdapter.notifyDataSetChanged();
		commentsListView.clearChoices();
		commentsListView.setSelection(commentsListAdapter.getCount() - 1); // 가장 맨 아래에 스크롤이 내려오게 함
	}
		
	// 댓글 수정, 삭제 물어보는 다이얼로그 생성함수
	private AlertDialog createReplyDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		String[] kinds = { "수정하기", "삭제하기" };
		ab.setTitle("댓글");

		Log.i("MyTag", "댓글 위치 : " + SELECT_REPLY_NUM);
		ab.setItems(kinds, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					showEditTextDialog(100); // 댓글 수정하는 다이얼로그 생성
					// 댓글 내용, 위치 넘겨야 함
				} else if (which == 1) {
					Log.i("MyTag", "댓글 삭제하기");
					Delete_Reply_Thread drThread = new Delete_Reply_Thread(ContentsActivity.this,30,comItem.get(SELECT_REPLY_NUM).getIndexNum());
					drThread.start();
				}
				setDismiss(replyDialog);
			}
		});

		ab.setNeutralButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setDismiss(replyDialog);
			}
		});

		return ab.create();
	}

	// 댓글 수정하는 다이얼로그 생성하는 함수
	private void showEditTextDialog(int what) {
		final LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.dialog_input, null);
		final EditText editText_contents;
		final TextView editText_Label;
		final String dialogTitle = "댓글 내용";
		final String dialogLabel = "내용을 입력하세요.";

		// 댓글의 댓글 선택시 자동으로 키보드 생성해주는 부분
		dialogEditText = (EditText) findViewById(R.id.editReply);
		editText_contents = (EditText) layout.findViewById(R.id.editReply);
		editText_Label = (TextView) layout.findViewById(R.id.editReply_label);
		editText_Label.setText(dialogLabel);

		imm.showSoftInput(dialogEditText, InputMethodManager.SHOW_FORCED);

		new AlertDialog.Builder(this).setTitle(dialogTitle).setView(layout)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String contents = editText_contents.getText().toString();
						// SharedPreferences pref = getSharedPreferences("prefs", 0);
						// final String myName = pref.getString("myName", "관리자"); // 프리퍼런스

						if (contents.equals("")) {
							Toast.makeText(getApplicationContext(), "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
						} else {
							// Connect_Thread mThread = new Connect_Thread(MapActivity.this, 12, tItem);
							// mThread.start();

							// 댓글 수정하는 스레드 삽입
						}
						CloseKeyboard();
					}

					private void CloseKeyboard() {
						imm.hideSoftInputFromWindow(editText_contents.getWindowToken(), 0);
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						CloseKeyboard();
						Toast.makeText(getApplicationContext(), "댓글 수정을 취소하였습니다", Toast.LENGTH_SHORT).show();
					}

					private void CloseKeyboard() {
						imm.hideSoftInputFromWindow(editText_contents.getWindowToken(), 0);
					}
				}).show();
	}

	// 다이얼로그 뷰에서 해제
	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	// 클릭 이벤트
	@Override
	public void onClick(View v) {
		final MyInfoItem myInfo = MainActivity.getMyInfoItem();
		switch (v.getId()) {
		case R.id.contents_logo_img:
			menuLayout.setVisibility(View.VISIBLE);
			menuHelperLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.contents_menu_btn:
			menuLayout.setVisibility(View.VISIBLE);
			menuHelperLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.contents_return_btn:
			menuLayout.setVisibility(View.GONE);
			menuHelperLayout.setVisibility(View.GONE);
			break;

		case R.id.contents_menu_helper_layout:
			menuLayout.setVisibility(View.GONE);
			menuHelperLayout.setVisibility(View.GONE);
			break;

		case R.id.contents_edit_btn:		// 글 수정
			if(conItem.getMemberNum() == myInfo.getMyMemNum()){		// 작성자인지 확인
				editThisContent();
			}
			break;

		case R.id.contents_delete_btn:		// 글 삭제
			if(conItem.getMemberNum() == myInfo.getMyMemNum()){		// 작성자인지 확인
				new AlertDialog.Builder(this).setTitle("글을 삭제하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Delete_Content_Thread dThread = new Delete_Content_Thread(ContentsActivity.this,26,conItem.getIndexNum());
						dThread.start();
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}	
				}).show();
				
			}
			break;

		case R.id.button_comment_register:	// 댓글 추가
			final String commentText = commentEdit.getEditableText().toString();

			if (commentText.equals("")) { // 아무 댓글 내용없이 등록하려고 할 경우
				Toast.makeText(this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
			} else {
				Insert_Reply_Thread iThread = new Insert_Reply_Thread(this, 27, conItem.getIndexNum(), MainActivity
						.getMyInfoItem().getMyMemNum(), commentText);
				iThread.start(); // 댓글 삽입하는 스레드

			}
			break;

		default:
			break;
		}

	}

	// 글 수정하는 액티비티로 넘어가는 함수
	public void editThisContent() {
		ContentIntent contentIntent = new ContentIntent(this, group_name, conItem.getBoardCategory(),
				conItem.getIndexNum(), conItem.getMemberNum(), mode);

		startActivity(contentIntent.put_intent(ModifyActivity.class));

		menuLayout.setVisibility(View.GONE);
		menuHelperLayout.setVisibility(View.GONE);
	}

	// 글 정보 세팅하는 함수
	public void setContentItem(ContentItem mItem) {
		conItem = new ContentItem();
		conItem = mItem;
	}

	// 뷰에 글정보 추가하는 함수
	public void setContentView() {
		contentsReadCount.setText(Integer.toString(conItem.getReadCount()) + "명 읽음");
		contentsReplyCount.setText(Integer.toString(conItem.getReplyCount()));
		contentsGroupName.setText(group_name);
		contentsTitle.setText(conItem.getTitle());
		contentsName.setText(conItem.getName());
		contentsDate.setText(conItem.getDate());
		contentsArticle.setText(conItem.getArticle());
		setProfileURL(conItem.getThumbnail());
		if (conItem.getImgUrl() != null) {
			new UrlImageDownloadTask(contentsImage).execute(SystemValue.imageConn + "" + conItem.getImgUrl());
		}
		Get_Reply_Thread rThread = new Get_Reply_Thread(this, 28, currentContentNum);
		rThread.start(); // 댓글 받아오는 스레드

		// contentsImage;
	}

	public void setProfileURL(final String profileImageURL) {
		if (contentsProfile != null && profileImageURL != null) {
			Application app = GlobalApplication.getGlobalApplicationContext();
			if (app == null)
				throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
			contentsProfile.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
		}
	}
}
