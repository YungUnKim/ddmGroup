package uos.codingsroom.ddmgroup;

import java.util.ArrayList;

import uos.codingsroom.ddmgroup.comm.Get_Content_Thread;
import uos.codingsroom.ddmgroup.comm.Get_Reply_Thread;
import uos.codingsroom.ddmgroup.comm.Insert_Reply_Thread;
import uos.codingsroom.ddmgroup.item.CommentItem;
import uos.codingsroom.ddmgroup.item.ContentItem;
import uos.codingsroom.ddmgroup.item.MyInfoItem;
import uos.codingsroom.ddmgroup.listview.CommentListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
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

	private ContentItem conItem; // 글 정보 객체
	private ContentItem tempItem = new ContentItem(); // 인텐트로 넘어온 글 정보 객체

	private String group_name;
	private int myNum; // 자신의 회원번호
	private boolean mode; // 공지사항, 일반 글 여부

	private ArrayList<CommentItem> comItem = new ArrayList<CommentItem>(); // 댓글 배열
	private int com_cnt = 0; // 댓글 개수

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		Bundle bundle = getIntent().getExtras();
		group_name = bundle.getString("board_name");
		tempItem.setBoardCategory(bundle.getInt("board_num"));
		tempItem.setIndexNum(bundle.getInt("content_num"));
		tempItem.setMemberNum(bundle.getInt("mem_num"));
		mode = bundle.getBoolean("mode"); // 공지사항 여부
		// myNum = bundle.getInt("myNum"); // 자신의 회원번호
		myNum = 23; // 임시로

		initializeView();

		Get_Content_Thread mThread = new Get_Content_Thread(this, 24, tempItem.getBoardCategory(), tempItem.getIndexNum(), tempItem.getMemberNum());
		mThread.start(); // 글 내용 받아오는 스레드

		Get_Reply_Thread rThread = new Get_Reply_Thread(this, 28, tempItem.getIndexNum());
		rThread.start(); // 댓글 받아오는 스레드

		setListView(); // 추후 이동
	}

	// 댓글 아이템을 설정하는함수
	public void setCommentItem(CommentItem mItem) {
		comItem.add(com_cnt++, mItem);
	}

	// 댓글을 뷰에 보여주는 함수
	public void setListView() {
		for (int i = 0; i < comItem.size(); i++) {
			commentsListAdapter.addItem(comItem.get(i));
			Log.i("MyTag", i + "번째 댓글 >> " + comItem.get(i).getKakaoUrl());
		}

		commentsListView.setAdapter(commentsListAdapter);
		commentsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int select_reply_num = arg2 - 1; // 댓글의 인덱스
				/*
				 * if(!comItem.get(select_reply_num).get){ // 해당 댓글의 주인이 아닐 경우 return; }
				 */
				replyDialog = createReplyDialog(select_reply_num);
				replyDialog.show();
			}
		});
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

		contentsImage = (ImageView) findViewById(R.id.contents_image);
		contentsLogo = (ImageView) findViewById(R.id.contents_logo_img);

		contentsMenuButton = (ImageView) findViewById(R.id.contents_menu_btn);

		menuLayout = (LinearLayout) findViewById(R.id.contents_menu_layout);
		menuHelperLayout = (RelativeLayout) findViewById(R.id.contents_menu_helper_layout);

		if (tempItem.getMemberNum() == MainActivity.getMyInfoItem().getMyMemNum()) {
			contentsMenuButton.setOnClickListener(this);
			menuHelperLayout.setOnClickListener(this);
			contentsLogo.setOnClickListener(this);
		} else {
			contentsMenuButton.setVisibility(View.GONE);
		}

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

	// 댓글 하나를 추가하는 함수
	public void addComment() {
		final MyInfoItem myInfo = MainActivity.getMyInfoItem();
		CommentItem addItem = new CommentItem(conItem.getIndexNum(), commentEdit.getEditableText().toString(), "방금 막", myInfo.getMyName(), myInfo.getMyProfileUrl()); // 임시 객체
		commentsListAdapter.addItem(addItem);
		comItem.add(com_cnt++, addItem);

		commentsListView.setAdapter(commentsListAdapter);

		commentsListView.setSelection(commentsListAdapter.getCount() - 1); // 가장 맨 아래에 스크롤이 내려오게 함
		commentEdit.setText("");
	}

	// 댓글 수정, 삭제 물어보는 다이얼로그 생성함수
	private AlertDialog createReplyDialog(int what) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		String[] kinds = { "수정하기", "삭제하기" };
		ab.setTitle("댓글");

		Log.i("MyTag", "댓글 위치 : " + what);
		ab.setItems(kinds, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// kind = which + 1;
				if (which == 0) {
					showEditTextDialog(100); // 댓글 수정하는 다이얼로그 생성
					// 댓글 내용, 위치 넘겨야 함
				} else if (which == 1) {
					Log.i("MyTag", "삭제하기");
					// 삭제 통신 스레드 삽입
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

		new AlertDialog.Builder(this).setTitle(dialogTitle).setView(layout).setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
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

		case R.id.contents_edit_btn:
			editThisContent();
			break;

		case R.id.contents_delete_btn:

			break;

		case R.id.button_comment_register:
			final String commentText = commentEdit.getEditableText().toString();

			if (commentText.equals("")) { // 아무 댓글 내용없이 등록하려고 할 경우
				Toast.makeText(this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
			} else {
				Insert_Reply_Thread iThread = new Insert_Reply_Thread(this, 27, conItem.getIndexNum(), myNum, commentText);
				iThread.start(); // 댓글 삽입하는 스레드

			}
			break;

		default:
			break;
		}

	}

	public void editThisContent() {
		ContentIntent contentIntent = new ContentIntent(this, group_name, conItem.getBoardCategory(), conItem.getIndexNum(), conItem.getMemberNum(), mode);

		startActivity(contentIntent.put_intent(ModifyActivity.class));

		menuLayout.setVisibility(View.GONE);
		menuHelperLayout.setVisibility(View.GONE);
	}

	// contentItem 세팅하는 함수
	public void setContentItem(ContentItem mItem) {
		conItem = new ContentItem();
		conItem = mItem;
		// Log.i("MyTag", "Content title : " + conItem.getTitle() + " / num : " + conItem.getIndexNum());
	}

	// View에 contentItem 집어넣는 함수
	public void setContentView() {
		contentsReadCount.setText(Integer.toString(conItem.getReadCount()) + "명 읽음");
		contentsReplyCount.setText(Integer.toString(conItem.getReplyCount()));
		contentsGroupName.setText(group_name);
		contentsTitle.setText(conItem.getTitle());
		contentsName.setText(conItem.getName());
		contentsDate.setText(conItem.getDate());
		contentsArticle.setText(conItem.getArticle());

		Log.i("MyTag", "Content title : " + conItem.getTitle() + " / mode : " + mode);

		// contentsImage;
	}
}
